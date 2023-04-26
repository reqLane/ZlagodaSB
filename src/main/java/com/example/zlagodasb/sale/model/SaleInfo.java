package com.example.zlagodasb.sale.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class SaleInfo {
    private String productName;
    private Integer productNumber;
    private BigDecimal sellingPrice;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Product name", productName);
        map.put("Amount", productNumber);
        map.put("Price", sellingPrice);
        return map;
    }
}
