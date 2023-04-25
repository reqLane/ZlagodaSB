package com.example.zlagodasb.store_product.model;

import com.example.zlagodasb.store_product.StoreProduct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@ToString
public class StoreProductModel {
    private String UPC;
    private Integer idProduct;
    private BigDecimal sellingPrice;
    private Integer productsNumber;
    private Date expirationDate;
    private boolean promotionalProduct;

    public StoreProduct toEntity() {
        StoreProduct entity = new StoreProduct();
        entity.setUPC(UPC);
        entity.setIdProduct(idProduct);
        entity.setSellingPrice(sellingPrice);
        entity.setProductsNumber(productsNumber);
        entity.setExpirationDate(expirationDate);
        entity.setPromotionalProduct(promotionalProduct);
        return entity;
    }
}
