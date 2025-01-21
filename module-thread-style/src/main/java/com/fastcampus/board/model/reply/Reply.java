package com.fastcampus.board.model.reply;

import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.entity.ReplyEntity;
import com.fastcampus.board.model.post.Post;
import com.fastcampus.board.model.user.User;

import java.time.ZonedDateTime;

public record Reply(
        Long replyId,
        String body,
        User user,
        Post post,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime
) {
    public static Reply from(ReplyEntity entity) {
        return new Reply(
                entity.getReplyId(),
                entity.getBody(),
                User.from(entity.getUser()),
                Post.from(entity.getPost()),
                entity.getCreatedDateTime(),
                entity.getUpdatedDateTime()
        );
    }

}


