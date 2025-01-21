package com.fastcampus.board.controller;

import com.fastcampus.board.model.entity.UserEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.post.PostPatchRequestBody;
import com.fastcampus.board.model.post.PostPostRequestBody;
import com.fastcampus.board.model.reply.Reply;
import com.fastcampus.board.model.reply.ReplyPatchRequestBody;
import com.fastcampus.board.model.reply.ReplyPostRequestBody;
import com.fastcampus.board.service.PostService;
import com.fastcampus.board.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/replies")
public class ReplyController {

    private final ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @GetMapping
    public ResponseEntity<List<Reply>> getRepliesByPostId(
            @PathVariable Long postId
    ) {
        var replies = replyService.getRepliesByPostId(postId);

        return ResponseEntity.ok(replies);
    }

    @PostMapping
    public ResponseEntity<Reply> createReply(
            @PathVariable Long postId,
            @RequestBody ReplyPostRequestBody body,
            Authentication authentication
    ){
        var reply = replyService.createReply(postId, body, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(reply);
    }

    @PatchMapping("/{replyId}")
    public ResponseEntity<Reply> updateReply(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            @RequestBody ReplyPatchRequestBody body,
            Authentication authentication
    ) {
        var updateReply = replyService.updateReply(postId, replyId, body, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(updateReply);
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable Long postId,
            @PathVariable Long replyId,
            Authentication authentication
    ){
        replyService.deleteReply(postId, replyId, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.noContent().build();
    }
}
