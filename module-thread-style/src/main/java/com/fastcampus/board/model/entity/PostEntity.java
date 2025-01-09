package com.fastcampus.board.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "post")
@SQLDelete(sql = "UPDATE \"post\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE postid = ?")
@SQLRestriction("deleteddatetime IS NULL")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @Column(columnDefinition = "TEXT")
    private String body;
    @Column
    private ZonedDateTime createdDateTime;
    @Column
    private ZonedDateTime updatedDateTime;
    @Column
    private ZonedDateTime deletedDateTime;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public ZonedDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(ZonedDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public ZonedDateTime getDeletedDateTime() {
        return deletedDateTime;
    }

    public void setDeletedDateTime(ZonedDateTime deletedDateTime) {
        this.deletedDateTime = deletedDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostEntity that)) return false;
        return Objects.equals(getPostId(), that.getPostId())
                && Objects.equals(getBody(), that.getBody())
                && Objects.equals(getCreatedDateTime(), that.getCreatedDateTime())
                && Objects.equals(getUpdatedDateTime(), that.getUpdatedDateTime())
                && Objects.equals(getDeletedDateTime(), that.getDeletedDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPostId(), getBody(), getCreatedDateTime(), getUpdatedDateTime(), getDeletedDateTime());
    }

    @PrePersist
    private void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
        this.updatedDateTime = ZonedDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedDateTime = ZonedDateTime.now();
    }
}
