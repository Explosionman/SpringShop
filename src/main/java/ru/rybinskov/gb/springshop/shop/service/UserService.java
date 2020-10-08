package ru.rybinskov.gb.springshop.shop.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.rybinskov.gb.springshop.shop.dto.UserDto;

public interface UserService extends UserDetailsService {
    boolean save(UserDto userDto);
}
