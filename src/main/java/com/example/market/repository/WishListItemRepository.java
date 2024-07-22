package com.example.market.repository;

import com.example.market.domain.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishListItemRepository extends JpaRepository<WishListItem, Long> {

    List<WishListItem> findByWishListId(Long withListId);

}
