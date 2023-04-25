package com.example.zlagodasb.category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Category {
    public static final String TABLE_NAME = "Category";

    private Integer categoryNumber;
    private String categoryName;
}
