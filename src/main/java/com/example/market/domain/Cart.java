package com.example.market.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne @JoinColumn(name="userAccount_id")
    private UserAccount userAccount;

    protected Cart(){}

    private Cart(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public static Cart of(UserAccount userAccount){
        return new Cart(userAccount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart that)) return false;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
