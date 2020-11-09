package ru.rybinskov.gb.springshop.shop.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.rybinskov.gb.springshop.shop.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
