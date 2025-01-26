package com.fastcampus.board.service;

import com.fastcampus.board.exception.follow.FollowAlreadyExistsException;
import com.fastcampus.board.exception.follow.FollowNotFoundException;
import com.fastcampus.board.exception.follow.InvalidFollowException;
import com.fastcampus.board.exception.user.UserAlreadyExistsException;
import com.fastcampus.board.exception.user.UserNotAllowedException;
import com.fastcampus.board.exception.user.UserNotFoundException;
import com.fastcampus.board.model.entity.FollowEntity;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.user.User;
import com.fastcampus.board.model.user.UserAuthenticationResponse;
import com.fastcampus.board.model.user.UserPatchRequestBody;
import com.fastcampus.board.repository.FollowEntityRepository;
import com.fastcampus.board.repository.UserEntityRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final FollowEntityRepository followEntityRepository;

    public UserService(UserEntityRepository userEntityRepository, BCryptPasswordEncoder passwordEncoder,
                       JwtService jwtService, FollowEntityRepository followEntityRepository) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.followEntityRepository = followEntityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username)
        );
    }

    @Transactional
    public User signUp(String username, String password) {
        userEntityRepository.findByUsername(username)
                .ifPresent((user) -> {
                    throw new UserAlreadyExistsException(username);
                });

        var saveUserEntity = userEntityRepository.save(
                UserEntity.of(username, passwordEncoder.encode(password))
        );

        return User.from(saveUserEntity);
    }

    public UserAuthenticationResponse login(String username, String password) {
        var userEntity = userEntityRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException(username)
        );

        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            var accessToken = jwtService.generateAccessToken(userEntity);

            return new UserAuthenticationResponse(accessToken);
        } else {
            throw new UserNotFoundException(username);
        }
    }

    public List<User> getUsers(String query) {
        List<UserEntity> userEntities;

        if (query != null && !query.isBlank()) {
            userEntities = userEntityRepository.findByUsernameContaining(query);
        } else {
            userEntities = userEntityRepository.findAll();
        }

        return userEntities.stream().map(User::from)
                .collect(Collectors.toList());
    }

    public User getUser(String username) {
        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return User.from(userEntity);
    }

    @Transactional
    public User updateUser(String username, UserPatchRequestBody userPatchRequestBody, UserEntity currentUser) {
        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        if (!userEntity.equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        if (userPatchRequestBody.description() != null) {
            userEntity.setDescription(userPatchRequestBody.description());
        }

        return User.from(userEntity);
    }

    @Transactional
    public User follow(String username, UserEntity currentUser) {
        var following = userEntityRepository.findByUsername(username).orElseThrow(() ->
            new UserNotFoundException(username)
        );
        // 자기 자신은 팔로우 불가
        if (following.equals(currentUser)) {
            throw new InvalidFollowException("A User cannot follow themselves.");
        }
        // 이미 팔로우를 한 경우
        followEntityRepository.findByFollowerAndFollowing(currentUser, following)
                .ifPresent(
                        followEntity -> {
                            throw new FollowAlreadyExistsException(currentUser, following);
                        }
                );
        // 팔로우 처리
        followEntityRepository.save(
                FollowEntity.of(currentUser, following)
        );

        following.setFollowersCount(following.getFollowersCount() + 1);
        currentUser.setFollowingsCount(currentUser.getFollowingsCount() + 1);

        return User.from(following);
    }

    @Transactional
    public User unfollow(String username, UserEntity currentUser) {
        var following = userEntityRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(username)
        );

        // 자기 자신은 언팔로우 불가
        if (following.equals(currentUser)) {
            throw new InvalidFollowException("A User cannot unfollow themselves.");
        }

        // 팔로우를 하지 않은 경우 언팔로우 불가
        var followEntity = followEntityRepository.findByFollowerAndFollowing(currentUser, following)
                .orElseThrow( ()->
                        new FollowNotFoundException(currentUser, following)
                );
        // 언팔로우
        followEntityRepository.delete(followEntity);

        following.setFollowersCount(Math.max(0, following.getFollowersCount() - 1));
        currentUser.setFollowingsCount(Math.max(0, following.getFollowingsCount() - 1));

        return User.from(following);
    }

    public List<User> getFollowersByUsername(String username) {
        var following = userEntityRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(username)
        );

        var followEntities = followEntityRepository.findByFollowing(following);

        return followEntities.stream().map(
                follow -> User.from(follow.getFollower())).collect(Collectors.toList());
    }

    public List<User> getFollowingsByUsername(String username) {
        var follower = userEntityRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(username)
        );

        var followEntities = followEntityRepository.findByFollower(follower);

        return followEntities.stream().map(
                follow -> User.from(follow.getFollowing())).collect(Collectors.toList());
    }
}
