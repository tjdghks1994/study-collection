package com.fastcampus.board.model.post;

import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.user.User;

import java.time.ZonedDateTime;

public record Post(
        Long postId,
        String body,
        User user,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime
) {
    public static Post from(PostEntity entity) {
        return new Post(
                entity.getPostId(),
                entity.getBody(),
                User.from(entity.getUser()),
                entity.getCreatedDateTime(),
                entity.getUpdatedDateTime()
        );
    }

}


