package com.bitharmony.comma.community.comment.service;

import com.bitharmony.comma.community.artitcle.service.ArticleService;
import com.bitharmony.comma.community.comment.entity.Comment;
import com.bitharmony.comma.community.comment.repository.CommentRepository;
import com.bitharmony.comma.global.exception.community.CommentNotFoundException;
import com.bitharmony.comma.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleService articleService;

    public List<Comment> getCommentsByArticleId(long id) {
        // 글이 존재하는지 체크, 없다면 ArticleNotFoundException 발생
        articleService.getArticleById(id);

        return commentRepository.findByArticleId(id);
    }

    public Comment getCommentById(long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);

        if (comment.isEmpty()) {
            throw new CommentNotFoundException();
        }

        return comment.get();
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    public boolean canDeleteOrModify(Member member, Comment comment) {
        return comment.getCommenter().getUsername().equals(member.getUsername());
    }

    public Comment modifyComment(Comment comment, String content) {
        Comment _comment = comment.toBuilder()
                .content(content)
                .modifyDate(LocalDateTime.now())
                .build();

        commentRepository.save(_comment);

        return _comment;
    }

    public Comment createComment(Member member, long articleId, String content) {
        Comment comment = Comment.builder()
                .commenter(member)
                .article(articleService.getArticleById(articleId))
                .content(content).build();

        commentRepository.save(comment);

        return comment;
    }

}
