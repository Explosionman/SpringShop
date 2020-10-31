package ru.rybinskov.gb.springshop.shop.bootstrap;


import org.springframework.boot.CommandLineRunner;
import ru.rybinskov.gb.springshop.shop.dao.ProductDao;
import ru.rybinskov.gb.springshop.shop.dao.UserDao;
import ru.rybinskov.gb.springshop.shop.domain.Product;
import ru.rybinskov.gb.springshop.shop.domain.Role;
import ru.rybinskov.gb.springshop.shop.domain.User;


//@Component
public class DataInit implements CommandLineRunner {

    public static final User USER = new User();
    public static final User MANAGER = new User();
    public static final User ADMIN = new User();

    public final ProductDao productJpaDAO;
    public final UserDao userDao;

    static {
        USER.setName("User");
        USER.setPassword("123");
        USER.setRole(Role.USER);

        MANAGER.setName("Manager");
        MANAGER.setPassword("123");
        MANAGER.setRole(Role.MANAGER);

        ADMIN.setName("Admin");
        ADMIN.setPassword("admin");
        ADMIN.setRole(Role.ADMIN);
    }

    public DataInit(ProductDao productJpaDAO, UserDao userDao) {
        this.productJpaDAO = productJpaDAO;
        this.userDao = userDao;
    }


    public static void init(ProductDao productJpaDAO, UserDao userDao) {
        productJpaDAO.save(new Product("сыр", 550.0));
        productJpaDAO.save(new Product("лук", 10.0));
        productJpaDAO.save(new Product("чеснок", 10.0));
        productJpaDAO.save(new Product("колбаса", 800.0));
        productJpaDAO.save(new Product("хлеб", 35.0));
        productJpaDAO.save(new Product("молоко", 45.0));
        productJpaDAO.save(new Product("мороженое", 40.0));
        productJpaDAO.save(new Product("ветчина", 700.0));
        productJpaDAO.save(new Product("свинина", 400.0));
        productJpaDAO.save(new Product("говядина", 500.0));
        productJpaDAO.save(new Product("макароны", 60.0));
        productJpaDAO.save(new Product("ячка", 50.0));
        productJpaDAO.save(new Product("рис", 60.0));
        productJpaDAO.save(new Product("яйца", 80.0));
        productJpaDAO.save(new Product("булка сдобная", 20.0));
        productJpaDAO.save(new Product("круассан", 50.0));
        productJpaDAO.save(new Product("бублики", 70.0));
        productJpaDAO.save(new Product("баранки", 80.0));
        productJpaDAO.save(new Product("котлеты", 80.0));
        productJpaDAO.save(new Product("товар", 80.0));
        productJpaDAO.save(new Product("товар1", 80.0));
        productJpaDAO.save(new Product("товар2", 80.0));
        productJpaDAO.save(new Product("товар3", 80.0));
        productJpaDAO.save(new Product("товар4", 80.0));
        productJpaDAO.save(new Product("товар5", 80.0));
        productJpaDAO.save(new Product("товар6", 80.0));
        productJpaDAO.save(new Product("товар7", 80.0));
        productJpaDAO.save(new Product("товар8", 80.0));
        productJpaDAO.save(new Product("товар9", 80.0));
        productJpaDAO.save(new Product("товар10", 80.0));
        productJpaDAO.save(new Product("товар11", 80.0));
        productJpaDAO.save(new Product("товар12", 80.0));
        productJpaDAO.save(new Product("товар13", 80.0));
        productJpaDAO.save(new Product("товар14", 80.0));

        userDao.save(USER);
        userDao.save(MANAGER);
        userDao.save(ADMIN);

    }

    @Override
    public void run(String... args) throws Exception {
        init(productJpaDAO, userDao);
    }
}
