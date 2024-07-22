package com.example.market.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ItemSellStatus {

    SELL("판매중"),
    SOLD_OUT("품절");

    @Getter private final String description;

}
