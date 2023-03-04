package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.example.clevertectesttaskbackend.dto.ReceiptDto;
import com.example.clevertectesttaskbackend.exception.NoSuchReceiptException;
import com.example.clevertectesttaskbackend.model.Product;
import com.example.clevertectesttaskbackend.model.Receipt;
import com.example.clevertectesttaskbackend.repository.ReceiptRepository;
import com.example.clevertectesttaskbackend.tesabuilder.*;
import com.itextpdf.text.DocumentException;
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
        TestBuilder<ProductDto> productDtoBuilder =
                new ProductDtoTestBuilder(1L, "Product 1", BigDecimal.valueOf(123), true);
        TestBuilder<ProductDto> productDtoBuilder2 =
                new ProductDtoTestBuilder(2L, "Product 2", BigDecimal.valueOf(123), true);
        ProductDto productDto1 = productDtoBuilder.build();
        ProductDto productDto2 = productDtoBuilder2.build();
        List<ProductDto> products = new ArrayList<>(Arrays.asList(productDto1, productDto2));
        byte[] byteArray = {1, 2, 3};
        TestBuilder<ReceiptDto> receiptDtoBuilder = new ReceiptDtoTestBuilder(products, byteArray, BigDecimal.valueOf(123));
        expectedReceiptDto = receiptDtoBuilder.build();
    }

    @BeforeEach
    void setUpEntity() {
        TestBuilder<Product> productBuilder1 = new ProductTestBuilder(1L, "Product 1", BigDecimal.valueOf(123), true);
        TestBuilder<Product> productBuilder2 = new ProductTestBuilder(2L, "Product 2", BigDecimal.valueOf(123), true);
        Product product1 = productBuilder1.build();
        Product product2 = productBuilder2.build();
        List<Product> products = new ArrayList<>(Arrays.asList(product1, product2));
        byte[] byteArray = {1, 2, 3};
        TestBuilder<Receipt> receiptBuilder = new ReceiptTestBuilder(products, byteArray, BigDecimal.valueOf(123));
        expectedReceipt = receiptBuilder.build();
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
