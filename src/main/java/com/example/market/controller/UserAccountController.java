package com.example.market.controller;

import com.example.market.dto.request.UserAccountRequest;
import com.example.market.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;


    // 회원가입
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("userAccountRequest", UserAccountRequest.of());
        return "member/join";
    }

    @PostMapping("/join")
    public String join(@Validated UserAccountRequest userAccountRequest, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()){
            return "member/join";
        }
        try{
            userAccountService.saveUser(userAccountRequest.toDto());
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/join";
        }
        return "redirect:/";
    }

    /**   아이디 중복 체크   */
    @PostMapping("/member/id-check")
    public @ResponseBody String idCheck(@RequestParam("userId") String userId){
        return userAccountService.idCheck(userId);
    }

    /**   로그인   */
    @GetMapping("/member/login")
    public String loginForm(Model model){
        return "member/login";
    }

    @GetMapping("/member/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/login";
    }


}
