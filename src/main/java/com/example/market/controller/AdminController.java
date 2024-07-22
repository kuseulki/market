package com.example.market.controller;

import com.example.market.dto.response.ItemResponse;
import com.example.market.dto.response.UserAccountResponse;
import com.example.market.service.ItemService;
import com.example.market.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ItemService itemService;
    private final UserAccountService userAccountService;

    // 관리자 페이지
    @GetMapping("/admin/page")
    public String adminPage(){
        return "admin/adminPage";
    }


    // 전체 상품 관리
    @GetMapping("/admin/item/all")
    public String itemAll(Model model) {
        List<ItemResponse> itemAll = itemService.itemFindAll().stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());

        model.addAttribute("itemAll", itemAll);
        return "admin/itemAllList";
    }

    // 전체 회원 관리
    @GetMapping("/admin/user/all")
    public String userAll(Model model){

        List<UserAccountResponse> userAll =  userAccountService.memberFindAll().stream()
                .map(UserAccountResponse::from)
                .collect(Collectors.toList());

        model.addAttribute("userAll", userAll);
        return "admin/memberAllList";
    }


}