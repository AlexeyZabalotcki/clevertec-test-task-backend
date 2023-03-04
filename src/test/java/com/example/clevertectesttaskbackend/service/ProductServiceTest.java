package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.example.clevertectesttaskbackend.exception.NoSuchProductException;
import com.example.clevertectesttaskbackend.model.Product;
import com.example.clevertectesttaskbackend.repository.ProductRepository;
import com.example.clevertectesttaskbackend.tesabuilder.ProductDtoTestBuilder;
import com.example.clevertectesttaskbackend.tesabuilder.ProductTestBuilder;
import com.example.clevertectesttaskbackend.tesabuilder.TestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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

    private static ProductDto expectedDto;
    private static Product product;

    @BeforeEach
    void setUp() {
        TestBuilder<ProductDto> productDtoBuilder =
                new ProductDtoTestBuilder(1L, "Product 1", BigDecimal.valueOf(123), true);
        expectedDto = productDtoBuilder.build();

        TestBuilder<Product> productBuilder =
                new ProductTestBuilder(1L, "Product 1", BigDecimal.valueOf(123), true);
        product = productBuilder.build();
    }

    @Test
    void checkGetAllShouldReturnAllProducts() {
        List<ProductDto> expectedProductsDto = new ArrayList<>(Collections.singletonList(expectedDto));
        List<Product> expectedProducts = new ArrayList<>(Collections.singletonList(product));

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
