package ru.rybinskov.gb.springshop.shop.DAO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rybinskov.gb.springshop.shop.dao.UserDao;
import ru.rybinskov.gb.springshop.shop.domain.Role;
import ru.rybinskov.gb.springshop.shop.domain.User;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@SqlGroup({@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:initUsers.sql")})
class UserDaoTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserDao userRepository;

    @Test
    void checkFindByName() {
        //have
        User user = new User();
        user.setName("SuperAdmin");
        user.setPassword("123321");
        user.setEmail("super-email@mail.ru");

        entityManager.persist(user);

        //execute
        User actualUser = userRepository.findFirstByName("SuperAdmin");

        //check
        assertNotNull(actualUser);
        Assertions.assertEquals(user.getName(), actualUser.getName());
        Assertions.assertEquals(user.getPassword(), actualUser.getPassword());
        Assertions.assertEquals(user.getEmail(), actualUser.getEmail());

    }

    @Test
    void checkFindByNameAfterSql() {
        //execute
        User actualUser = userRepository.findFirstByName("testUser");

        //check
        assertNotNull(actualUser);
        Assertions.assertEquals(99, actualUser.getId());
        Assertions.assertEquals("testUser", actualUser.getName());
        Assertions.assertEquals("testUser", actualUser.getPassword());
        Assertions.assertEquals("test@test.ru", actualUser.getEmail());
        Assertions.assertEquals(Role.ROLE_ADMIN, actualUser.getRole());

    }
}