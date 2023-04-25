package com.example.zlagodasb.store_product;

import com.example.zlagodasb.store_product.model.StoreProductInfo;
import com.example.zlagodasb.store_product.model.StoreProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Service
public class StoreProductService {
    private final StoreProductRepo storeProductRepo;

    @Autowired
    public StoreProductService(StoreProductRepo storeProductRepo) {
        this.storeProductRepo = storeProductRepo;
    }

    //OPERATIONS

    public List<StoreProduct> findAllSortedByAmount() {
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

    public StoreProduct create(StoreProductModel storeProductModel) {
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
