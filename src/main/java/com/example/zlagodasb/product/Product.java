package com.example.zlagodasb.product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Product {
    public static final String TABLE_NAME = "Product";

    private Integer idProduct;
    private Integer categoryNumber;
    private String productName;
    private String manufacturer;
    private String characteristics;
}
