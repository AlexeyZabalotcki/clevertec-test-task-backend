package com.example.clevertectesttaskbackend.repository;

import com.example.clevertectesttaskbackend.model.DiscountCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<DiscountCard, Long> {
    List<DiscountCard> findAll();
}
