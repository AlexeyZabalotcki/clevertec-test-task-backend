package com.example.clevertectesttaskbackend.controller;

import com.example.clevertectesttaskbackend.dto.ReceiptDto;
import com.example.clevertectesttaskbackend.service.ReceiptService;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receipt")
@RequiredArgsConstructor
public class ReceiptController {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);

    private final ReceiptService receiptService;

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> getById(@PathVariable Long id) {
        byte[] data = receiptService.getReceipt(id).getReceipt();
        ByteArrayResource resource = new ByteArrayResource(data);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "receipt; filename=receipt.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @PostMapping("/")
    public ResponseEntity<ReceiptDto> addReceipt(@RequestBody ReceiptDto receipt) {
        logger.info("Controller input receipt " + receipt);
        ReceiptDto receiptDto = null;
        try {
            receiptDto = receiptService.addReceipt(receipt);
        } catch (DocumentException e) {
            e.printStackTrace();
            return new ResponseEntity("Not found receipt", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(receiptDto);
    }

}
