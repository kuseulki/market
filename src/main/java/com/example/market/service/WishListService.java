package com.example.market.service;

import com.example.market.domain.Item;
import com.example.market.domain.UserAccount;
import com.example.market.domain.WishList;
import com.example.market.domain.WishListItem;
import com.example.market.dto.WishListDetailDto;
import com.example.market.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class WishListService {

    private final UserAccountRepository userAccountRepository;
    private final ItemRepository itemRepository;
    private final WishListRepository wishListRepository;
    private final WishListItemRepository wishListItemRepository;
    private final CartItemRepository cartItemRepository;

    // 위시리스트 들어있는 상품 조회
    @Transactional(readOnly = true)
    public List<WishListDetailDto> getWishList(String userId) {

        UserAccount userAccount = userAccountRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));
        WishList wishList = wishListRepository.findByUserAccount_UserId(userId).orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));

        List<WishListItem> wishListItems = wishListItemRepository.findByWishListId(wishList.getId());
        return wishListItems.stream()
                .map(WishListDetailDto::from)
                .collect(Collectors.toList());
    }

    // 위시리스트 - 저장
    public Long addWishList(WishListDetailDto dto, String userId) {

        UserAccount userAccount = userAccountRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));
        WishList wishList = wishListRepository.findByUserAccount_UserId(userId).orElseGet(() -> {
            WishList newWishList = WishList.of(userAccount);
            wishListRepository.save(newWishList);
            return newWishList;
        });

        Item item = itemRepository.findById(dto.itemId()).orElseThrow(() -> new IllegalArgumentException("Invalid itemId: " + dto.itemId()));
        WishListItem wishListItem = WishListItem.of(wishList, item, dto.count());
        wishListItemRepository.save(wishListItem);
        return wishListItem.getId();
    }

    public void deleteWishList(Long wishListItemId) {
        wishListItemRepository.deleteById(wishListItemId);
    }
}
