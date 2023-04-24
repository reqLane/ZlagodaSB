package com.example.zlagodasb.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepo categoryRepo;

    @Autowired
    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    //OPERATIONS

    public Category findByCategoryName(String categoryName) {
        return categoryRepo.findByCategoryName(categoryName);
    }

    //DEFAULT OPERATIONS

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    public Category findById(Integer categoryNumber) {
        return categoryRepo.findById(categoryNumber);
    }

    public Category create(CategoryModel categoryModel) {
        return categoryRepo.create(categoryModel.toEntity());
    }

    public void update(Category category) {
        categoryRepo.update(category);
    }

    public void deleteById(Integer categoryNumber){
        categoryRepo.deleteById(categoryNumber);
    }

    public void delete(@NonNull Collection<Category> list){
        categoryRepo.delete(list);
    }

    public void deleteAll(){
        categoryRepo.deleteAll();
    }
}
