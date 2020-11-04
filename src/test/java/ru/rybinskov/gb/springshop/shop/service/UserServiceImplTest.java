package ru.rybinskov.gb.springshop.shop.service;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.rybinskov.gb.springshop.shop.dao.UserDao;
import ru.rybinskov.gb.springshop.shop.domain.User;

import java.util.UUID;

class UserServiceImplTest {

    private UserServiceImpl userService;
    private UserDao userRepository;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserDao.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void checkFindByName() {
        //have
        String name = "Alex";
        User expectedUser = User.builder().id(1L).name(name).build();

        Mockito.when(userRepository.findFirstByName(Mockito.anyString())).thenReturn(expectedUser);

        //execute
        User actualUser = userService.findByName(name);

        //check
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser, actualUser);

    }

    @Test
    void checkFindByNameExact() {
        //have
        String name = "Alex";
        User expectedUser = User.builder().id(1L).name(name).build();

        Mockito.when(userRepository.findFirstByName(Mockito.eq(name))).thenReturn(expectedUser);

        //execute
        User actualUser = userService.findByName(name);
        User rndUser = userService.findByName(UUID.randomUUID().toString());

        //check
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser, actualUser);

        Assertions.assertNull(rndUser);

    }
}