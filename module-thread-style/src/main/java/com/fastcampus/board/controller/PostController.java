package com.fastcampus.board.controller;

import com.fastcampus.board.model.Post;
import com.fastcampus.board.model.PostPostRequestBody;
import com.fastcampus.board.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(){
        List<Post> posts = postService.getPosts();

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId){
        Optional<Post> matchingPost = postService.getPostById(postId);

        return matchingPost.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostPostRequestBody body){
        var post = postService.createPost(body);

        return ResponseEntity.ok(post);
    }
}
