package com.example.market.dto.request;

import com.example.market.domain.type.RoleType;
import com.example.market.dto.UserAccountDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserAccountRequest(
        @NotBlank(message = "아이디는 필수 입력값입니다.")
        String userId,
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
        String userPassword,
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "이름은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
        String userName,
        String phone,
        String zipCode,
        String addr,
        @NotBlank(message = "상세주소를 입력해 주세요")
        String addrDetail,
        RoleType roleTypes,
        String provider

){
    public static UserAccountRequest of() {
        return new UserAccountRequest(null, null, null, null, null, null, null, null, null, null);
    }
    public static UserAccountRequest of(String userId, String userPassword) {
        return new UserAccountRequest(userId, userPassword, null, null, null, null, null, null, null, null);
    }


    public static UserAccountRequest of(String userId, String userPassword, String email, String userName, String phone, String zipCode, String addr, String addrDetail, RoleType roleTypes, String provider) {
        return new UserAccountRequest(userId, userPassword, email, userName, phone, zipCode, addr, addrDetail, roleTypes, provider);
    }

    public UserAccountDto toDto() {
        return UserAccountDto.of(
                userId,
                userPassword,
                email,
                userName,
                phone,
                zipCode,
                addr,
                addrDetail,
                roleTypes,
                provider
        );
    }
}