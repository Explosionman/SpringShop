package ru.rybinskov.gb.springshop.shop.service;

import org.springframework.stereotype.Service;
import ru.rybinskov.gb.springshop.shop.dao.OrderRepository;
import ru.rybinskov.gb.springshop.shop.domain.Order;


import javax.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public void saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}
