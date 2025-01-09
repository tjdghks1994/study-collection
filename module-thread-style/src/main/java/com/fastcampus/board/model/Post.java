package com.fastcampus.board.model;

import com.fastcampus.board.model.entity.PostEntity;

import java.time.ZonedDateTime;

public record Post(
        Long postId,
        String body,
        ZonedDateTime createdDateTime,
        ZonedDateTime updatedDateTime
) {
    public static Post from(PostEntity entity) {
        return new Post(
                entity.getPostId(),
                entity.getBody(),
                entity.getCreatedDateTime(),
                entity.getUpdatedDateTime()
        );
    }
}


