package com.example.clevertectesttaskbackend.controller;

import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.example.clevertectesttaskbackend.dto.ReceiptDto;
import com.example.clevertectesttaskbackend.service.ReceiptService;
import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiptControllerTest {

    @Mock
    ReceiptService receiptService;

    @InjectMocks
    ReceiptController receiptController;

    ReceiptDto expected;

    @BeforeEach
    void setUp() {
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
        expected = ReceiptDto.builder()
                .products(products)
                .receipt(byteArray)
                .totalPrice(BigDecimal.valueOf(380))
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void checkGetByIdShouldReturnReceiptPdf() {
        Long id = 1L;

        when(receiptService.getReceipt(anyLong())).thenReturn(expected);

        ResponseEntity<ByteArrayResource> actual = receiptController.getById(id);

        verify(receiptService).getReceipt(id);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected.getReceipt(), actual.getBody().getByteArray());
    }

    @Test
    void checkAddReceiptShouldReturnByteArrayWithArgumentCapture() throws DocumentException {
        expected.setId(null);

        List<ProductDto> productDtos = new ArrayList<>(expected.getProducts());
        byte[] receipt = expected.getReceipt();
        BigDecimal totalPrice = expected.getTotalPrice();

        ArgumentCaptor<ReceiptDto> argumentCaptor = ArgumentCaptor.forClass(ReceiptDto.class);

        when(receiptService.addReceipt(argumentCaptor.capture())).thenReturn(expected);

        ResponseEntity<ReceiptDto> actual = receiptController.addReceipt(expected);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(productDtos, actual.getBody().getProducts());
        assertEquals(receipt, actual.getBody().getReceipt());
        assertEquals(totalPrice, actual.getBody().getTotalPrice());

        verify(receiptService, times(1)).addReceipt(argumentCaptor.getValue());
    }

    @Test
    void checkAddReceiptShouldReturnByteArray() throws DocumentException {
        expected.setId(null);

        List<ProductDto> productDtos = new ArrayList<>(expected.getProducts());
        byte[] receipt = expected.getReceipt();
        BigDecimal totalPrice = expected.getTotalPrice();

        when(receiptService.addReceipt(Mockito.any())).thenReturn(expected);

        ResponseEntity<ReceiptDto> actual = receiptController.addReceipt(expected);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(productDtos, actual.getBody().getProducts());
        assertEquals(receipt, actual.getBody().getReceipt());
        assertEquals(totalPrice, actual.getBody().getTotalPrice());

        verify(receiptService, times(1)).addReceipt(expected);
    }

    @Test
    void checkAddReceiptShouldReturnException() throws DocumentException {
        expected.setReceipt(null);
        doThrow(new DocumentException()).when(receiptService).addReceipt(expected);

        ResponseEntity actual = receiptController.addReceipt(expected);

        verify(receiptService, times(1)).addReceipt(expected);

        ResponseEntity expected = new ResponseEntity("Not found receipt", HttpStatus.NOT_ACCEPTABLE);

        assertEquals(expected, actual);
    }
}
