package com.example.market.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table
@Getter
@Setter
@Entity
public class ItemImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_image_id")
    private Long id;

    @Column private String originalFileName;

    @Column private String storedFileName;

    @Column private String savePath;                // 이미지 조회 경로

    @Column private Boolean imageYn;                // 대표 이미지 여부


    @ManyToOne @JoinColumn(name = "item_id")
    private Item item;

    protected ItemImage(){}

    public ItemImage(String originalFileName, String storedFileName, String savePath, Item item) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.savePath = savePath;
        this.item = item;
    }

    public static ItemImage of(String originalFileName, String storedFileName, String savePath, Item item){
        return new ItemImage(originalFileName, storedFileName, savePath, item);
    }
}
