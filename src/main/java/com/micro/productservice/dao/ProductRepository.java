package com.micro.productservice.dao;

import com.micro.productservice.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Override
    <S extends Product> S save(S entity);


    @Query("SELECT p FROM Product p WHERE p.productId = :productId")
    Optional<Product> findByProductId(@Param("productId") Integer integer);
}
