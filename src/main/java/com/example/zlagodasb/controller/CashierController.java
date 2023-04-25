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
import com.example.zlagodasb.sale.Sale;
import com.example.zlagodasb.sale.model.SaleModel;
import com.example.zlagodasb.sale.SaleService;
import com.example.zlagodasb.store_product.StoreProductService;
import com.example.zlagodasb.store_product.model.StoreProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
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
            for (SaleModel saleModel : data.getSaleModels()) {
                Sale entity = saleModel.toEntity();
                entity.setCheckNumber(saved.getCheckNumber());
                entity = saleService.create(entity);
            }
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

    @GetMapping("/getProductsByName")
    public ResponseEntity<List<Product>> getProductsByName(@RequestBody Map<String, String> data) {
        try {
            List<Product> result = productService.findAllByName(data.get("productName"));
            for (Product product : result)
                product.setStoreProducts(null);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    //getProductsWithCategoryNameSortedByName mutual

    @GetMapping("/getClientsBySurname")
    public ResponseEntity<List<CustomerCard>> getClientsBySurname(@RequestBody Map<String, String> data) {
        try {
            List<CustomerCard> result = customerCardService.findAllBySurname(data.get("custSurname"));
            for (CustomerCard customerCard : result)
                customerCard.setChecks(null);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/getAllChecksMadeToday")
    public ResponseEntity<List<CheckInfo>> getAllChecksMadeToday(@RequestBody Map<String, String> data) {
        try {
            String idCashier = data.get("idCashier");
            Date now = new Date(Calendar.getInstance().getTime().getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Date yesterday = new Date(calendar.getTime().getTime());
            List<CheckInfo> result = checkService.getChecksInfoOfCashierInPeriod(idCashier, yesterday, now);
            for (CheckInfo checkInfo : result)
                checkInfo.setSalesInfo(null);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/getAllChecksMadeInPeriod")
    public ResponseEntity<List<CheckInfo>> getAllChecksMadeInPeriod(@RequestBody Map<String, String> data) {
        try {
            String idCashier = data.get("idCashier");
            Date startDate = Date.valueOf(data.get("startDate"));
            Date endDate = Date.valueOf(data.get("endDate"));
            List<CheckInfo> result = checkService.getChecksInfoOfCashierInPeriod(idCashier, startDate, endDate);
            for (CheckInfo checkInfo : result)
                checkInfo.setSalesInfo(null);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/getCheckInfoByCheckNumber")
    public ResponseEntity<CheckInfo> getCheckInfoByCheckNumber(@RequestBody Map<String, String> data) {
        try {
            String checkNumber = data.get("checkNumber");
            CheckInfo result = checkService.getCheckInfoByCheckNumber(checkNumber);
            if(result == null) throw new Exception("Check with number not found");
            result.setSalesInfo(saleService.getSalesInfoByCheckNumber(result.getCheckNumber()));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }

    //getPromotionalProductsSorted mutual

    //getNonPromotionalProductsSorted mutual

    //getStoreProductInfoByUPC mutual

    @GetMapping("/getSelfInfo")
    public ResponseEntity<Employee> getSelfInfo(@RequestBody Map<String, String> data) {
        try {
            String idCashier = data.get("idCashier");
            Employee result = employeeService.findById(idCashier);
            if(result == null) throw new Exception("Cashier with id not found");
            result.setChecks(null);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }
}
