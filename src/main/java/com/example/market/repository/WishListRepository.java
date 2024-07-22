package com.example.market.repository;

import com.example.market.domain.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishListRepository  extends JpaRepository<WishList, Long> {

    Optional<WishList> findByUserAccount_UserId(String userId);
}
