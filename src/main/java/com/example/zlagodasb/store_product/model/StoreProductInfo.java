package com.example.zlagodasb.store_product.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class StoreProductInfo {
    private BigDecimal sellingPrice;
    private Integer productsNumber;
    private String productName;
    private String manufacturer;
    private String characteristics;
    private boolean promotionalProduct;
}
