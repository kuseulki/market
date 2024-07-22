package com.example.market.controller;

import com.example.market.dto.request.CartItemRequest;
import com.example.market.dto.response.CartDetailResponse;
import com.example.market.dto.security.BoardPrincipal;
import com.example.market.service.CartService;
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

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/cart")
@Controller
public class CartController {

    private final CartService cartService;

    @PostMapping
    public @ResponseBody ResponseEntity<Long> order(@RequestBody @Validated CartItemRequest cartItemRequest, BindingResult bindingResult, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String userId = boardPrincipal.getUsername();
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartItemRequest.toDto(), userId);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cartItemId, HttpStatus.OK);
    }

    // 장바구니 페이지
    @GetMapping("/list")
    public String getCart(@AuthenticationPrincipal BoardPrincipal boardPrincipal, Model model){

        List<CartDetailResponse> cart = cartService.getCartList(boardPrincipal.getUsername())
                .stream()
                .map(CartDetailResponse::from)
                .collect(Collectors.toList());
        model.addAttribute("cartItems", cart);

        return "cart/list";
    }

    // 장바구니 수량 업데이트
    @PatchMapping("/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, Integer count,
                                                       @AuthenticationPrincipal BoardPrincipal boardPrincipal){

        if(count <= 0) {
            return new ResponseEntity<String>("최소 1개 이상 담아주세요.", HttpStatus.BAD_REQUEST);
        }
        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    // 장바구니 삭제
    @DeleteMapping("/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }


}
