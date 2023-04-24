package com.example.zlagodasb.store_product;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class StoreProductService {
    private StoreProductRepo storeProductRepo;

    //OPERATIONS



    //DEFAULT OPERATIONS

    public List<StoreProduct> findAll() {
        return storeProductRepo.findAll();
    }

    public StoreProduct findById(String UPC) {
        return storeProductRepo.findById(UPC);
    }

    public StoreProduct create(StoreProduct storeProduct) {
        return storeProductRepo.create(storeProduct);
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
