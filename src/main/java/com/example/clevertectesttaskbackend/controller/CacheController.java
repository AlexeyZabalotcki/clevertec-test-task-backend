package com.example.clevertectesttaskbackend.controller;

import com.example.clevertectesttaskbackend.cache.CardLruCache;
import com.example.clevertectesttaskbackend.cache.ProductLruCache;
import com.example.clevertectesttaskbackend.dto.CacheInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor
public class CacheController {

    private CardLruCache cardCache;
    private ProductLruCache productCache;

    @Autowired
    public CacheController(@Qualifier("cardLruCache") CardLruCache cardLruCache,
                           @Qualifier("productLruCache") ProductLruCache productLruCache) {
        this.cardCache = cardLruCache;
        this.productCache = productLruCache;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public CacheInfoDto getCacheInfo() {
        int cardSize = cardCache.size();
        int cardCapacity = cardCache.getCapacity();
        int productSize = productCache.size();
        int productCapacity = productCache.getCapacity();

        return new CacheInfoDto(cardSize, productSize, cardCapacity, productCapacity);
    }
}