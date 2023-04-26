package com.example.zlagodasb.sale.model;

import com.example.zlagodasb.sale.Sale;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SaleModel {
    private String UPC;
    private Integer productNumber;

    public Sale toEntity() {
        Sale entity = new Sale();
        entity.setUPC(UPC);
        entity.setProductNumber(productNumber);
        return entity;
    }
}
