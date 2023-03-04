package com.example.clevertectesttaskbackend.repository;

import com.example.clevertectesttaskbackend.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

}
