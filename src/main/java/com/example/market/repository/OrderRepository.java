package com.example.market.repository;

import com.example.market.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserAccount_UserIdOrderByOrderDateDesc(String userId);

    Long countByUserAccount_UserId(String userId);

    @Query("select o from Order o" +
            " left join fetch o.payment p" +
            " left join fetch o.userAccount u" +
            " where o.id = :id")
    Optional<Order> findOrderAndPaymentAndUserAccount(@Param("id") Long id);


    @Query("select o from Order o" +
            " left join fetch o.payment p" +
            " where o.id = :id")
    Optional<Order> findOrderAndPayment(@Param("id") Long id);

}

