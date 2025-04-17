package com.smart1.appsmartweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart1.appsmartweb.model.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long>{
    
}
