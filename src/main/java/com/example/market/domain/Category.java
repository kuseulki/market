package com.example.market.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Entity
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String name;

    @Setter @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    public List<Item> items = new ArrayList<>();

    protected Category(){}

    private Category( String name){
        this.name = name;
    }

    public static Category of(String name){
        return new Category(name);
    }


    public void addItem(Item item) {
        items.add(item);
        item.setCategory(this);
    }

    public void removeItem(Item item) {
        items.remove(item);
        item.setCategory(null);
    }

    public void changeName(String name) {
        this.name = name;
    }
}
