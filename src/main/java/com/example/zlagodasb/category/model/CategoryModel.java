package com.example.zlagodasb.category.model;

import com.example.zlagodasb.category.Category;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryModel {
    private String categoryName;

    public Category toEntity() {
        Category entity = new Category();
        entity.setCategoryName(categoryName);
        return entity;
    }
}
