package com.micro.productservice.dao;

import com.micro.productservice.entity.ReservedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservedItemRepository extends JpaRepository<ReservedItem, Integer> {


    Optional<List<ReservedItem>> findAllBySagaId(String sagaId);
}
