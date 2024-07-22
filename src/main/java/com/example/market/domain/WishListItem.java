package com.example.market.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@ToString(callSuper = true)
@Getter
@Entity
public class WishListItem extends AuditingFields {

    @Id @Column(name = "wish_list_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="wish_list_id")
    private WishList wishList;

    @Setter @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Setter private int count;

    protected WishListItem(){}
    private WishListItem(WishList wishList, Item item, int count){
        this.wishList = wishList;
        this.item = item;
        this.count = count;
    }

    public static WishListItem of(WishList wishList, Item item, int count){
        return new WishListItem(wishList, item, count);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WishListItem that)) return false;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
