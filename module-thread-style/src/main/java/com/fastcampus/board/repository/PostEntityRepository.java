package com.fastcampus.board.repository;

import com.fastcampus.board.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {
}
