package com.fastcampus.board.controller;

import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostPatchRequestBody;
import com.fastcampus.board.model.post.PostPostRequestBody;
import com.fastcampus.board.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(){
        log.info("GET /api/v1/posts");
        var posts = postService.getPosts();

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId){
        log.info("GET /api/v1/posts/{}", postId);
        var post = postService.getPostById(postId);

        return ResponseEntity.ok(post);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable String username) {
        var posts = postService.getPostsByUsername(username);

        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestBody PostPostRequestBody body,
            Authentication authentication
    ){
        log.info("POST /api/v1/posts");
        var post = postService.createPost(body, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(post);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long postId,
            @RequestBody PostPatchRequestBody body,
            Authentication authentication
    ){
        log.info("PATCH /api/v1/posts/{}", postId);
        var updatePost = postService.updatePost(postId, body, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(updatePost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            Authentication authentication
    ){
        log.info("DELETE /api/v1/posts/{}", postId);
        postService.deletePost(postId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.noContent().build();
    }
}
