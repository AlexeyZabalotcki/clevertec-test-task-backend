package com.example.clevertectesttaskbackend.service;

import com.example.clevertectesttaskbackend.dto.ProductDto;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PdfUtils {

    private final String HEADER = "CASH RECEIPT";
    private final String SHOP = "SUPERMARKET 123";
    private final String ADDRESS = "12, MILKWAY Galaxy / Earth";
    private final String TEL = "Tel: 123-456-7890";
    private final String CASHIER = "CASHIER: ";
    private final String DATE = "DATE:";
    private final String TOTAL_PRICE = "Total Price";


    static class BackgroundImage extends PdfPageEventHelper {

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                Image image = Image.getInstance("resources/template.png");

                image.setAbsolutePosition(0, 0);
                image.scaleToFit(document.getPageSize().getWidth(), document.getPageSize().getHeight());

                PdfContentByte canvas = writer.getDirectContentUnder();
                canvas.addImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getPdfDoc(Map<ProductDto, Long> productsToQuantity, BigDecimal price) throws Exception {
        Document document = new Document(PageSize.A4, 36, 36, 144, 54);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        writer.setPageEvent(new BackgroundImage());

        PdfPTable header = crateHeader();
        PdfPTable mallTitle = crateMallTitle();
        PdfPTable address = crateAddress();
        PdfPTable phoneNumber = cratePhoneNumber();
        PdfPTable cashierDate = crateCashierDate();
        PdfPTable products = crateProducts(productsToQuantity);
        PdfPTable footer = crateFooter(price);

        document.add(header);
        document.add(mallTitle);
        document.add(address);
        document.add(phoneNumber);
        document.add(cashierDate);
        document.add(products);
        document.add(footer);
        document.close();

        return outputStream.toByteArray();
    }

    private PdfPTable crateHeader() {
        PdfPTable tableHeader = new PdfPTable(1);
        tableHeader.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        tableHeader.addCell(HEADER);
        tableHeader.setWidthPercentage(50);
        return tableHeader;
    }

    private PdfPTable crateMallTitle() {
        PdfPTable tableMallTitle = new PdfPTable(1);
        tableMallTitle.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        tableMallTitle.addCell(SHOP);
        tableMallTitle.setWidthPercentage(50);
        return tableMallTitle;
    }

    private PdfPTable crateAddress() {
        PdfPTable tableAddress = new PdfPTable(1);
        tableAddress.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        tableAddress.addCell(ADDRESS);
        tableAddress.setWidthPercentage(50);
        return tableAddress;
    }

    private PdfPTable cratePhoneNumber() {
        PdfPTable tablePhoneNumber = new PdfPTable(1);
        tablePhoneNumber.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        tablePhoneNumber.addCell(TEL);
        tablePhoneNumber.setWidthPercentage(50);
        return tablePhoneNumber;
    }

    private PdfPTable crateCashierDate() {
        PdfPTable tableCashierDate = new PdfPTable(3);
        tableCashierDate.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        tableCashierDate.addCell(CASHIER);
        int min = 1000;
        int max = 9999;
        Random random = new Random();
        tableCashierDate.addCell("№" + random.nextInt(max - min));
        tableCashierDate.addCell(DATE + LocalDate.now());
        return tableCashierDate;
    }

    private PdfPTable crateProducts(Map<ProductDto, Long> productsToQuantity) {
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
        return tableForProducts;
    }

    private PdfPTable crateFooter(BigDecimal price) {
        PdfPTable tableForFooter = new PdfPTable(2);
        tableForFooter.addCell(TOTAL_PRICE);
        tableForFooter.addCell(String.valueOf(price));
        tableForFooter.setWidthPercentage(80);
        tableForFooter.setFooterRows(1);
        return tableForFooter;
    }
}
