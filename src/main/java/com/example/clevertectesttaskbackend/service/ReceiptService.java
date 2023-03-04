package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.dto.DiscountCardDto;
import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.example.clevertectesttaskbackend.dto.ProductQuantityDto;
import com.example.clevertectesttaskbackend.dto.ReceiptDto;
import com.example.clevertectesttaskbackend.exception.NoSuchReceiptException;
import com.example.clevertectesttaskbackend.model.Product;
import com.example.clevertectesttaskbackend.model.Receipt;
import com.example.clevertectesttaskbackend.repository.ReceiptRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    public ReceiptDto getReceipt(Long id) {
        Receipt receipt = receiptRepository.findById(id).orElseThrow(() -> new NoSuchReceiptException("Check id"));
        return toReceiptDto(receipt);
    }

    public ReceiptDto addReceipt(ReceiptDto receipt) throws DocumentException {
        ReceiptDto receiptDto = fillReceipt(receipt);
        Receipt receiptToDb = toReceipt(receiptDto);
        Receipt save = receiptRepository.save(receiptToDb);

        return toReceiptDto(save);
    }

    private BigDecimal calculatePrice(List<ProductQuantityDto> dtos, ReceiptDto receipt) {
        BigDecimal discountedPrice = getDiscountedPrice(dtos);
        BigDecimal nonDiscountedPrice = getNonDiscountedPrice(dtos);
        BigDecimal totalDiscountedPrice = discountedPrice.add(nonDiscountedPrice);
        BigDecimal fullPriceTotal = getFullPriceTotal(dtos);
        BigDecimal taxes = getTax(totalDiscountedPrice);

        return receipt.getCard() != null ? totalDiscountedPrice.add(taxes) : fullPriceTotal.add(taxes);
    }

    private BigDecimal getTax(BigDecimal priceWithoutDiscCard) {
        return priceWithoutDiscCard.multiply(BigDecimal.valueOf(17))
                .divide(BigDecimal.valueOf(100), RoundingMode.valueOf(2));
    }

    private BigDecimal getFullPriceTotal(List<ProductQuantityDto> dtos) {
        return dtos.stream().map(ProductQuantityDto::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getNonDiscountedPrice(List<ProductQuantityDto> dtos) {
        List<ProductQuantityDto> allProducts = new ArrayList<>(dtos);
        List<ProductQuantityDto> discountedProducts = dtos.stream()
                .filter(ProductQuantityDto::isDiscount)
                .collect(Collectors.toList());

        allProducts.removeAll(discountedProducts);

        return dtos.stream()
                .map(ProductQuantityDto::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getDiscountedPrice(List<ProductQuantityDto> dtos) {
        List<BigDecimal> discounts = dtos.stream()
                .filter(ProductQuantityDto::isDiscount)
                .map(this::calculateDiscount)
                .collect(Collectors.toList());

        List<BigDecimal> prices = dtos.stream()
                .filter(ProductQuantityDto::isDiscount)
                .map(ProductQuantityDto::getPrice)
                .collect(Collectors.toList());

        return IntStream.range(0, discounts.size())
                .mapToObj(i -> prices.get(i).subtract(discounts.get(i)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateDiscount(ProductQuantityDto product) {
        BigDecimal price = product.getPrice();
        return product.isDiscount() ? price.multiply(BigDecimal.valueOf(10))
                .divide(BigDecimal.valueOf(100), RoundingMode.valueOf(2)) : price;
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

    private ReceiptDto fillReceipt(ReceiptDto receipt) throws DocumentException {
        List<ProductDto> products = receipt.getProducts();

        Map<ProductDto, Long> productsToQuantity = products.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<ProductQuantityDto> dtos = toQuantityDto(productsToQuantity);

        BigDecimal price = calculatePrice(dtos, receipt);
        DiscountCardDto card = receipt.getCard();

        byte[] pdfReceipt = getPdfDoc(productsToQuantity, price);

        return ReceiptDto.builder()
                .products(products)
                .card(card)
                .receipt(pdfReceipt)
                .totalPrice(price)
                .build();
    }

    private byte[] getPdfDoc(Map<ProductDto, Long> productsToQuantity, BigDecimal price) throws DocumentException {
        Document document = new Document();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, outputStream);

        document.open();
        PdfPTable tableHeader = new PdfPTable(1);
        tableHeader.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        tableHeader.addCell("CASH RECEIPT");
        tableHeader.setWidthPercentage(50);

        PdfPTable tableMallTitle = new PdfPTable(1);
        tableMallTitle.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        tableMallTitle.addCell("SUPERMARKET 123");
        tableMallTitle.setWidthPercentage(50);

        PdfPTable tableAddress = new PdfPTable(1);
        tableAddress.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        tableAddress.addCell("12, MILKWAY Galaxy / Earth");
        tableAddress.setWidthPercentage(50);

        PdfPTable tablePhoneNumber = new PdfPTable(1);
        tablePhoneNumber.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        tablePhoneNumber.addCell("Tel: 123-456-7890");
        tablePhoneNumber.setWidthPercentage(50);

        PdfPTable tableCashierDate = new PdfPTable(3);
        tableCashierDate.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        tableCashierDate.addCell("CASHIER: ");
        int min = 1000;
        int max = 9999;
        Random random = new Random();
        tableCashierDate.addCell("№" + random.nextInt(max - min));
        tableCashierDate.addCell("DATE:" + LocalDate.now());

        PdfPTable tableForProducts = new PdfPTable(4);
        tableForProducts.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        tableForProducts.addCell("Qty");
        tableForProducts.addCell("Description");
        tableForProducts.addCell("Price");
        tableForProducts.addCell("Discount");
        productsToQuantity.entrySet().stream()
                .forEach(entry -> {
                    tableForProducts.addCell(String.valueOf(entry.getValue()));
                    tableForProducts.addCell(entry.getKey().getTitle());
                    tableForProducts.addCell(String.valueOf(entry.getKey().getPrice().
                            multiply(BigDecimal.valueOf(entry.getValue()))));
                    tableForProducts.addCell(String.valueOf(entry.getKey().isDiscount()));
                });

        PdfPTable tableForFooter = new PdfPTable(2);
        tableForFooter.addCell("Total Price");
        tableForFooter.addCell(String.valueOf(price));
        tableForFooter.setWidthPercentage(80);
        tableForFooter.setFooterRows(1);

        document.add(tableMallTitle);
        document.add(tableAddress);
        document.add(tablePhoneNumber);
        document.add(tableCashierDate);
        document.add(tableForProducts);
        document.add(tableForFooter);
        document.close();

        return outputStream.toByteArray();
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
