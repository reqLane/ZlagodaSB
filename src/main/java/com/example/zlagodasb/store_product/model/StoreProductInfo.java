package com.example.zlagodasb.store_product.model;

import com.example.zlagodasb.sale.model.SaleInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("UPC", UPC);
        map.put("Name", productName);
        map.put("Amount", productsNumber);
        map.put("Price", sellingPrice);
        map.put("Expiration date", expirationDate);
        map.put("Promo", promotionalProduct);
        map.put("Manufacturer", manufacturer);
        map.put("Characteristics", characteristics);
        return map;
    }
}
