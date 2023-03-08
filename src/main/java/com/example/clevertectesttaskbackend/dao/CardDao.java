package com.example.clevertectesttaskbackend.dao;

import com.example.clevertectesttaskbackend.entity.DiscountCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardDao extends CrudRepository<DiscountCard, Long> {
    List<DiscountCard> findAll();

    Optional<DiscountCard> findById(Long id);
}
