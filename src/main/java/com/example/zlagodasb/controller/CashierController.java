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
import com.example.zlagodasb.product.Product;
import com.example.zlagodasb.product.ProductService;
import com.example.zlagodasb.sale.SaleService;
import com.example.zlagodasb.store_product.StoreProductService;
import com.example.zlagodasb.store_product.model.StoreProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.example.zlagodasb.util.Utils.isNullOrEmpty;

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
    public ResponseEntity<Boolean> createCheck(@RequestBody CheckModel data) {
        try {
            if(isNullOrEmpty(data.getCardNumber())) data.setCardNumber(null);

            Check saved = checkService.create(data);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    //FUNCTIONAL OPERATIONS

    //getProductsSortedByName mutual

    @GetMapping("/getStoreProductsSortedByName")
    public ResponseEntity<List<StoreProductInfo>> getStoreProductsSortedByNumber() {
        try {
            List<StoreProductInfo> result = storeProductService.findAllSortedByName();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    //getCustomersSortedBySurname mutual

    @PostMapping("/getProductsByName")
    public ResponseEntity<List<Product>> getProductsByName(@RequestBody Map<String, String> data) {
        try {
            List<Product> result = productService.findAllByName(data.get("productName"));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    //getProductsWithCategoryNameSortedByName mutual

    @PostMapping("/getClientsBySurname")
    public ResponseEntity<List<CustomerCard>> getClientsBySurname(@RequestBody Map<String, String> data) {
        try {
            List<CustomerCard> result = customerCardService.findAllBySurname(data.get("custSurname"));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getAllChecksMadeToday")
    public ResponseEntity<List<CheckInfo>> getAllChecksMadeToday(@RequestBody Map<String, String> data) {
        try {
            String idCashier = data.get("idCashier");

            Timestamp now = new Timestamp(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Timestamp yesterday = new Timestamp(calendar.getTimeInMillis());

            List<CheckInfo> result = checkService.getChecksInfoOfCashierInPeriod(idCashier, yesterday, now);
            for (CheckInfo checkInfo : result)
                checkInfo.setSalesInfo(null);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getAllChecksMadeInPeriod")
    public ResponseEntity<List<CheckInfo>> getAllChecksMadeInPeriod(@RequestBody Map<String, String> data) {
        try {
            String idCashier = data.get("idCashier");

            Timestamp startDate = new Timestamp(Date.valueOf(data.get("startDate")).getTime());
            Timestamp endDate = new Timestamp(Date.valueOf(data.get("endDate")).getTime());

            List<CheckInfo> result = checkService.getChecksInfoOfCashierInPeriod(idCashier, startDate, endDate);
            for (CheckInfo checkInfo : result)
                checkInfo.setSalesInfo(null);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getCheckInfoByCheckNumber")
    public ResponseEntity<CheckInfo> getCheckInfoByCheckNumber(@RequestBody Map<String, String> data) {
        try {
            String checkNumber = data.get("checkNumber");
            CheckInfo result = checkService.getCheckInfoByCheckNumber(checkNumber);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }

    //getPromotionalProductsSorted mutual

    //getNonPromotionalProductsSorted mutual

    //getStoreProductInfoByUPC mutual

    @PostMapping("/getSelfInfo")
    public ResponseEntity<Employee> getSelfInfo(@RequestBody Map<String, String> data) {
        try {
            String idCashier = data.get("idCashier");
            Employee result = employeeService.findById(idCashier);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }
}
