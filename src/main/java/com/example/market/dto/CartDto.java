package com.example.market.dto;

import com.example.market.domain.UserAccount;

public record CartDto(
        Long id,
        UserAccount userAccount
) {


}
