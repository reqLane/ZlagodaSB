package com.example.zlagodasb.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepo productRepo;

    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    //OPERATIONS

    public Product findByProductName(String productName) {
        return productRepo.findByProductName(productName);
    }

    //DEFAULT OPERATIONS

    public List<Product> findAll() {
        return productRepo.findAll();
    }

    public Product findById(Integer idProduct) {
        return productRepo.findById(idProduct);
    }

    public Product create(ProductModel productModel) {
        return productRepo.create(productModel.toEntity());
    }

    public void update(Product product) {
        productRepo.update(product);
    }

    public void deleteById(Integer idProduct){
        productRepo.deleteById(idProduct);
    }

    public void delete(@NonNull Collection<Product> list){
        productRepo.delete(list);
    }

    public void deleteAll(){
        productRepo.deleteAll();
    }
}
