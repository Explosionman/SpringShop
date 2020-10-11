package ru.rybinskov.gb.springshop.shop.service;

import org.springframework.stereotype.Service;
import ru.rybinskov.gb.springshop.shop.dao.BucketDao;
import ru.rybinskov.gb.springshop.shop.dao.ProductDao;
import ru.rybinskov.gb.springshop.shop.domain.Bucket;
import ru.rybinskov.gb.springshop.shop.domain.Product;
import ru.rybinskov.gb.springshop.shop.domain.User;
import ru.rybinskov.gb.springshop.shop.dto.BucketDetailDto;
import ru.rybinskov.gb.springshop.shop.dto.BucketDto;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BucketServiceImpl implements BucketService {

    private final BucketDao bucketRepository;
    private final ProductDao productRepository;
    private final UserService userService;

    public BucketServiceImpl(BucketDao bucketRepository, ProductDao productRepository, UserService userService) {
        this.bucketRepository = bucketRepository;
        this.productRepository = productRepository;
        this.userService = userService;
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
    public void deleteFromUserBucket(Long productId, String username) {
        BucketDto bucket = getBucketByUser(username);
        List<BucketDetailDto> detailDtoList = bucket.getBucketDetails();
        Map<Long, BucketDetailDto> mapByProductId = new HashMap<>();
        for (BucketDetailDto bucketDetailDto : detailDtoList) {
            Product product = productRepository.getOne(productId);
            if (bucketDetailDto.getProductId().equals(product.getId())) {
                bucketDetailDto.setAmount(bucketDetailDto.getAmount() - 1.0);
                bucketDetailDto.setSum(bucketDetailDto.getSum() - product.getPrice());
            }
            mapByProductId.put(bucketDetailDto.getProductId(), bucketDetailDto);
        }
        bucket.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucket.aggregate();
        //всё минусуется, но не могу сохранить данные, чтобы они обновились после редиректа....
        //Подскажите плз, как?
        //bucketRepository.save()....................
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
}


