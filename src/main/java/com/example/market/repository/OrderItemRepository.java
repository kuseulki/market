package com.example.market.repository;

import com.example.market.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>  {

    List<OrderItem> findByOrderId(Long orderId);
}
