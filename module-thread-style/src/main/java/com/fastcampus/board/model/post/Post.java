package com.fastcampus.board.model.post;

import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.user.User;

import java.time.ZonedDateTime;

public record Post(
        Long postId,
        String body,
        Long repliesCount,
        Long likesCount,
        User user,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime,
        Boolean isLiking
) {
    public static Post from(PostEntity entity) {
        return new Post(
                entity.getPostId(),
                entity.getBody(),
                entity.getRepliesCount(),
                entity.getLikesCount(),
                User.from(entity.getUser()),
                entity.getCreatedDateTime(),
                entity.getUpdatedDateTime(),
                null
        );
    }

    public static Post from(PostEntity entity, boolean isLiking) {
        return new Post(
                entity.getPostId(),
                entity.getBody(),
                entity.getRepliesCount(),
                entity.getLikesCount(),
                User.from(entity.getUser()),
                entity.getCreatedDateTime(),
                entity.getUpdatedDateTime(),
                isLiking
        );
    }

}


