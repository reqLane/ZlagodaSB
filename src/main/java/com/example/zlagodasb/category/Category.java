package com.example.zlagodasb.category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class Category {
    public static final String TABLE_NAME = "Category";

    private Integer categoryNumber;
    private String categoryName;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Category ID", categoryNumber);
        map.put("Name", categoryName);
        return map;
    }
}
