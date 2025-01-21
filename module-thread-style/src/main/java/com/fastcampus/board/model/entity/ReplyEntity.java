package com.fastcampus.board.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "reply",
        indexes = {
                @Index(name = "reply_userid_idx", columnList = "userid"),
                @Index(name = "reply_postid_idx", columnList = "postid")
        }
)
@SQLDelete(sql = "UPDATE \"reply\" SET deleteddatetime = CURRENT_TIMESTAMP WHERE replyid = ?")
@SQLRestriction("deleteddatetime IS NULL")
public class ReplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;
    @Column(columnDefinition = "TEXT")
    private String body;
    @Column
    private ZonedDateTime createdDateTime;
    @Column
    private ZonedDateTime updatedDateTime;
    @Column
    private ZonedDateTime deletedDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postid")
    private PostEntity post;

    public static ReplyEntity of(String body, UserEntity user, PostEntity post) {
        var reply = new ReplyEntity();
        reply.setBody(body);
        reply.setUser(user);
        reply.setPost(post);

        return reply;
    }

    @PrePersist
    private void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
        this.updatedDateTime = this.createdDateTime;
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedDateTime = ZonedDateTime.now();

    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReplyEntity that)) return false;
        return Objects.equals(getReplyId(), that.getReplyId()) &&
                Objects.equals(getBody(), that.getBody()) &&
                Objects.equals(getCreatedDateTime(), that.getCreatedDateTime()) &&
                Objects.equals(getUpdatedDateTime(), that.getUpdatedDateTime()) &&
                Objects.equals(getDeletedDateTime(), that.getDeletedDateTime()) &&
                Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getPost(), that.getPost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReplyId(), getBody(), getCreatedDateTime(),
                getUpdatedDateTime(), getDeletedDateTime(), getUser(), getPost());
    }
}
