package ru.rybinskov.gb.springshop.shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rybinskov.gb.springshop.shop.domain.User;


public interface UserDao extends JpaRepository<User, Long> {
    User findFirstByName(String name);
}
