package com.fastcampus.board.service;

import com.fastcampus.board.exception.post.PostNotFoundException;
import com.fastcampus.board.model.Post;
import com.fastcampus.board.model.PostPatchRequestBody;
import com.fastcampus.board.model.PostPostRequestBody;
import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.repository.PostEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostEntityRepository postEntityRepository;

    public PostService(PostEntityRepository postEntityRepository) {
        this.postEntityRepository = postEntityRepository;
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
    public Post createPost(PostPostRequestBody request) {
        var postEntity = new PostEntity();
        postEntity.setBody(request.body());
        var saveEntity = postEntityRepository.save(postEntity);

        return Post.from(saveEntity);
    }

    @Transactional
    public Post updatePost(Long postId, PostPatchRequestBody body) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId)
        );

        postEntity.setBody(body.body());

        return Post.from(postEntity);
    }

    @Transactional
    public void deletePost(Long postId) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId)
        );

        postEntityRepository.delete(postEntity);
    }
}
