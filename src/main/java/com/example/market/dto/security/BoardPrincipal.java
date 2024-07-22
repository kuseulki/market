package com.example.market.dto.security;

import com.example.market.domain.type.RoleType;
import com.example.market.dto.UserAccountDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public record BoardPrincipal(
        String userId,
        String password,
        String email,
        String userName,
        Collection<? extends GrantedAuthority> authorities,     // 권한
        Map<String, Object> oAuth2Attributes,                   // sns
        String provider

// 추가 - OAuth2User
) implements UserDetails, OAuth2User {

    public static BoardPrincipal of(String userId, String password, String email, String userName, RoleType roleTypes) {
        return BoardPrincipal.of(userId, password, email, userName, roleTypes, Map.of(),null);
    }

    public static BoardPrincipal of(String userId, String password, String email, String userName, RoleType roleTypes, String provider) {
        return BoardPrincipal.of(userId, password, email, userName, roleTypes, Map.of(), provider);
    }

    // 추가
    public static BoardPrincipal of(String userId, String password, String email, String userName, RoleType roleTypes, Map<String, Object> oAuth2Attributes, String provider) {

        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(roleTypes.getDescription()));

        return new BoardPrincipal(
                userId,
                password,
                email,
                userName,
                authorities,
                oAuth2Attributes,
                provider

        );
    }

    public static BoardPrincipal from(UserAccountDto dto) {
        return BoardPrincipal.of(
                dto.userId(),
                dto.userPassword(),
                dto.email(),
                dto.userName(),
                dto.roleTypes(),
                dto.provider()
        );
    }

    public UserAccountDto toDto() {

        RoleType roleType = RoleType.USER;

        return UserAccountDto.of(
                userId,
                password,
                email,
                userName,
                roleType,
                provider

        );
    }

    // spring security
    @Override public String getUsername() { return userId; }
    @Override public String getPassword() { return password; }

    // 권한
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // oauth2
    @Override public Map<String, Object> getAttributes() { return oAuth2Attributes; }

    // 유저 식별 정보 - google, naver, kakao
    @Override public String getName() { return userId; }

}
