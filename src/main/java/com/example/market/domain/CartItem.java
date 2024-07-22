package com.example.market.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Entity
public class CartItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @Setter @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @Setter @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Setter private Integer count;      // 같은 상품 몇개 담을지

    protected CartItem(){}

    private CartItem(Cart cart, Item item, Integer count) {
        this.cart = cart;
        this.item = item;
        this.count = count;
    }

    public static CartItem of(Cart cart, Item item, Integer count) {
        return new CartItem(cart, item, count);
    }

    // 장바구니에 기존에 담겨 있는 상품인데, 해당 상품을 추가로 장바구니에 담을 때 기존 수량에 현재 담을 수량을 더해줄 떄 사용
    public void addCount(int count){
        this.count += count;
    }

    // 장바구니에 담겨있는 수량 변경
    public void updateCount(int count){
        this.count = count;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem that)) return false;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
