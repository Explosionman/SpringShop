package ru.rybinskov.gb.springshop.shop.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.rybinskov.gb.springshop.shop.domain.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User getById(Long id);
    User auth(String name, String password);
    List<User> getAll();
    User findByName(String name);
    void save(User user);
}
