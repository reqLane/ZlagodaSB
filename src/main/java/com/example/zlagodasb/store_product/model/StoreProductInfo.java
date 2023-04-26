package com.example.zlagodasb.store_product.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@ToString
public class StoreProductInfo {
    private String UPC;
    private String productName;
    private Integer productsNumber;
    private BigDecimal sellingPrice;
    private Date expirationDate;
    private boolean promotionalProduct;
    private String manufacturer;
    private String characteristics;
}
