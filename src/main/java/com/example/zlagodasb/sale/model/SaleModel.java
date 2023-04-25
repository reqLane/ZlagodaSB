package com.example.zlagodasb.sale.model;

import com.example.zlagodasb.sale.Sale;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class SaleModel {
    private String UPC;
    private Integer productNumber;
    private BigDecimal sellingPrice;

    public Sale toEntity() {
        Sale entity = new Sale();
        entity.setUPC(UPC);
        entity.setProductNumber(productNumber);
        entity.setSellingPrice(sellingPrice);
        return entity;
    }
}
