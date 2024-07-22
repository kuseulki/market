package com.example.market.config;

import com.example.market.domain.type.RoleType;
import com.example.market.dto.UserAccountDto;
import com.example.market.dto.security.BoardPrincipal;
import com.example.market.dto.security.KakaoOAuth2Response;
import com.example.market.repository.UserAccountRepository;
import com.example.market.service.UserAccountService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/",
                                "/login",
                                "/join",
                                "/articles",
                                "/articles/search-hashtag",
                                "/item/**",
                                "/member/**",
                                "/admin/**",
                                "/cart/**",
                                "/wishlist/**"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/join",
                                "/item/**",
                                "/member/**",
                                "/cart/**",
                                "/wishlist/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/member/login")         // get
                        .loginProcessingUrl("/login")       // post
                        .defaultSuccessUrl("/", true)             // 로그인 성공 시 리다이렉트
                        .failureUrl("/member/login/error")
                        .usernameParameter("userId")
                        .passwordParameter("userPassword")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .oauth2Login(oAuth -> oAuth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                )
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        return username -> userAccountRepository
                .findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService(
            UserAccountService userAccountService,
            PasswordEncoder passwordEncoder
    ) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

        return userRequest -> {
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
            String registrationId = userRequest.getClientRegistration().getRegistrationId();        // kakao
            String providerId = String.valueOf(kakaoResponse.id());
            String userId = registrationId + "_" + providerId;
            String dummyPassword = passwordEncoder.encode("{bcrypt}" + UUID.randomUUID());
            RoleType roleTypes = RoleType.SNS;
            String provider = userRequest.getClientRegistration().getRegistrationId();

            return userAccountService.searchUser(userId)
                    .map(BoardPrincipal::from)
                    .orElseGet(() ->
                            BoardPrincipal.from(
                                    userAccountService.kakaoSaveUser(
                                            UserAccountDto.of(
                                                    userId,
                                                    dummyPassword,
                                                    kakaoResponse.email(),
                                                    kakaoResponse.userName(),
                                                    roleTypes,
                                                    provider
                                            )

                                    )
                            )
                    );
        };

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
