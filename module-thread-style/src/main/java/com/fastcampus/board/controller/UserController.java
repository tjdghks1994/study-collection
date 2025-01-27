package com.fastcampus.board.controller;

import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.user.*;
import com.fastcampus.board.service.PostService;
import com.fastcampus.board.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final PostService postService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(required = false) String query,
            Authentication authentication
    ) {
        log.info("query = {}", query);
        var users = userService.getUsers(query, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(
            @PathVariable String username,
            Authentication authentication
    ) {
        var user = userService.getUser(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/posts")
    public ResponseEntity<List<Post>> getPostsByUsername(
            @PathVariable String username,
            Authentication authentication
    ) {
        var posts = postService.getPostsByUsername(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(posts);
    }

    @PostMapping("/{username}/follows")
    public ResponseEntity<User> follow(@PathVariable String username, Authentication authentication) {
        var user = userService.follow(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{username}/follows")
    public ResponseEntity<User> unfollow(@PathVariable String username, Authentication authentication) {
        var user = userService.unfollow(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<User>> getFollowersByUser(
            @PathVariable String username,
            Authentication authentication
    ) {
        var followers = userService.getFollowersByUsername(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{username}/followings")
    public ResponseEntity<List<User>> getFollowingsByUser(
            @PathVariable String username,
            Authentication authentication
    ) {
        var followings = userService.getFollowingsByUsername(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(followings);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<User> updateUser(
            @PathVariable String username,
            @RequestBody UserPatchRequestBody requestBody,
            Authentication authentication
    ) {
        var user = userService.updateUser(username, requestBody, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> signUp(@Valid @RequestBody UserSignUpRequestBody userSignUpRequestBody) {
        var user = userService.signUp(
                userSignUpRequestBody.username(),
                userSignUpRequestBody.password()
        );

        return ResponseEntity.ok(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(
            @Valid @RequestBody UserLoginRequestBody userLoginRequestBody
    ) {
        var response = userService.login(
                userLoginRequestBody.username(),
                userLoginRequestBody.password()
        );

        return ResponseEntity.ok(response);
    }

}
