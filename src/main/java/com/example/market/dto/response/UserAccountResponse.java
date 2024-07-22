package com.example.market.dto.response;

import com.example.market.domain.type.RoleType;
import com.example.market.dto.UserAccountDto;

public record UserAccountResponse(
        String userId,
        String email,
        String userName,
        String phone,
        String zipCode,
        String addr,
        String addrDetail,
        RoleType roleTypes,
        String provider
){
    private static UserAccountResponse of(String userId, String email, String userName, String phone, String zipCode, String addr, String addrDetail) {
        return UserAccountResponse.of(userId, email, userName, phone, zipCode, addr, addrDetail, null, null);
    }

    private static UserAccountResponse of(String userId, String email, String userName, String phone, String zipCode, String addr, String addrDetail, RoleType roleTypes, String provider) {
        return new UserAccountResponse(userId, email, userName, phone, zipCode, addr, addrDetail, roleTypes, provider);
    }

    public static UserAccountResponse from(UserAccountDto dto) {
        return new UserAccountResponse(
                dto.userId(),
                dto.email(),
                dto.userName(),
                dto.phone(),
                dto.zipCode(),
                dto.addr(),
                dto.addrDetail(),
                dto.roleTypes(),
                dto.provider()
        );
    }
}
