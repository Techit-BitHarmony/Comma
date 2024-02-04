package com.bitharmony.comma.community.comment.controller;

import com.bitharmony.comma.community.artitcle.entity.Article;
import com.bitharmony.comma.community.artitcle.service.ArticleService;
import com.bitharmony.comma.community.comment.dto.*;
import com.bitharmony.comma.community.comment.entity.Comment;
import com.bitharmony.comma.community.comment.service.CommentService;
import com.bitharmony.comma.global.exception.NotAuthorizedException;
import com.bitharmony.comma.global.response.GlobalResponse;
import com.bitharmony.comma.member.entity.Member;
import com.bitharmony.comma.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community/comments")
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;

    @GetMapping("/{articleId}")
    public GlobalResponse<CommentGetListResponse> getCommentListByArticleId(@PathVariable long articleId){

        List<Comment> comments = commentService.getCommentsByArticleId(articleId);

        return GlobalResponse.of(
                "200",
                CommentGetListResponse.builder()
                        .commentList(comments.stream().map(CommentDto::new).toList())
                        .build()
        );
    }

    @GetMapping("/{articleId}/{commentId}")
    public GlobalResponse<CommentGetResponse> getComment(@PathVariable long commentId){

        Comment comment = commentService.getCommentById(commentId);

        return GlobalResponse.of(
                "200",
                CommentGetResponse.builder()
                        .content(comment.getContent())
                        .build()
        );
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public GlobalResponse<CommentCreateResponse> addComment(
            @RequestBody @Valid CommentCreateRequest request,
            Principal principal
    ){
        Member member = memberService.getMemberByUsername(principal.getName());
        Comment comment = commentService.createComment(member, request.articleId(), request.content());

        return GlobalResponse.of(
                "200",
                CommentCreateResponse.builder()
                        .articleId(comment.getArticle().getId())
                        .build()
        );
    }

    @DeleteMapping("/{commentId}")
    public GlobalResponse<Void> deleteComment(
            @PathVariable long commentId,
            Principal principal) {
        Member member = memberService.getMemberByUsername(principal.getName());
        Comment comment = commentService.getCommentById(commentId);

        if(!commentService.canDeleteOrModify(member, comment)) {
            throw new NotAuthorizedException();
        }

        commentService.deleteComment(comment);

        return GlobalResponse.of(
                "204"
        );
    }


    @PutMapping("/{commentId}")
    public GlobalResponse<CommentModifyResponse> modifyComment(
            @PathVariable long commentId,
            @RequestBody @Valid CommentModifyRequest request,
            Principal principal
    ) {
        Member member = memberService.getMemberByUsername(principal.getName());
        Comment comment = commentService.getCommentById(commentId);

        if(!commentService.canDeleteOrModify(member, comment)) {
            throw new NotAuthorizedException();
        }

        Comment _comment = commentService.modifyComment(comment, request.content());

        return GlobalResponse.of(
                "200",
                CommentModifyResponse.builder()
                        .articleId(_comment.getArticle().getId())
                        .build()
        );

    }

}
