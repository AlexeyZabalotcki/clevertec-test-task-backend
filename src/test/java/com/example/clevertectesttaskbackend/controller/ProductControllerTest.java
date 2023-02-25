package com.example.clevertectesttaskbackend.controller;

import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.example.clevertectesttaskbackend.exception.NoSuchProductException;
import com.example.clevertectesttaskbackend.service.ProductService;
import org.junit.jupiter.api.AfterEach;
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

    private ProductDto expectedProduct;

    @BeforeEach
    void setUp() {
        expectedProduct = ProductDto.builder()
                .id(1L)
                .title("Product 1")
                .price(BigDecimal.valueOf(123))
                .discount(true)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    private static Stream<Arguments> productProvider() {
        ProductDto expectedProduct = ProductDto.builder()
                .id(1L)
                .title("Product 1")
                .price(BigDecimal.valueOf(123))
                .discount(true)
                .build();

        ProductDto expectedProduct1 = ProductDto.builder()
                .id(1L)
                .title("Product 2")
                .price(BigDecimal.valueOf(1233))
                .discount(false)
                .build();
        return Stream.of(
                Arguments.of(expectedProduct),
                Arguments.of(expectedProduct1)
        );
    }

    @Test
    void checkFindAllShouldReturnAllProducts() {
        List<ProductDto> expectedProducts = Arrays.asList(expectedProduct);

        when(productService.getAll()).thenReturn(expectedProducts);

        List<ProductDto> actual = productController.findAll();

        assertNotNull(actual);
        assertEquals(expectedProducts.size(), actual.size());
        assertEquals(expectedProducts.get(0), actual.get(0));

        verify(productService, times(1)).getAll();
    }

    @Test
    void checkAddShouldReturnAddedProduct() {
        expectedProduct.setId(null);

        when(productService.addProduct(Mockito.any())).thenReturn(expectedProduct);

        ResponseEntity<ProductDto> actual = productController.add(expectedProduct);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertNull(actual.getBody().getId());
        assertEquals(BigDecimal.valueOf(123), actual.getBody().getPrice());
        assertTrue(actual.getBody().isDiscount());

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
    void checkFindByIdShouldReturnProduct() {
        Long id = 1L;

        when(productService.findById(anyLong())).thenReturn(expectedProduct);

        ResponseEntity<ProductDto> actual = productController.findById(id);

        verify(productService).findById(id);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(id, actual.getBody().getId());
        assertEquals(BigDecimal.valueOf(123), actual.getBody().getPrice());
        assertTrue(actual.getBody().isDiscount());

        verify(productService, times(1)).findById(id);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void checkFindByIdShouldTrowNoSuchProductException(long id) {
        when(productService.findById(id)).thenThrow(new NoSuchProductException("Check product id"));

        ResponseEntity<ProductDto> response = productController.findById(id);

        assertEquals("Check product id", response.getBody());
        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());

        verify(productService).findById(id);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void checkDeleteByIdShouldReturnOkStatus(Long id) {
        ResponseEntity<ProductDto> response = productController.deleteById(id);

        assertNotNull(response);
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
