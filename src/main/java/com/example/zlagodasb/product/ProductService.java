package com.example.zlagodasb.product;

import com.example.zlagodasb.product.model.ProductInfo;
import com.example.zlagodasb.product.model.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
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

    public List<ProductInfo> findAllInfo() {
        return productRepo.findAllInfo();
    }

    public Product findByProductName(String productName) {
        return productRepo.findByProductName(productName);
    }

    public List<ProductInfo> findAllSortedByName() {
        return productRepo.findAllSortedByProductName();
    }

    public List<ProductInfo> findAllByName(String productName) {
        return productRepo.findAllByProductName(productName);
    }

    public List<ProductInfo> findAllWithCategorySortedByName(Integer categoryNumber) {
        return productRepo.findAllWithCategoryNumberSortedByName(categoryNumber);
    }

    public Long getTotalAmountOfProductSoldInPeriod(Integer idProduct, Timestamp startDate, Timestamp endDate) {
        Long result = productRepo.getTotalAmountOfProductSoldInPeriod(idProduct, startDate, endDate);

        return result != null ? result : (long)0;
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
