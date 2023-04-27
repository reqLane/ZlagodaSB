package com.example.zlagodasb.controller;

import com.example.zlagodasb.category.CategoryService;
import com.example.zlagodasb.check.Check;
import com.example.zlagodasb.check.model.CheckInfo;
import com.example.zlagodasb.check.model.CheckModel;
import com.example.zlagodasb.check.CheckService;
import com.example.zlagodasb.customer_card.CustomerCard;
import com.example.zlagodasb.customer_card.CustomerCardService;
import com.example.zlagodasb.employee.Employee;
import com.example.zlagodasb.employee.EmployeeService;
import com.example.zlagodasb.employee.model.EmployeeInfo;
import com.example.zlagodasb.product.ProductService;
import com.example.zlagodasb.product.model.ProductInfo;
import com.example.zlagodasb.sale.SaleService;
import com.example.zlagodasb.sale.model.SaleModel;
import com.example.zlagodasb.store_product.StoreProductService;
import com.example.zlagodasb.store_product.model.StoreProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

import static com.example.zlagodasb.util.Utils.isNullOrEmpty;
import static com.example.zlagodasb.util.Utils.parseIdFrom;

@RestController
@RequestMapping("/cashier")
public class CashierController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final StoreProductService storeProductService;
    private final EmployeeService employeeService;
    private final CustomerCardService customerCardService;
    private final CheckService checkService;
    private final SaleService saleService;

    @Autowired
    public CashierController(CategoryService categoryService,
                             ProductService productService,
                             StoreProductService storeProductService,
                             EmployeeService employeeService,
                             CustomerCardService customerCardService,
                             CheckService checkService,
                             SaleService saleService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.storeProductService = storeProductService;
        this.employeeService = employeeService;
        this.customerCardService = customerCardService;
        this.checkService = checkService;
        this.saleService = saleService;
    }

    //DATA OPERATIONS

    @PostMapping("/createCheck")
    public ResponseEntity<Boolean> createCheck(@RequestBody Map<String, Object> data) {
        try {
            CheckModel checkModel = new CheckModel();
            checkModel.setIdEmployee((String)data.get("idEmployee"));
            Object cardNumber = data.get("cardNumber");
            if(cardNumber == null || isNullOrEmpty((String)cardNumber)) checkModel.setCardNumber(null);
            else checkModel.setCardNumber(parseIdFrom((String)cardNumber));

            List<LinkedHashMap<String, ?>> list = (ArrayList<LinkedHashMap<String, ?>>)data.get("saleModels");
            List<SaleModel> saleModels = new ArrayList<>();
            for (LinkedHashMap<String, ?> map : list) {
                SaleModel saleModel = new SaleModel();
                saleModel.setUPC((String)map.get("UPC"));
                saleModel.setProductNumber((Integer)map.get("productNumber"));
                saleModels.add(saleModel);
            }
            checkModel.setSaleModels(saleModels);

            Check saved = checkService.create(checkModel);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    //FUNCTIONAL OPERATIONS

    //getProductsSortedByName mutual

    @GetMapping("/getStoreProductsSortedByName")
    public ResponseEntity<List<Map<String, Object>>> getStoreProductsSortedByNumber() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (StoreProductInfo storeProductInfo : storeProductService.findAllSortedByName()) {
                result.add(storeProductInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    //getCustomersSortedBySurname mutual

    @PostMapping("/getProductsByName")
    public ResponseEntity<List<Map<String, Object>>> getProductsByName(@RequestBody Map<String, String> data) {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (ProductInfo productInfo : productService.findAllByName(data.get("productName"))) {
                result.add(productInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    //getProductsWithCategoryNameSortedByName mutual

    @PostMapping("/getClientsBySurname")
    public ResponseEntity<List<Map<String, Object>>> getClientsBySurname(@RequestBody Map<String, String> data) {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (CustomerCard customerCard : customerCardService.findAllBySurname(data.get("custSurname"))) {
                result.add(customerCard.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getAllChecksMadeToday")
    public ResponseEntity<List<Map<String, Object>>> getAllChecksMadeToday(@RequestBody Map<String, String> data) {
        try {
            String idCashier = data.get("idCashier");

            Timestamp now = new Timestamp(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Timestamp yesterday = new Timestamp(calendar.getTimeInMillis());

            List<Map<String, Object>> result = new ArrayList<>();
            for (CheckInfo checkInfo : checkService.getChecksInfoOfCashierInPeriod(idCashier, yesterday, now)) {
                result.add(checkInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getAllChecksMadeInPeriod")
    public ResponseEntity<List<Map<String, Object>>> getAllChecksMadeInPeriod(@RequestBody Map<String, String> data) {
        try {
            String idCashier = data.get("idCashier");

            Timestamp startDate = new Timestamp(Date.valueOf(data.get("startDate")).getTime());
            Timestamp endDate = new Timestamp(Date.valueOf(data.get("endDate")).getTime());

            List<Map<String, Object>> result = new ArrayList<>();
            for (CheckInfo checkInfo : checkService.getChecksInfoOfCashierInPeriod(idCashier, startDate, endDate)) {
                result.add(checkInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getCheckInfoByCheckNumber")
    public ResponseEntity<Map<String, Object>> getCheckInfoByCheckNumber(@RequestBody Map<String, String> data) {
        try {
            String checkNumber = data.get("checkNumber");
            CheckInfo result = checkService.getCheckInfoByCheckNumber(checkNumber);
            return ResponseEntity.ok(result.toMap());
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }

    //getPromotionalProductsSorted mutual

    //getNonPromotionalProductsSorted mutual

    //getStoreProductInfoByUPC mutual

    @PostMapping("/getSelfInfo")
    public ResponseEntity<Map<String, Object>> getSelfInfo(@RequestBody Map<String, String> data) {
        try {
            String idCashier = data.get("idCashier");
            EmployeeInfo result = employeeService.findById(idCashier);
            return ResponseEntity.ok(result.toMap());
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }
}
