package ru.rybinskov.gb.springshop.shop.service;


import ru.rybinskov.gb.springshop.shop.domain.Bucket;
import ru.rybinskov.gb.springshop.shop.domain.User;
import ru.rybinskov.gb.springshop.shop.dto.BucketDto;

import java.util.List;

public interface BucketService {
    Bucket createBucket(User user, List<Long> productIds);

    void addProducts(Bucket bucket, List<Long> productIds);

    void deleteFromUserBucket(Long productId, String username);

    BucketDto getBucketByUser(String name);

}
