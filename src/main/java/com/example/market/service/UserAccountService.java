package com.example.market.service;

import com.example.market.domain.UserAccount;
import com.example.market.dto.UserAccountDto;
import com.example.market.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder encoder;

    public void saveUser(UserAccountDto dto) {
        idCheck(dto.userId());
        userAccountRepository.save(dto.toEntity(encoder));
    }

    public UserAccountDto kakaoSaveUser(UserAccountDto dto){
        return UserAccountDto.from(userAccountRepository.save(dto.toEntity(encoder)));
    }

    @Transactional(readOnly = true)
    public Optional<UserAccountDto> searchUser(String userId) {
        return userAccountRepository.findById(userId)
                .map(UserAccountDto::from);
    }

    /**   아이디 중복 체크    */
    public String idCheck(String userId) {
        Optional<UserAccount> checkMember = userAccountRepository.findByUserId(userId);
        if (checkMember.isPresent()){
            return "사용 불가능한 아이디 입니다.";
        }
        return "ok";
    }

    // 회원 전체 조회 - ADMIN
    @Transactional(readOnly = true)
    public List<UserAccountDto> memberFindAll(){
        return userAccountRepository.findAll().stream().map(UserAccountDto::from).toList();
    }

}
