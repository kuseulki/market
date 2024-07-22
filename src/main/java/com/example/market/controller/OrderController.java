package com.example.market.controller;

import com.example.market.dto.CartOrderDto;
import com.example.market.dto.OrderDto;
import com.example.market.dto.OrderHistDto;
import com.example.market.dto.security.BoardPrincipal;
import com.example.market.service.OrderService;
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

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping(value = "/cart/orders")
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal) {

        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
        if (cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
            return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
        }
        for (CartOrderDto cartOrder : cartOrderDtoList) {
            if (!orderService.validateCartItem(cartOrder.getCartItemId(), principal.getName())) {
                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }
        Long orderId = orderService.orderCartItem(cartOrderDtoList, principal.getName());
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @PostMapping("/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Validated OrderDto orderDto, BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String userId = principal.getName();
        Long orderId;

        try {
            orderId = orderService.order(orderDto, userId);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = "/order/list")
    public String orderHist(@AuthenticationPrincipal BoardPrincipal boardPrincipal,
                            @RequestParam(name = "orderId", required = false) String orderId, Model model) {

        List<OrderHistDto> ordersHistDtoList = orderService.getOrderList(boardPrincipal.getName());
        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("orderId", orderId);
        return "order/orderHist";
    }
}



//    @PostMapping("/order")
//    public @ResponseBody ResponseEntity order(@RequestBody OrderItemRequest orderItemRequest, @AuthenticationPrincipal BoardPrincipal boardPrincipal){
//
//
//        String userId = boardPrincipal.getUsername();
//        Long orderId;
//
//        try {
//            orderId = orderService.order(orderItemRequest.toDto(), userId);
//        } catch (Exception e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(orderId, HttpStatus.OK);
//    }
//
//    // 주문 목록
//    @GetMapping(value = {"/order/list"})
//    public String orderHist(@AuthenticationPrincipal BoardPrincipal boardPrincipal, Model model){
//
//        List<OrderItemResponse> order = orderService.getOrderList(boardPrincipal.userName())
//                .stream()
//                .map(OrderItemResponse::from)
//                .collect(Collectors.toList());
//
//        model.addAttribute("orders", order);
//
//        return "order/list";
//    }















