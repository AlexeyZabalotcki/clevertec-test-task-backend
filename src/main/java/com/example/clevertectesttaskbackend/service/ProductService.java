package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.exception.NoSuchProductException;
import com.example.clevertectesttaskbackend.model.DiscountCard;
import com.example.clevertectesttaskbackend.model.Product;
import com.example.clevertectesttaskbackend.repository.CardRepository;
import com.example.clevertectesttaskbackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CardRepository cardRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).get();
    }

    public Product addProducts(Product product) {
        return productRepository.save(product);
    }

    public Product update(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public void getCheck(Long id, Integer quantity) throws NoSuchProductException {
        headOfCheck();
        if (productRepository.findById(id).isPresent()) {
            Product product = productRepository.findById(id).get();
            String title = product.getTitle();
            BigDecimal itemPrice = product.getPrice();
            BigDecimal totalPrice = calculatePrice(product, quantity);
            System.out.printf("%s %7s %10s %6s\n", quantity, title, "$" + itemPrice, "$" + totalPrice);
        } else {
            throw new NoSuchProductException("There are no such id!!!\nCheck your input");
        }
    }

    private BigDecimal calculatePrice(Product product, Integer quantity ) {
        BigDecimal fullPrice;
        BigDecimal discount = BigDecimal.valueOf(0);
        BigDecimal finalPrice = BigDecimal.valueOf(0);
        Integer cardNumber = 1;
        DiscountCard card = cardRepository.findByNumber(cardNumber);
        if (quantity > 5 && product.isDiscount() && card.isDiscount()) {
            BigDecimal price = product.getPrice().multiply(BigDecimal.valueOf(10)).divide(BigDecimal.valueOf(100), 2)
                    .multiply(BigDecimal.valueOf(quantity));
            fullPrice = price.add(finalPrice);
            discount = fullPrice.add(price).multiply(BigDecimal.valueOf(10)).divide(BigDecimal.valueOf(100), 2);
            finalPrice = fullPrice.subtract(discount);
            checkBottom(fullPrice, finalPrice, discount);
            return price;
        } else {
            BigDecimal price = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            fullPrice = price.add(finalPrice);
            checkBottom(fullPrice, finalPrice, discount);
            return price;
        }
    }

    private void headOfCheck() {
        String receiptTitle = "CASH RECEIPT";
        String marketTitle = "SUPERMARKET 123";
        String address = "12, MILKWAY Galaxy/Earth";
        String phone = "Tel. 123-456-7890";
        String cashier = "CASHIER";
        Random random = new Random();
        int cashierNumb = random.nextInt(999);
        String qty = "QTY";
        String description = "DESCRIPTION";
        String price = "PRICE";
        String total = "TOTAL";
        String date = "DATE:";
        LocalDate currentDate = LocalDate.now();
        String currentTime = "TIME: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
        System.out.printf("%20s\n %21s\n %27s\n %22s\n ", receiptTitle, marketTitle, address, phone);
        System.out.printf("%s %9s\n %28s\n", cashier + ":" + cashierNumb, date + " " + currentDate, currentTime);
        System.out.println("-----------------------------");
        System.out.printf("%s %5s %6s %6s\n", qty, description, price, total);
    }

    private void checkBottom(BigDecimal fullPrice, BigDecimal finalPrice, BigDecimal discount) {
        String fullPriceText = "FULL PRICE";
        String discountText = "DISCOUNT";
        String taxable = "TAXABLE TOT.";
        String vat = "VAT17%";
        String total = "TOTAL";
        BigDecimal totalFullPrice = fullPrice;
        BigDecimal taxes = finalPrice.multiply(BigDecimal.valueOf(17)).divide(BigDecimal.valueOf(100), 2);
        System.out.println("-----------------------------");
        System.out.printf("%s %17s \n", fullPriceText, "$" + fullPrice);
        System.out.printf("%s %19s \n", discountText, "$" + discount);
        System.out.printf("%s %15s \n", taxable, "$" + finalPrice);
        System.out.printf("%s %21s \n", vat, "$" + taxes);
        System.out.printf("%s %22s \n", total, "$" + (totalFullPrice.add(taxes)));
    }
}
