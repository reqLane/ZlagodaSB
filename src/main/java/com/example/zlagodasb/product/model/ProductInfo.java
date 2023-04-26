package com.example.zlagodasb.product.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class ProductInfo {
    private Integer idProduct;
    private String productName;
    private String categoryName;
    private String manufacturer;
    private String characteristics;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Product ID", idProduct);
        map.put("Name", productName);
        map.put("Category", categoryName);
        map.put("Manufacturer", manufacturer);
        map.put("Characteristics", characteristics);
        return map;
    }
}
