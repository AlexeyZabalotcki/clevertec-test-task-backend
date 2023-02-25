package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.example.clevertectesttaskbackend.dto.ReceiptDto;
import com.example.clevertectesttaskbackend.exception.NoSuchReceiptException;
import com.example.clevertectesttaskbackend.model.Product;
import com.example.clevertectesttaskbackend.model.Receipt;
import com.example.clevertectesttaskbackend.repository.ReceiptRepository;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

    @InjectMocks
    private ReceiptService receiptService;

    @Mock
    private ReceiptRepository receiptRepository;

    private ReceiptDto expectedReceiptDto;
    private Receipt expectedReceipt;

    @BeforeEach
    void setUpDto() {
        ProductDto p1 = ProductDto.builder()
                .id(1L)
                .title("Product 1")
                .price(BigDecimal.valueOf(100))
                .discount(true)
                .build();
        ProductDto p2 = ProductDto.builder()
                .id(2L)
                .title("Product 2")
                .price(BigDecimal.valueOf(100))
                .discount(false)
                .build();
        ProductDto p3 = ProductDto.builder()
                .id(3L)
                .title("Product 3")
                .price(BigDecimal.valueOf(100))
                .discount(true)
                .build();
        ProductDto p4 = ProductDto.builder()
                .id(4L)
                .title("Product 4")
                .price(BigDecimal.valueOf(100))
                .discount(false)
                .build();
        List<ProductDto> products = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));
        byte[] byteArray = {1, 2, 3};
        expectedReceiptDto = ReceiptDto.builder()
                .products(products)
                .receipt(byteArray)
                .totalPrice(BigDecimal.valueOf(380))
                .build();
    }

    @BeforeEach
    void setUpEntity() {
        Product p1 = Product.builder()
                .id(1L)
                .title("Product 1")
                .price(BigDecimal.valueOf(100))
                .discount(true)
                .build();
        Product p2 = Product.builder()
                .id(2L)
                .title("Product 2")
                .price(BigDecimal.valueOf(100))
                .discount(false)
                .build();
        Product p3 = Product.builder()
                .id(3L)
                .title("Product 3")
                .price(BigDecimal.valueOf(100))
                .discount(true)
                .build();
        Product p4 = Product.builder()
                .id(4L)
                .title("Product 4")
                .price(BigDecimal.valueOf(100))
                .discount(false)
                .build();
        List<Product> products = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));
        byte[] byteArray = {1, 2, 3};
        expectedReceipt = Receipt.builder()
                .id(1L)
                .products(products)
                .receipt(byteArray)
                .totalPrice(BigDecimal.valueOf(380))
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void checkGetReceiptShouldReturnReceipt() {
        when(receiptRepository.findById(1L)).thenReturn(Optional.ofNullable(expectedReceipt));

        ReceiptDto actualReceipt = receiptService.getReceipt(1L);

        assertEquals(expectedReceiptDto, actualReceipt);
    }

    @Test
    void checkGetReceiptShouldTrowNoSuchReceiptException() {
        assertThrows(NoSuchReceiptException.class, () -> {
            receiptService.getReceipt(1330L);
        });
    }

    @Test
    void checkAddReceiptShouldAddAndReturnReceipt() throws DocumentException {
        when(receiptRepository.save(any(Receipt.class))).thenReturn(expectedReceipt);

        ReceiptDto actualReceipt = receiptService.addReceipt(expectedReceiptDto);

        assertEquals(expectedReceiptDto, actualReceipt);

    }
}
