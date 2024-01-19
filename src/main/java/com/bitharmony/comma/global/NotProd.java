package com.bitharmony.comma.global;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class NotProd {
//    private final ArticleService articleService;
//    private final MemberService memberService;
//    private final CommentService commentService;

    @Bean
    public ApplicationRunner initNotProd() {
//        int userCount = 10;
//        for (int i = 0; i < userCount; i++) {
//            MemberCreateForm memberCreateForm = new MemberCreateForm();
//            memberCreateForm.setUsername("user" + (i + 1));
//            memberCreateForm.setPassword("1234");
//            Member member = memberService.create(memberCreateForm);
//            if (i % 2 == 0) {
//                memberService.setMembership(member, true);
//            }
//        }
//        for (int i = 0; i < 100; i++) {
//            ArticleForm articleForm = new ArticleForm();
//            articleForm.setTitle("title" + (i + 1));
//            articleForm.setBody("body" + (i + 1));
//            articleForm.setWriter("user" + (i % userCount + 1));
//            articleForm.setPaid(true);
//            articleService.createArticle(articleForm);
//        }
//        for (int i = 0; i < 10; i++) {
//            CommentForm commentForm = new CommentForm();
//            commentForm.setBody("test comment");
//            commentService.createComment(commentForm,articleService.getArticleById(i+1),"user1");
//        }
//
        return args -> {
        };
    }
}
