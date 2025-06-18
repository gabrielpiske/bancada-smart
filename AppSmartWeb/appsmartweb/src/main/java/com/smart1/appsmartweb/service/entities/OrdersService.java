package com.smart1.appsmartweb.service.entities;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart1.appsmartweb.model.Orders;
import com.smart1.appsmartweb.repository.OrdersRepository;

@Service
public class OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    public Orders saveOrders(Orders orders){
        return ordersRepository.save(orders);
    }

    public List<Orders> lisOrders(){
        return ordersRepository.findAll();
    }

    public Orders findById(Long id){
        Optional<Orders> orders = ordersRepository.findById(id);
        return orders.orElseThrow(() -> new RuntimeException("Order Não Encontrada"));
    }

    public void deleteById(Long id){
        ordersRepository.deleteById(id);
    }
}
