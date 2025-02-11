package com.lec.spring.domains.post.repository;

import com.lec.spring.domains.post.entity.PostComment;
import com.lec.spring.domains.post.repository.dsl.QPostCommentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long>, QPostCommentRepository {
}
