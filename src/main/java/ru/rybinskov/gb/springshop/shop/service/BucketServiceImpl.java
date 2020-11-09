package ru.rybinskov.gb.springshop.shop.service;

import org.springframework.stereotype.Service;
import ru.rybinskov.gb.springshop.shop.aspect.MeasureMethod;
import ru.rybinskov.gb.springshop.shop.dao.BucketDao;
import ru.rybinskov.gb.springshop.shop.dao.ProductDao;
import ru.rybinskov.gb.springshop.shop.domain.*;
import ru.rybinskov.gb.springshop.shop.dto.BucketDetailDto;
import ru.rybinskov.gb.springshop.shop.dto.BucketDto;
import ru.rybinskov.gb.springshop.shop.mapper.BucketMapper;


import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BucketServiceImpl implements BucketService {

    private final BucketMapper mapper = BucketMapper.MAPPER;
    private final BucketDao bucketRepository;
    private final ProductDao productRepository;
    private final UserService userService;
    private final OrderService orderService;

    public BucketServiceImpl(BucketDao bucketRepository, ProductDao productRepository, UserService userService, OrderService orderService) {
        this.bucketRepository = bucketRepository;
        this.productRepository = productRepository;
        this.userService = userService;
        this.orderService = orderService;
    }

    @Override
    @Transactional
    public Bucket createBucket(User user, List<Long> productIds) {
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        List<Product> productList = getCollectRefProductsByIds(productIds);
        bucket.setProducts(productList);
        return bucketRepository.save(bucket);
    }

    private List<Product> getCollectRefProductsByIds(List<Long> productIds) {
        return productIds.stream()
                .map(productRepository::getOne)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addProducts(Bucket bucket, List<Long> productIds) {
        List<Product> products = bucket.getProducts();
        List<Product> newProductsList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductsList.addAll(getCollectRefProductsByIds(productIds));
        bucket.setProducts(newProductsList);
        bucketRepository.save(bucket);
    }

    @Override
    @Transactional
    @MeasureMethod
    public void deleteFromUserBucket(Long productId, String username) {
        User user = userService.findByName(username);
        Bucket bucket = user.getBucket();
        List<Product> products = bucket.getProducts();
        List<Product> newProductsList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductsList.remove(productRepository.getOne(productId));
        bucket.setProducts(newProductsList);
        bucketRepository.save(bucket);
    }

    @Override
    @Transactional
    @MeasureMethod
    public void addToUserBucket(Long productId, String username) {
        User user = userService.findByName(username);
        addProducts(user.getBucket(), Collections.singletonList(productId));
    }

    @Override
    @Transactional
    @MeasureMethod
    public void deleteAllFromUserBucket(Long productId, String username) {
        User user = userService.findByName(username);
        Bucket bucket = user.getBucket();
        List<Product> products = bucket.getProducts();
        List<Product> productIds = new ArrayList<>();
        for (Product prod : products) {
            if (prod.getId().equals(productId)) {
                productIds.add(prod);
            }
        }
        List<Product> newProductsList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductsList.removeAll(productIds);
        bucket.setProducts(newProductsList);
        bucketRepository.save(bucket);
    }


    @Override
    public BucketDto getBucketByUser(String name) {
        User user = userService.findByName(name);
        if (user == null || user.getBucket() == null) {
            return new BucketDto();
        }

        BucketDto bucketDto = new BucketDto();
        Map<Long, BucketDetailDto> mapByProductId = new HashMap<>();

        List<Product> products = user.getBucket().getProducts();
        for (Product product : products) {
            BucketDetailDto detail = mapByProductId.get(product.getId());
            if (detail == null) {
                mapByProductId.put(product.getId(), new BucketDetailDto(product));
            } else {
                detail.setAmount(detail.getAmount() + 1.0);
                detail.setSum(detail.getSum() + product.getPrice());
            }
        }

        bucketDto.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDto.aggregate();

        return bucketDto;
    }

    @Transactional
    public void commitBucketToOrder(String username){
        User user = userService.findByName(username);
        if(user == null){
            throw new RuntimeException("User is not found");
        }
        Bucket bucket = user.getBucket();
        if(bucket == null || bucket.getProducts().isEmpty()){
            return;
        }

        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        order.setUser(user);

        Map<Product, Long> productWithAmount = bucket.getProducts().stream()
                .collect(Collectors.groupingBy(product -> product, Collectors.counting()));

        List<OrderDetails> orderDetails = productWithAmount.entrySet().stream()
                .map(pair -> new OrderDetails(order, pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());

        BigDecimal total = new BigDecimal(orderDetails.stream()
                .map(detail -> detail.getPrice().multiply(detail.getAmount()))
                .mapToDouble(BigDecimal::doubleValue).sum());

        order.setDetails(orderDetails);
        order.setSum(total);
        order.setAddress("none");

        orderService.saveOrder(order);
        bucket.getProducts().clear();
        bucketRepository.save(bucket);

    }
}


