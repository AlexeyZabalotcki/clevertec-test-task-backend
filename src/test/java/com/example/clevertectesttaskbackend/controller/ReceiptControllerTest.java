package com.example.clevertectesttaskbackend.controller;

import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.example.clevertectesttaskbackend.dto.ReceiptDto;
import com.example.clevertectesttaskbackend.service.ReceiptService;
import com.example.clevertectesttaskbackend.tesabuilder.ProductDtoTestBuilder;
import com.example.clevertectesttaskbackend.tesabuilder.ReceiptDtoTestBuilder;
import com.example.clevertectesttaskbackend.tesabuilder.TestBuilder;
import com.itextpdf.text.DocumentException;
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
    private ReceiptService receiptService;

    @InjectMocks
    private ReceiptController receiptController;

    private ReceiptDto expected;

    private static TestBuilder<ReceiptDto> receiptDtoBuilder;
    private static TestBuilder<ProductDto> productDtoBuilder;

    @BeforeEach
    void setUp() {
        productDtoBuilder = new ProductDtoTestBuilder(1L, "Product 1", BigDecimal.valueOf(123), true);
        ProductDto productDto = productDtoBuilder.build();
        List<ProductDto> products = new ArrayList<>(Arrays.asList(productDto));
        byte[] byteArray = {1, 2, 3};
        receiptDtoBuilder = new ReceiptDtoTestBuilder(products, byteArray, BigDecimal.valueOf(123));
        expected = receiptDtoBuilder.build();
    }

    @Test
    void checkGetByIdShouldReturnReceiptPdf() {
        Long id = 1L;

        when(receiptService.getReceipt(anyLong())).thenReturn(expected);

        ResponseEntity<ByteArrayResource> actual = receiptController.getById(id);

        verify(receiptService).getReceipt(id);

        assertEquals(expected.getReceipt(), actual.getBody().getByteArray());
    }

    @Test
    void checkGetByIdShouldReturnStatusOk() {
        Long id = 1L;

        when(receiptService.getReceipt(anyLong())).thenReturn(expected);

        ResponseEntity<ByteArrayResource> actual = receiptController.getById(id);

        verify(receiptService).getReceipt(id);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void checkAddReceiptShouldReturnByteArrayWithArgumentCapture() throws DocumentException {
        expected.setId(null);

        byte[] receipt = expected.getReceipt();

        ArgumentCaptor<ReceiptDto> argumentCaptor = ArgumentCaptor.forClass(ReceiptDto.class);

        when(receiptService.addReceipt(argumentCaptor.capture())).thenReturn(expected);

        ResponseEntity<ReceiptDto> actual = receiptController.addReceipt(expected);

        assertEquals(receipt, actual.getBody().getReceipt());

        verify(receiptService, times(1)).addReceipt(argumentCaptor.getValue());
    }

    @Test
    void checkAddReceiptShouldReturnByteArray() throws DocumentException {
        expected.setId(null);

        byte[] receipt = expected.getReceipt();

        when(receiptService.addReceipt(Mockito.any())).thenReturn(expected);

        ResponseEntity<ReceiptDto> actual = receiptController.addReceipt(expected);

        assertEquals(receipt, actual.getBody().getReceipt());

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
