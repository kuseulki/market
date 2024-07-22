package com.example.market.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentStatus {

    OK("완료"),
    READY("준비"),
    CANCEL("취소");

    @Getter
    private final String description;
}
