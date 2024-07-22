package com.example.market.domain;

import com.example.market.domain.type.ItemSellStatus;
import com.example.market.handler.OutOfStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@ToString(callSuper = true)
@Entity
public class Item extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Setter @ManyToOne(optional = false) @JoinColumn(name = "userId")
    private UserAccount userAccount; // 유저 정보 (ID)

    @Setter @Column(nullable = false, length = 50)
    private String itemName;                //상품명

    @Setter @Column(name = "price", nullable = false)
    private Integer price;                      //가격

    @Setter @Column(nullable = false)
    private Integer stockNumber;                //재고수량

    @Setter @Lob @Column(nullable = false)
    private String itemDetail;              //상품 상세 설명

    @Setter private String hashtag;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    @ToString.Exclude
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ItemImage> itemimages = new ArrayList<>();

    @Setter @ManyToOne @JoinColumn(name = "category_id")
    private Category category;

    public void addStock(Integer stockNumber){
        this.stockNumber += stockNumber;
    }

    public void removeStock(Integer stockNumber) {
        int restStock = this.stockNumber - stockNumber;
        if (stockNumber < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }


    protected Item(){}

    private Item(UserAccount userAccount, String itemName, Integer price, Integer stockNumber, String itemDetail, String hashtag) {
        this.userAccount = userAccount;
        this.itemName = itemName;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemDetail = itemDetail;
        this.hashtag = hashtag;
    }

    public static Item of(UserAccount userAccount, String itemName, Integer price, Integer stockNumber, String itemDetail, String hashtag) {
        return new Item(userAccount, itemName, price, stockNumber, itemDetail, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return id != null && id.equals(item.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}