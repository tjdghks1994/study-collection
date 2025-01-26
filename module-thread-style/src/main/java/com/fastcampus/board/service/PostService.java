package com.fastcampus.board.service;

import com.fastcampus.board.exception.post.PostNotFoundException;
import com.fastcampus.board.exception.user.UserNotAllowedException;
import com.fastcampus.board.exception.user.UserNotFoundException;
import com.fastcampus.board.model.entity.LikeEntity;
import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostPatchRequestBody;
import com.fastcampus.board.model.post.PostPostRequestBody;
import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.repository.LikeEntityRepository;
import com.fastcampus.board.repository.PostEntityRepository;
import com.fastcampus.board.repository.UserEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;

    public PostService(PostEntityRepository postEntityRepository,
                       UserEntityRepository userEntityRepository,
                       LikeEntityRepository likeEntityRepository) {
        this.postEntityRepository = postEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.likeEntityRepository = likeEntityRepository;
    }

    public List<Post> getPosts() {
        return postEntityRepository.findAll().stream()
                .map(Post::from)
                .collect(Collectors.toList());
    }

    public Post getPostById(Long postId) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId)
        );

        return Post.from(postEntity);
    }

    @Transactional
    public Post createPost(PostPostRequestBody request, UserEntity currentUser) {
        var saveEntity = postEntityRepository.save(
                PostEntity.of(request.body(), currentUser)
        );

        return Post.from(saveEntity);
    }

    @Transactional
    public Post updatePost(Long postId, PostPatchRequestBody body, UserEntity currentUser) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId)
        );

        if (!postEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        postEntity.setBody(body.body());

        return Post.from(postEntity);
    }

    @Transactional
    public void deletePost(Long postId, UserEntity currentUser) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId)
        );

        if (!postEntity.getUser().equals(currentUser)) {
            throw new UserNotAllowedException();
        }

        postEntityRepository.delete(postEntity);
    }

    public List<Post> getPostsByUsername(String username) {
        var userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        return postEntityRepository.findByUser(userEntity).stream()
                .map(Post::from).collect(Collectors.toList());
    }

    @Transactional
    public Post toggleLike(Long postId, UserEntity currentUser) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException(postId)
        );

        var likeEntity = likeEntityRepository.findByUserAndPost(currentUser, postEntity);

        if (likeEntity.isPresent()) {
            likeEntityRepository.delete(likeEntity.get());
            postEntity.setLikesCount(Math.max(0, postEntity.getLikesCount() - 1));
        } else {
            likeEntityRepository.save(LikeEntity.of(currentUser, postEntity));
            postEntity.setLikesCount(postEntity.getLikesCount() + 1);
        }

        return Post.from(postEntity);
    }
}
