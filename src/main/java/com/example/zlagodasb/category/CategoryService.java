package com.example.zlagodasb.category;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class CategoryService {
    private CategoryRepo categoryRepo;

    //OPERATIONS



    //DEFAULT OPERATIONS

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    public Category findById(Integer categoryNumber) {
        return categoryRepo.findById(categoryNumber);
    }

    public Category create(Category category) {
        return categoryRepo.create(category);
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
