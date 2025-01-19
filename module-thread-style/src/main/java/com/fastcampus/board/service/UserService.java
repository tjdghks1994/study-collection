package com.fastcampus.board.service;

import com.fastcampus.board.exception.user.UserAlreadyExistsException;
import com.fastcampus.board.exception.user.UserNotAllowedException;
import com.fastcampus.board.exception.user.UserNotFoundException;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.user.User;
import com.fastcampus.board.model.user.UserAuthenticationResponse;
import com.fastcampus.board.model.user.UserPatchRequestBody;
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

    public UserService(UserEntityRepository userEntityRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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
}
