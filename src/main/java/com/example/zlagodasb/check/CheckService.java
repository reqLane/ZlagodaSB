package com.example.zlagodasb.check;

import com.example.zlagodasb.check.model.CheckInfo;
import com.example.zlagodasb.check.model.CheckModel;
import com.example.zlagodasb.customer_card.CustomerCard;
import com.example.zlagodasb.customer_card.CustomerCardService;
import com.example.zlagodasb.sale.Sale;
import com.example.zlagodasb.sale.SaleService;
import com.example.zlagodasb.sale.model.SaleModel;
import com.example.zlagodasb.store_product.StoreProduct;
import com.example.zlagodasb.store_product.StoreProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.example.zlagodasb.util.Utils.isExpired;
import static com.example.zlagodasb.util.Utils.parseIdFrom;

@Service
public class CheckService {
    private final CheckRepo checkRepo;
    private final SaleService saleService;
    private final CustomerCardService customerCardService;
    private final StoreProductService storeProductService;

    @Autowired
    public CheckService(CheckRepo checkRepo,
                        SaleService saleService,
                        CustomerCardService customerCardService,
                        StoreProductService storeProductService) {
        this.checkRepo = checkRepo;
        this.saleService = saleService;
        this.customerCardService = customerCardService;
        this.storeProductService = storeProductService;
    }

    //OPERATIONS

    public List<Check> deleteOldChecks() {
        List<Check> oldChecks = checkRepo.findOldChecks();

        delete(oldChecks);

        return oldChecks;
    }

    public List<CheckInfo> getChecksInfoOfCashierInPeriod(String idCashier, Timestamp startDate, Timestamp endDate) {
        if(startDate.compareTo(endDate) > 0) {
            Timestamp tmp = new Timestamp(startDate.getTime());
            startDate = new Timestamp(endDate.getTime());
            endDate = tmp;
        }

        List<CheckInfo> result = checkRepo.getChecksInfoOfCashierInPeriod(idCashier, startDate, endDate);
        for (CheckInfo checkInfo : result) {
            checkInfo.setSalesInfo(saleService.getSalesInfoByCheckNumber(checkInfo.getCheckNumber()));
        }

        return result;
    }

    public List<CheckInfo> getAllChecksInfoInPeriod(Timestamp startDate, Timestamp endDate) {
        if(startDate.compareTo(endDate) > 0) {
            Timestamp tmp = new Timestamp(startDate.getTime());
            startDate = new Timestamp(endDate.getTime());
            endDate = tmp;
        }

        List<CheckInfo> result = checkRepo.getAllChecksInfoInPeriod(startDate, endDate);
        for (CheckInfo checkInfo : result) {
            checkInfo.setSalesInfo(saleService.getSalesInfoByCheckNumber(checkInfo.getCheckNumber()));
        }

        return result;
    }

    public CheckInfo getCheckInfoByCheckNumber(String checkNumber) throws Exception {
        CheckInfo result = checkRepo.getCheckInfoByCheckNumber(checkNumber);
        if(result == null)
            throw new Exception("Check with number not found");

        result.setSalesInfo(saleService.getSalesInfoByCheckNumber(result.getCheckNumber()));

        return result;
    }

    public BigDecimal getTotalIncomeFromChecksOfCashierInPeriod(String idCashier, Timestamp startDate, Timestamp endDate) {
        BigDecimal result = checkRepo.getTotalIncomeFromChecksOfCashierInPeriod(idCashier, startDate, endDate);

        return result != null ? result : BigDecimal.ZERO;
    }

    public BigDecimal getTotalIncomeFromChecksInPeriod(Timestamp startDate, Timestamp endDate) {
        BigDecimal result = checkRepo.getTotalIncomeFromChecksInPeriod(startDate, endDate);

        return result != null ? result : BigDecimal.ZERO;
    }

    //DEFAULT OPERATIONS

    public List<Check> findAll() {
        return checkRepo.findAll();
    }

    public Check findById(String checkNumber) {
        return checkRepo.findById(checkNumber);
    }

    @Transactional
    public Check create(CheckModel checkModel) throws Exception {
        if(checkModel.getSaleModels() == null || checkModel.getSaleModels().isEmpty())
            throw new Exception("Can't create empty check");

        Check entity = checkModel.toEntity();

        String uuid = UUID.randomUUID().toString();
        String checkNumber = uuid.substring(1, 3) + uuid.substring(9, 11) + uuid.substring(14, 16) + uuid.substring(19, 21) + uuid.substring(24, 26);
        entity.setCheckNumber(checkNumber);

        entity.setPrintDate(new Timestamp(System.currentTimeMillis()));

        List<Sale> sales = new ArrayList<>();
        double sumTotal = 0;
        for (SaleModel saleModel : checkModel.getSaleModels()) {
            saleModel.setUPC(parseIdFrom(saleModel.getUPC()));
            Sale sale = saleModel.toEntity();
            sale.setCheckNumber(entity.getCheckNumber());

            StoreProduct storeProduct = storeProductService.findById(saleModel.getUPC());
            if(storeProduct == null)
                throw new Exception("StoreProduct with UPC not found");
            if(storeProduct.getProductsNumber() <= 0)
                throw new Exception("StoreProduct with UPC not present in shop");
            if(isExpired(storeProduct.getExpirationDate()))
                throw new Exception("StoreProduct with UPC was expired and taken off");

            Integer storedNumber = storeProduct.getProductsNumber();
            Integer expectedNumber = saleModel.getProductNumber();
            int resultStoredNumber = storedNumber - expectedNumber;
            if(resultStoredNumber < 0)
                throw new Exception("StoreProduct amount is less than expected");

            storeProduct.setProductsNumber(resultStoredNumber);
            storeProductService.update(storeProduct);

            sale.setSellingPrice(storeProduct.getSellingPrice());
            sales.add(sale);

            sumTotal += sale.getProductNumber() * sale.getSellingPrice().doubleValue();
        }
        double vat = sumTotal * 0.2;
        entity.setVat(BigDecimal.valueOf(vat));

        if(entity.getCardNumber() != null) {
            CustomerCard customerCard = customerCardService.findById(entity.getCardNumber());
            double coefficient = (100 - customerCard.getPercent()) / 100.0;
            sumTotal *= coefficient;
        }

        entity.setSumTotal(BigDecimal.valueOf(sumTotal));

        entity = checkRepo.create(entity);

        for (Sale sale : sales) {
            saleService.create(sale);
        }

        return entity;
    }

    public void update(Check check) {
        checkRepo.update(check);
    }

    public void deleteById(String checkNumber){
        checkRepo.deleteById(checkNumber);
    }

    public void delete(@NonNull Collection<Check> list){
        checkRepo.delete(list);
    }

    public void deleteAll(){
        checkRepo.deleteAll();
    }
}
