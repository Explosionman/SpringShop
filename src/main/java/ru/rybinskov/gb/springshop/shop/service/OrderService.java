package ru.rybinskov.gb.springshop.shop.service;


import ru.rybinskov.gb.springshop.shop.domain.Order;

public interface OrderService {
    void saveOrder(Order order);
    Order getOrder(Long id);
}
