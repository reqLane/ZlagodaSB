package com.example.zlagodasb.sale.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class SaleInfo {
    private String productName;
    private Integer productNumber;
    private BigDecimal sellingPrice;
}
