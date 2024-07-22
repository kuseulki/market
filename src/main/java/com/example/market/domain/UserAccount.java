package com.example.market.domain;

import com.example.market.domain.type.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "userId", unique = true),
        @Index(columnList = "email", unique = true),
})
@Entity
public class UserAccount{

//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id @Column(length = 50) private String userId;                            // 아이디

    @Setter @Column(nullable = false) private String userPassword;             // 비밀번호
    @Setter @Column private String email;                        // 이메일
    @Setter @Column private String userName;                     // 이름
    @Setter @Column private String phone;                        // 휴대폰
    @Setter @Column private String zipCode;                      // 우편번호
    @Setter @Column private String addr;                         // 주소1
    @Setter @Column private String addrDetail;

//    @Column(nullable = false) private Set<RoleType> roleTypes = new LinkedHashSet<>();

    @Setter @Column  @Enumerated(EnumType.STRING) private RoleType roleTypes;

    @Setter @Column private String provider; // SNS

    protected UserAccount() {}

    private UserAccount(String userId, String userPassword, String email, String userName, String phone, String zipCode, String addr, String addrDetail, RoleType roleTypes) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.userName = userName;
        this.zipCode = zipCode;
        this.phone = phone;
        this.addr = addr;
        this.addrDetail = addrDetail;
        this.roleTypes = roleTypes;
    }


    // 회원가입 최소, 인증이 없을때
    public static UserAccount of(String userId, String userPassword, String email, String userName, String phone, String zipCode, String addr, String addrDetail, RoleType roleTypes) {
        return new UserAccount(userId, userPassword, email, userName, phone, zipCode, addr, addrDetail, roleTypes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return userId != null && userId.equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

}
