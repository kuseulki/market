package com.example.market.service;

import com.example.market.domain.Cart;
import com.example.market.domain.CartItem;
import com.example.market.domain.Item;
import com.example.market.domain.UserAccount;
import com.example.market.dto.CartDetailDto;
import com.example.market.dto.CartItemDto;
import com.example.market.repository.CartItemRepository;
import com.example.market.repository.CartRepository;
import com.example.market.repository.ItemRepository;
import com.example.market.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CartService {
    
    private final UserAccountRepository userAccountRepository;
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    
    // 장바구니 - 저장
    public Long addCart(CartItemDto dto, String userId) {
        UserAccount userAccount = userAccountRepository.findById(userId) .orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));
        Item item = itemRepository.findById(dto.itemId()).orElseThrow(() -> new IllegalArgumentException("Invalid itemId: " + dto.itemId()));

        // 사용자의 장바구니를 찾거나, 없으면 새로 생성
        Cart cart = cartRepository.findByUserAccount_UserId(userId).orElseGet(() -> cartRepository.save(Cart.of(userAccount)));

        Optional<CartItem> savedCartItemOptional  = cartItemRepository.findByCartIdAndItemId(cart.getId(), dto.itemId());

        if(savedCartItemOptional.isPresent()){

            CartItem savedCartItem = savedCartItemOptional.get();
            savedCartItem.addCount(dto.count());
            cartItemRepository.save(savedCartItem);
            return savedCartItem.getId();

        } else {
            CartItem cartItem = CartItem.of(cart, item, dto.count());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    // 장바구니에 들어있는 상품 조회
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String userId){

        UserAccount userAccount = userAccountRepository.findById(userId) .orElseThrow(() -> new IllegalArgumentException("Invalid userId: " + userId));
        Cart cart = cartRepository.findByUserAccount_UserId(userId).orElseThrow(() ->  new IllegalArgumentException("Invalid userId: " + userId));

//        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        return cartItems.stream()
                .map(CartDetailDto::from)
                .collect(Collectors.toList());
    }

    // 장바구니 상품 수량 업데이트
    public void updateCartItemCount(Long cartItemId, Integer count){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }

    // 장바구니 상품 삭제
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }


}
