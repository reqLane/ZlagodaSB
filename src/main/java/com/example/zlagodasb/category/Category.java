package com.example.zlagodasb.category;

import com.example.zlagodasb.product.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Category {
    public static final String TABLE_NAME = "Category";

    private Integer categoryNumber;
    private String categoryName;

    private List<Product> products = new ArrayList<>();
}
