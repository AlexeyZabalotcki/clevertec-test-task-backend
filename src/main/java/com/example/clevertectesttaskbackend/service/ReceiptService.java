package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.dto.DiscountCardDto;
import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.example.clevertectesttaskbackend.dto.ProductQuantityDto;
import com.example.clevertectesttaskbackend.dto.ReceiptDto;
import com.example.clevertectesttaskbackend.exception.NoSuchReceiptException;
import com.example.clevertectesttaskbackend.model.Product;
import com.example.clevertectesttaskbackend.model.Receipt;
import com.example.clevertectesttaskbackend.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final CalculateUtils calculateUtils;
    private final PdfUtils pdfUtils;

    public ReceiptDto getReceipt(Long id) {
        Receipt receipt = receiptRepository.findById(id).orElseThrow(() -> new NoSuchReceiptException("Check id"));
        return toReceiptDto(receipt);
    }

    public ReceiptDto addReceipt(ReceiptDto receipt) throws Exception {
        ReceiptDto receiptDto = fillReceipt(receipt);
        Receipt receiptToDb = toReceipt(receiptDto);
        Receipt save = receiptRepository.save(receiptToDb);

        return toReceiptDto(save);
    }


    private ReceiptDto fillReceipt(ReceiptDto receipt) throws Exception {
        List<ProductDto> products = receipt.getProducts();

        Map<ProductDto, Long> productsToQuantity = products.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<ProductQuantityDto> dtos = toQuantityDto(productsToQuantity);

        BigDecimal price = calculateUtils.calculatePrice(dtos, receipt);
        DiscountCardDto card = receipt.getCard();

        byte[] pdfReceipt = pdfUtils.getPdfDoc(productsToQuantity, price);

        return ReceiptDto.builder()
                .products(products)
                .card(card)
                .receipt(pdfReceipt)
                .totalPrice(price)
                .build();
    }

    private Receipt toReceipt(ReceiptDto dto) {
        List<ProductDto> products = dto.getProducts();
        List<Product> productsEntity = products.stream().map(this::toProduct).collect(Collectors.toList());

        return Receipt.builder()
                .products(productsEntity)
                .receipt(dto.getReceipt())
                .totalPrice(dto.getTotalPrice())
                .build();
    }

    private ReceiptDto toReceiptDto(Receipt receipt) {
        List<Product> products = receipt.getProducts();
        List<ProductDto> productsDto = products.stream().map(this::toProductDto).collect(Collectors.toList());

        return ReceiptDto.builder()
                .products(productsDto)
                .receipt(receipt.getReceipt())
                .totalPrice(receipt.getTotalPrice())
                .build();
    }

    private Product toProduct(ProductDto dto) {
        return Product.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .price(dto.getPrice())
                .discount(dto.isDiscount())
                .build();
    }

    private ProductDto toProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .discount(product.isDiscount())
                .build();
    }

    private List<ProductQuantityDto> toQuantityDto(Map<ProductDto, Long> productsToQuantity) {
        return productsToQuantity.entrySet().stream()
                .map(p -> ProductQuantityDto.builder()
                        .id(p.getKey().getId())
                        .title(p.getKey().getTitle())
                        .price(p.getKey().getPrice().multiply(BigDecimal.valueOf(p.getValue())))
                        .quantity(Math.toIntExact(p.getValue()))
                        .discount(p.getKey().isDiscount())
                        .build())
                .collect(Collectors.toList());
    }
}
