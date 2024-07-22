package com.example.market.repository;

import com.example.market.domain.Item;
import com.example.market.domain.QItem;
import com.example.market.repository.querydsl.ItemRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface ItemRepository extends
        JpaRepository<Item, Long>,
        ItemRepositoryCustom,
        QuerydslPredicateExecutor<Item>,
        QuerydslBinderCustomizer<QItem>

{

    Page<Item> findByHashtag(String hashtag, Pageable pageable);

    Page<Item> findByItemNameContaining(String itemName, Pageable pageable);

    void deleteByIdAndUserAccount_UserId(Long itemId, String user);

    @Override
    default void customize(QuerydslBindings bindings, QItem root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.itemName, root.createdAt, root.createdBy);
        bindings.bind(root.itemName).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }

}
