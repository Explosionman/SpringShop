package ru.rybinskov.gb.springshop.shop.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rybinskov.gb.springshop.shop.dao.ProductDao;
import ru.rybinskov.gb.springshop.shop.domain.Bucket;
import ru.rybinskov.gb.springshop.shop.domain.Product;
import ru.rybinskov.gb.springshop.shop.domain.User;
import ru.rybinskov.gb.springshop.shop.dto.BucketDetailDto;
import ru.rybinskov.gb.springshop.shop.dto.BucketDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl {

    private final ProductDao productJpaDAO;
    private final UserService userService;
    private final BucketService bucketService;
    private final SimpMessagingTemplate template;

    public ProductServiceImpl(ProductDao productJpaDAO, UserService userService, BucketService bucketService, SimpMessagingTemplate template) {
        this.productJpaDAO = productJpaDAO;
        this.userService = userService;
        this.bucketService = bucketService;
        this.template = template;
    }

    @Transactional
    public void save(Product product) {
        productJpaDAO.save(product);
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productJpaDAO.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(Long id) {
        productJpaDAO.deleteById(id);
    }

    @Transactional
    public List<Product> getAll() {
        List<Product> products = productJpaDAO.findAll();
        products.sort(Comparator.comparingLong(Product::getId));
        return products;
    }

    @Transactional
    public List<Product> getAllFilteredByMinPrice() {
        List<Product> products = productJpaDAO.findAll();
        products.sort(Comparator.comparingDouble(Product::getPrice));
        return products;
    }

    @Transactional
    public List<Product> getAllFilteredByMaxPrice() {
        List<Product> products = productJpaDAO.findAll();
        Comparator<Product> productComparator = Comparator.comparing(Product::getPrice);
        Collections.sort(products, productComparator.reversed());
        return products;
    }

    @Transactional
    public List<Product> getPriceByRange(Long start, Long end) {
        List<Product> products = productJpaDAO.findAll();
        return products.stream()
                .filter(product -> product.getPrice() >= start && product.getPrice() <= end)
                .sorted(Comparator.comparingDouble(Product::getPrice))
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(Product product) {
        productJpaDAO.save(product);
    }

    @Transactional
    public void addToUserBucket(Long productId, String username) {
        User user = userService.findByName(username);
        if (user == null) {
            throw new RuntimeException("User not found. " + username);
        }

        Bucket bucket = user.getBucket();
        if (bucket == null) {
            Bucket newBucket = bucketService.createBucket(user, Collections.singletonList(productId));
            user.setBucket(newBucket);
            userService.save(user);
        } else {
            bucketService.addProducts(bucket, Collections.singletonList(productId));
        }
        template.convertAndSend("/topic/products", bucketService.getBucketByUser(username));
    }
}
