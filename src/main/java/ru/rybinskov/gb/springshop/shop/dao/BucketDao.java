package ru.rybinskov.gb.springshop.shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rybinskov.gb.springshop.shop.domain.Bucket;


public interface BucketDao extends JpaRepository<Bucket, Long> {
    void deleteById(Long id);
}
