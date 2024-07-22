package com.example.market.repository.querydsl;

import java.util.List;

public interface ItemRepositoryCustom {
    List<String> findAllDistinctHashtags();
}
