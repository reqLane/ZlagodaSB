package com.example.zlagodasb.product;

import com.example.zlagodasb.store_product.StoreProduct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Product {
    public static final String TABLE_NAME = "Product";

    private Integer idProduct;
    private Integer categoryNumber;
    private String productName;
    private String manufacturer;
    private String characteristics;

    private List<StoreProduct> storeProducts = new ArrayList<>();
}
