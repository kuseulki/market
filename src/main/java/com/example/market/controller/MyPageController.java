package com.example.market.controller;

import com.example.market.dto.ArticleDto;
import com.example.market.dto.security.BoardPrincipal;
import com.example.market.service.ArticleService;
import com.example.market.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/member")
@Controller
public class MyPageController {

    private final UserAccountService userAccountService;
    private final ArticleService articleService;

    // 내가 작성글 보기
    @GetMapping("/mypage/article")
    public String index(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        Model model, @AuthenticationPrincipal BoardPrincipal boardPrincipal
    ) {

        Page<ArticleDto> articles = articleService.findArticlesByUserId(boardPrincipal.getUsername(), pageable);

        model.addAttribute("articles", articles);
        return "myPage/myArticle";
    }
}
