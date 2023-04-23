package com.example.zlagodasb.sale;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class Sale {
    public static final String TABLE_NAME = "Sale";

    private String UPC;
    private String checkNumber;
    private Integer productNumber;
    private BigDecimal sellingPrice;
}
