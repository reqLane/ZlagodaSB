package com.example.zlagodasb.store_product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

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



    //DEFAULT OPERATIONS

    public List<StoreProduct> findAll() {
        return storeProductRepo.findAll();
    }

    public StoreProduct findById(String UPC) {
        return storeProductRepo.findById(UPC);
    }

    public StoreProduct create(StoreProductModel storeProductModel) {
        return storeProductRepo.create(storeProductModel.toEntity());
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
