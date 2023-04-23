package com.example.zlagodasb.store_product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@ToString
public class StoreProduct {
    public static final String TABLE_NAME = "Store_Product";

    private String UPC;
    private Integer idProduct;
    private BigDecimal sellingPrice;
    private Integer productsNumber;
    private Date expirationDate;
    private boolean promotionalProduct;
}
