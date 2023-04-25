package com.example.zlagodasb.sale;

import com.example.zlagodasb.sale.model.SaleInfo;
import com.example.zlagodasb.store_product.StoreProduct;
import com.example.zlagodasb.store_product.StoreProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class SaleService {
    private final SaleRepo saleRepo;
    private final StoreProductService storeProductService;

    @Autowired
    public SaleService(SaleRepo saleRepo,
                       StoreProductService storeProductService) {
        this.saleRepo = saleRepo;
        this.storeProductService = storeProductService;
    }

    //OPERATIONS

    public List<SaleInfo> getSalesInfoByCheckNumber(String checkNumber) {
        return saleRepo.getSalesInfoByCheckNumber(checkNumber);
    }

    //DEFAULT OPERATIONS

    public List<Sale> findAll() {
        return saleRepo.findAll();
    }

    public Sale findById(String UPC, String checkNumber) {
        return saleRepo.findById(UPC, checkNumber);
    }

    public Sale create(Sale sale) throws Exception {
        StoreProduct storeProduct = storeProductService.findById(sale.getUPC());
        if(storeProduct == null)
            throw new Exception("StoreProduct with UPC not found");

        Integer storedNumber = storeProduct.getProductsNumber();
        Integer expectedNumber = sale.getProductNumber();
        int resultStoredNumber = storedNumber - expectedNumber;

        if(resultStoredNumber < 0)
            throw new Exception("StoreProduct amount is less than expected");
        else {
            storeProduct.setProductsNumber(resultStoredNumber);
            storeProductService.update(storeProduct);
        }

        sale.setSellingPrice(storeProduct.getSellingPrice());

        return saleRepo.create(sale);
    }

    public void update(Sale sale) {
        saleRepo.update(sale);
    }

    public void deleteById(String UPC, String checkNumber){
        saleRepo.deleteById(UPC, checkNumber);
    }

    public void delete(@NonNull Collection<Sale> list){
        saleRepo.delete(list);
    }

    public void deleteAll(){
        saleRepo.deleteAll();
    }
}
