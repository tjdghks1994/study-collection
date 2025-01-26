package com.fastcampus.board.model.entity;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "follow",
        indexes = {
            @Index(name = "follow_follower_following_idx", columnList = "follower, following", unique = true)
        }
)
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @Column
    private ZonedDateTime createdDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower")
    private UserEntity follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following")
    private UserEntity following;

    public static FollowEntity of(UserEntity follower, UserEntity following) {
        var follow = new FollowEntity();
        follow.setFollower(follower);
        follow.setFollowing(following);
        return follow;
    }

    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followId) {
        this.followId = followId;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public UserEntity getFollower() {
        return follower;
    }

    public void setFollower(UserEntity follower) {
        this.follower = follower;
    }

    public UserEntity getFollowing() {
        return following;
    }

    public void setFollowing(UserEntity following) {
        this.following = following;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowEntity that)) return false;
        return Objects.equals(getFollowId(), that.getFollowId()) &&
                Objects.equals(getCreatedDateTime(), that.getCreatedDateTime()) &&
                Objects.equals(getFollower(), that.getFollower()) &&
                Objects.equals(getFollowing(), that.getFollowing());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFollowId(), getCreatedDateTime(),
                getFollower(), getFollowing());
    }

    @PrePersist
    private void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
    }
}
