package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.example.clevertectesttaskbackend.exception.NoSuchProductException;
import com.example.clevertectesttaskbackend.model.Product;
import com.example.clevertectesttaskbackend.repository.ProductRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private ProductDto expectedDto;
    private Product product;

    @BeforeEach
    void setUp() {
        expectedDto = ProductDto.builder()
                .id(1L)
                .title("Product 1")
                .price(BigDecimal.valueOf(123))
                .discount(true)
                .build();

        product = Product.builder()
                .id(1L)
                .title("Product 1")
                .price(BigDecimal.valueOf(123))
                .discount(true)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void checkGetAllShouldReturnAllProducts() {
        List<ProductDto> expectedProductsDto = new ArrayList<>(Arrays.asList(expectedDto));
        List<Product> expectedProducts = new ArrayList<>(Arrays.asList(product));

        when(productRepository.findAll()).thenReturn(expectedProducts);

        List<ProductDto> actual = productService.getAll();

        assertEquals(expectedProductsDto, actual);

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void checkFindByIdShouldReturnProductDto() {
        Long id = 1L;

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        ProductDto actual = productService.findById(id);

        assertEquals(expectedDto, actual);

        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void checkFindByIdShouldTrowNoSuchProductException() {
        assertThrows(NoSuchProductException.class, () -> {
            productService.findById(1330L);
        });
    }

    @Test
    void checkAddProductShouldAddProductAndReturnProductDto() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto actual = productService.addProduct(expectedDto);

        assertEquals(expectedDto, actual);

        verify(productRepository, times(1)).save(product);
    }

    @Test
    void checkUpdateShouldUpdateProductAndReturnProductDto() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto actual = productService.update(expectedDto);

        assertEquals(expectedDto, actual);

        verify(productRepository, times(1)).save(product);
    }

    @Test
    void checkDeleteByIdShouldDeleteProduct() {
        Long id = 1L;
        doNothing().when(productRepository).deleteById(id);

        productService.deleteById(id);

        verify(productRepository, times(1)).deleteById(id);
    }
}
