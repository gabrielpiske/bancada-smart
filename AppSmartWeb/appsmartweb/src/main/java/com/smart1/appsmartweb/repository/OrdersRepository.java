package com.smart1.appsmartweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smart1.appsmartweb.model.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findFirstByProductionOrder(Long productionOrder);

    @Query("SELECT COALESCE(MAX(o.productionOrder), 0) FROM Orders o")
    Long findMaxProductionOrder();
}
