package com.example.market.controller;

import com.example.market.dto.WishListDetailDto;
import com.example.market.dto.response.WishListDetailResponse;
import com.example.market.dto.security.BoardPrincipal;
import com.example.market.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;

    @GetMapping("/wishlist/list")
    public String wishListHist(@AuthenticationPrincipal BoardPrincipal boardPrincipal, Model model){

        List<WishListDetailResponse> wishListDetailDtoList = wishListService.getWishList(boardPrincipal.getUsername())
                .stream()
                .map(WishListDetailResponse::from)
                .collect(Collectors.toList());
        model.addAttribute("wishItems", wishListDetailDtoList);
        return "wishList/list";
    }

    @PostMapping("/wishlist")
    public @ResponseBody ResponseEntity<Long> wishList(@RequestBody @Validated WishListDetailDto wishListDetailDto, BindingResult bindingResult, Principal principal){

        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String userId = principal.getName();
        Long wishItemId;
        try {
            wishItemId = wishListService.addWishList(wishListDetailDto, userId);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(wishItemId, HttpStatus.OK);
    }


    @PostMapping("/wishlist/{wishListItemId}")
    public String deleteWishListItem(@PathVariable("wishListItemId") Long wishListItemId){
        wishListService.deleteWishList(wishListItemId);
        return "redirect:/wishlist/list";
    }
}