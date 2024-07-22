package com.example.market.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ItemSearchType {

    ITEMNAME("상품명"),
    HASHTAG("해시태그");

    @Getter private final String description;

}
