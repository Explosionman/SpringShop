package ru.rybinskov.gb.springshop.shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rybinskov.gb.springshop.shop.domain.Product;

public interface ProductDao extends JpaRepository<Product, Long> {

}
