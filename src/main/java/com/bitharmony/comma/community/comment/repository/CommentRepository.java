package com.bitharmony.comma.community.comment.repository;

import com.bitharmony.comma.community.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArticleId(long id);
}
