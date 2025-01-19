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
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(required = false) String query
    ) {
        log.info("query = {}", query);
        var users = userService.getUsers(query);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        var user = userService.getUser(username);

        return ResponseEntity.ok(user);
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
