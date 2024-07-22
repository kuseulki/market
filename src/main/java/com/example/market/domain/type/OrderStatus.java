package com.example.market.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    ORDER("주문"),
    CANCEL("취소");

    @Getter
    private final String description;
}
