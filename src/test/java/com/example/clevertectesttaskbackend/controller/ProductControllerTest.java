package com.example.clevertectesttaskbackend.controller;

import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.example.clevertectesttaskbackend.exception.NoSuchProductException;
import com.example.clevertectesttaskbackend.service.ProductService;
import com.example.clevertectesttaskbackend.tesabuilder.ProductDtoTestBuilder;
import com.example.clevertectesttaskbackend.tesabuilder.TestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private static ProductDto expectedProduct;

    @BeforeEach
    void setUp() {
        TestBuilder<ProductDto> builder = new ProductDtoTestBuilder(1L, "Product 1", BigDecimal.valueOf(123), true);
        expectedProduct = builder.build();
    }

    private static Stream<Arguments> productProvider() {
        return Stream.of(
                Arguments.of(expectedProduct)
        );
    }

    @Test
    void checkFindAllShouldReturnAllProducts() {
        List<ProductDto> expectedProducts = Arrays.asList(expectedProduct);

        when(productService.getAll()).thenReturn(expectedProducts);

        List<ProductDto> actual = productController.findAll();

        assertEquals(expectedProducts, actual);

        verify(productService, times(1)).getAll();
    }

    @Test
    void checkAddShouldReturnStatusOk() {
        expectedProduct.setId(null);

        when(productService.addProduct(Mockito.any())).thenReturn(expectedProduct);

        ResponseEntity<ProductDto> actual = productController.add(expectedProduct);

        assertEquals(HttpStatus.OK, actual.getStatusCode());

        verify(productService, times(1)).addProduct(expectedProduct);
    }

    @Test
    void checkAddShouldReturnTrueIfProductIdNull() {
        expectedProduct.setId(null);

        when(productService.addProduct(Mockito.any())).thenReturn(expectedProduct);

        ResponseEntity<ProductDto> actual = productController.add(expectedProduct);

        assertNull(actual.getBody().getId());

        verify(productService, times(1)).addProduct(expectedProduct);
    }

    @Test
    void checkAddShouldReturnTrueIfProductNotNull() {
        expectedProduct.setId(null);

        when(productService.addProduct(Mockito.any())).thenReturn(expectedProduct);

        ResponseEntity<ProductDto> actual = productController.add(expectedProduct);

        assertNotNull(actual.getBody());

        verify(productService, times(1)).addProduct(expectedProduct);
    }

    @Test
    void checkAddShouldReturnTrueIfExpectedPriceEqualsActual() {
        expectedProduct.setId(null);

        when(productService.addProduct(Mockito.any())).thenReturn(expectedProduct);

        ResponseEntity<ProductDto> actual = productController.add(expectedProduct);

        assertEquals(expectedProduct.getPrice(), actual.getBody().getPrice());

        verify(productService, times(1)).addProduct(expectedProduct);
    }

    @Test
    void checkAddShouldReturnTrueIfExpectedDiscountEqualsActual() {
        expectedProduct.setId(null);

        when(productService.addProduct(Mockito.any())).thenReturn(expectedProduct);

        ResponseEntity<ProductDto> actual = productController.add(expectedProduct);

        assertEquals(expectedProduct.isDiscount(), actual.getBody().isDiscount());

        verify(productService, times(1)).addProduct(expectedProduct);
    }

    @ParameterizedTest
    @MethodSource("productProvider")
    void checkUpdateShouldReturnUpdatedProduct() {
        ResponseEntity<ProductDto> actual = productController.update(expectedProduct);

        assertEquals(HttpStatus.OK, actual.getStatusCode());

        verify(productService, times(1)).update(expectedProduct);
    }

    @Test
    void checkFindByIdShouldReturnStatusOk() {
        Long id = 1L;

        when(productService.findById(anyLong())).thenReturn(expectedProduct);

        ResponseEntity<ProductDto> actual = productController.findById(id);

        verify(productService).findById(id);

        assertEquals(HttpStatus.OK, actual.getStatusCode());

        verify(productService, times(1)).findById(id);
    }

    @Test
    void checkFindByIdShouldReturnProductWithEqualsId() {
        Long id = 1L;

        when(productService.findById(anyLong())).thenReturn(expectedProduct);

        ResponseEntity<ProductDto> actual = productController.findById(id);

        verify(productService).findById(id);

        assertEquals(id, actual.getBody().getId());

        verify(productService, times(1)).findById(id);
    }

    @Test
    void checkFindByIdShouldReturnProductWithEqualsPrice() {
        Long id = 1L;

        when(productService.findById(anyLong())).thenReturn(expectedProduct);

        ResponseEntity<ProductDto> actual = productController.findById(id);

        verify(productService).findById(id);

        assertEquals(expectedProduct.getPrice(), actual.getBody().getPrice());

        verify(productService, times(1)).findById(id);
    }

    @Test
    void checkFindByIdShouldReturnProductWithEqualsDiscounts() {
        Long id = 1L;

        when(productService.findById(anyLong())).thenReturn(expectedProduct);

        ResponseEntity<ProductDto> actual = productController.findById(id);

        verify(productService).findById(id);

        assertEquals(expectedProduct.isDiscount(), actual.getBody().isDiscount());

        verify(productService, times(1)).findById(id);
    }


    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void checkFindByIdShouldTrowNoSuchProductException(long id) {
        when(productService.findById(id)).thenThrow(new NoSuchProductException("Check product id"));

        ResponseEntity<ProductDto> response = productController.findById(id);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());

        verify(productService).findById(id);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void checkDeleteByIdShouldReturnOkStatus(Long id) {
        ResponseEntity<ProductDto> response = productController.deleteById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(productService, times(1)).deleteById(id);
    }

    @Test
    void checkDeleteByIdShouldReturnException() {
        Long id = 1L;
        doThrow(new EmptyResultDataAccessException(1)).when(productService).deleteById(id);

        ResponseEntity actual = productController.deleteById(id);

        verify(productService, times(1)).deleteById(id);

        ResponseEntity expectedResponse = new ResponseEntity("That id: " + id + " not found", HttpStatus.NOT_ACCEPTABLE);

        assertEquals(expectedResponse, actual);
    }
}
