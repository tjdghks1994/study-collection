package com.fastcampus.board.repository;

import com.fastcampus.board.model.entity.PostEntity;
import com.fastcampus.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findByUser(UserEntity user);
}
