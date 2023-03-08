package com.example.clevertectesttaskbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "cache-info")
@XmlAccessorType(XmlAccessType.FIELD)
public class CacheInfoDto {
    @XmlElement(name = "card-size")
    private int cardSize;

    @XmlElement(name = "product-size")
    private int productSize;

    @XmlElement(name = "card-max-size")
    private int cardMaxSize;

    @XmlElement(name = "product-max-size")
    private int productMaxSize;
}
