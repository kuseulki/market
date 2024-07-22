package com.example.market.dto;

import com.example.market.domain.UserAccount;
import com.example.market.domain.type.RoleType;
import org.springframework.security.crypto.password.PasswordEncoder;


public record UserAccountDto(
//        Long id,
        String userId,
        String userPassword,
        String email,
        String userName,
        String phone,
        String zipCode,
        String addr,
        String addrDetail,

        RoleType roleTypes,
        String provider
) {

    public static UserAccountDto of(String userId, String userPassword,  String email, String userName){
        return UserAccountDto.of(userId, userPassword, email, userName,null, null, null, null, null, null);
    }

    public static UserAccountDto of(String userId, String userPassword,  String email, String userName, RoleType roleTypes, String provider){
        return UserAccountDto.of(userId, userPassword, email, userName,null, null, null, null, roleTypes, provider);
    }

    public static UserAccountDto of(String userId, String userPassword, String email, String userName, String phone, String zipCode, String addr, String addrDetail, RoleType roleTypes, String provider) {
        return new UserAccountDto(userId, userPassword, email, userName, phone, zipCode, addr, addrDetail, roleTypes, provider);
    }

    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
//                entity.getId(),
                entity.getUserId(),
                entity.getUserPassword(),
                entity.getEmail(),
                entity.getUserName(),
                entity.getPhone(),
                entity.getZipCode(),
                entity.getAddr(),
                entity.getAddrDetail(),
                entity.getRoleTypes(),
                entity.getProvider()
        );
    }

    public UserAccount toEntity(PasswordEncoder encoder) {
        return UserAccount.of(
                userId,
                encoder.encode(userPassword),
                email,
                userName,
                phone,
                zipCode,
                addr,
                addrDetail,
//                roleTypes.
                RoleType.ADMIN
        );
    }

}