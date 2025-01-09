package com.fastcampus.board.controller;

import com.fastcampus.board.model.Post;
import com.fastcampus.board.model.PostPatchRequestBody;
import com.fastcampus.board.model.PostPostRequestBody;
import com.fastcampus.board.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(){
        var posts = postService.getPosts();

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId){
        var post = postService.getPostById(postId);

        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody body){
        var post = postService.createPost(body);

        return ResponseEntity.ok(post);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable Long postId, @RequestBody PostPatchRequestBody body){
        var updatePost = postService.updatePost(postId, body);

        return ResponseEntity.ok(updatePost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);

        return ResponseEntity.noContent().build();
    }
}
