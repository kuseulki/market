package com.example.market.repository.querydsl;

import com.example.market.domain.QItem;
import com.example.market.domain.Item;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ItemRepositoryCustomImpl extends QuerydslRepositorySupport implements ItemRepositoryCustom {

    public ItemRepositoryCustomImpl() { super(Item.class); }

    @Override
    public List<String> findAllDistinctHashtags() {
        QItem item = QItem.item;

        return from(item)
                .distinct()
                .select(item.hashtag)
                .where(item.hashtag.isNotNull())
                .fetch();
    }
}
