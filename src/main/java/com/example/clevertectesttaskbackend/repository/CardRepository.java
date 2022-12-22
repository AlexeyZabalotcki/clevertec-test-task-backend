package com.example.clevertectesttaskbackend.repository;

import com.example.clevertectesttaskbackend.model.DiscountCard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends CrudRepository<DiscountCard, Long> {
    List<DiscountCard> findAll();

    DiscountCard findByNumber(Integer number);
}
