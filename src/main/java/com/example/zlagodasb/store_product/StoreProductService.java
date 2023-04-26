package com.example.zlagodasb.store_product;

import com.example.zlagodasb.store_product.model.StoreProductInfo;
import com.example.zlagodasb.store_product.model.StoreProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static com.example.zlagodasb.util.Utils.isExpired;

@Service
public class StoreProductService {
    private final StoreProductRepo storeProductRepo;

    @Autowired
    public StoreProductService(StoreProductRepo storeProductRepo) {
        this.storeProductRepo = storeProductRepo;
    }

    //OPERATIONS

    public void updatePromoList() {
        for (StoreProduct storeProduct : storeProductRepo.findAllToBecomePromo()) {
            if(!storeProduct.isPromotionalProduct()) {
                storeProduct.setPromotionalProduct(true);
                storeProduct.setSellingPrice(BigDecimal.valueOf(storeProduct.getSellingPrice().doubleValue() * 0.8));
                storeProductRepo.update(storeProduct);
            }
        }
    }

    public List<StoreProduct> findAllPresent() {
        return storeProductRepo.findAllPresent();
    }

    public List<StoreProductInfo> findAllSortedByAmount() {
        return storeProductRepo.findAllSortedByProductsNumber();
    }

    public List<StoreProductInfo> findAllSortedByName() {
        return storeProductRepo.findAllSortedByProductName();
    }

    public StoreProductInfo getInfoByUPC(String UPC) {
        return storeProductRepo.getInfoByUPC(UPC);
    }

    public List<StoreProductInfo> findAllPromotionalSortedBy(String sortBy) {
        return storeProductRepo.findAllPromotionalSortedBy(sortBy);
    }

    public List<StoreProductInfo> findAllNonPromotionalSortedBy(String sortBy) {
        return storeProductRepo.findAllNonPromotionalSortedBy(sortBy);
    }

    //DEFAULT OPERATIONS

    public List<StoreProduct> findAll() {
        return storeProductRepo.findAll();
    }

    public StoreProduct findById(String UPC) {
        return storeProductRepo.findById(UPC);
    }

    public StoreProduct create(StoreProductModel storeProductModel) throws Exception {
        if(isExpired(storeProductModel.getExpirationDate()))
            throw new Exception("Can't create StoreProduct which is expired already");

        StoreProduct entity = storeProductModel.toEntity();

        List<StoreProduct> similarStoreProducts = storeProductRepo.findByIdProduct(entity.getIdProduct());
        for (StoreProduct similarStoreProduct : similarStoreProducts) {
            double newSellingPrice = entity.getSellingPrice().doubleValue();
            if(similarStoreProduct.isPromotionalProduct())
                newSellingPrice *= 0.8;
            similarStoreProduct.setSellingPrice(BigDecimal.valueOf(newSellingPrice));
            storeProductRepo.update(similarStoreProduct);
        }

        return storeProductRepo.create(entity);
    }

    public void update(StoreProduct storeProduct) {
        storeProductRepo.update(storeProduct);
    }

    public void deleteById(String UPC){
        storeProductRepo.deleteById(UPC);
    }

    public void delete(@NonNull Collection<StoreProduct> list){
        storeProductRepo.delete(list);
    }

    public void deleteAll(){
        storeProductRepo.deleteAll();
    }
}
