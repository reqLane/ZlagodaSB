package com.example.zlagodasb.controller;

import com.example.zlagodasb.category.Category;
import com.example.zlagodasb.category.model.CategoryModel;
import com.example.zlagodasb.category.CategoryService;
import com.example.zlagodasb.check.Check;
import com.example.zlagodasb.check.CheckService;
import com.example.zlagodasb.check.model.CheckInfo;
import com.example.zlagodasb.customer_card.CustomerCard;
import com.example.zlagodasb.customer_card.CustomerCardService;
import com.example.zlagodasb.customer_card.model.CustomerCardInfo;
import com.example.zlagodasb.employee.model.EmployeeInfo;
import com.example.zlagodasb.employee.model.EmployeeModel;
import com.example.zlagodasb.employee.EmployeeService;
import com.example.zlagodasb.product.Product;
import com.example.zlagodasb.product.model.ProductInfo;
import com.example.zlagodasb.product.model.ProductModel;
import com.example.zlagodasb.product.ProductService;
import com.example.zlagodasb.sale.SaleService;
import com.example.zlagodasb.store_product.StoreProduct;
import com.example.zlagodasb.store_product.model.StoreProductInfo;
import com.example.zlagodasb.store_product.model.StoreProductModel;
import com.example.zlagodasb.store_product.StoreProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.zlagodasb.util.Utils.isNullOrEmpty;
import static com.example.zlagodasb.util.Utils.parseIdFrom;

@RestController
@RequestMapping("/manager")
public class ManagerController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final StoreProductService storeProductService;
    private final EmployeeService employeeService;
    private final CustomerCardService customerCardService;
    private final CheckService checkService;
    private final SaleService saleService;

    @Autowired
    public ManagerController(CategoryService categoryService,
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

    @PostMapping("/createCategory")
    public ResponseEntity<Boolean> createCategory(@RequestBody CategoryModel data) {
        try {
            Category saved = categoryService.create(data);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PostMapping("/createProduct")
    public ResponseEntity<Boolean> createProduct(@RequestBody Map<String, String> data) {
        try {
            Category category = categoryService.findByCategoryName(data.get("categoryName"));
            if(category == null) throw new Exception("Category with name not found");

            ProductModel productModel = new ProductModel();
            productModel.setCategoryNumber(category.getCategoryNumber());
            productModel.setProductName(data.get("productName"));
            productModel.setManufacturer(data.get("manufacturer"));
            productModel.setCharacteristics(data.get("characteristics"));
            Product saved = productService.create(productModel);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PostMapping("/createStoreProduct")
    public ResponseEntity<Boolean> createStoreProduct(@RequestBody Map<String, String> data) {
        try {
            Product product = productService.findByProductName(data.get("productName"));
            if(product == null) throw new Exception("Product with name not found");

            StoreProductModel storeProductModel = new StoreProductModel();
            storeProductModel.setUPC(data.get("UPC"));
            storeProductModel.setIdProduct(product.getIdProduct());
            storeProductModel.setSellingPrice(BigDecimal.valueOf(Double.parseDouble(data.get("sellingPrice"))));
            storeProductModel.setProductsNumber(Integer.parseInt(data.get("productsNumber")));
            storeProductModel.setExpirationDate(Date.valueOf(data.get("expirationDate"))); //yyyy-[m]m-[d]d (2023-04-24)
            storeProductModel.setPromotionalProduct(Boolean.parseBoolean(data.get("promotionalProduct")));
            StoreProduct saved = storeProductService.create(storeProductModel);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PostMapping("/createEmployee")
    public ResponseEntity<Boolean> createEmployee(@RequestBody EmployeeModel data) {
        try {
            if(isNullOrEmpty(data.getEmplPatronymic())) data.setEmplPatronymic(null);

            EmployeeInfo saved = employeeService.create(data);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PatchMapping ("/updateCategory")
    public ResponseEntity<Boolean> updateCategory(@RequestBody Map<String, String> data) {
        try {
            Category category = categoryService.findByCategoryName(data.get("currentName"));
            if(category == null) throw new Exception("Category with name not found");

            Category toUpdate = new Category();
            toUpdate.setCategoryNumber(category.getCategoryNumber());
            toUpdate.setCategoryName(data.get("newName"));
            categoryService.update(toUpdate);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PatchMapping ("/updateProduct")
    public ResponseEntity<Boolean> updateProduct(@RequestBody Map<String, String> data) {
        try {
            Product product = productService.findByProductName(data.get("currentName"));
            Category category = categoryService.findByCategoryName(data.get("categoryName"));
            if(product == null) throw new Exception("Product with name not found");
            if(category == null) throw new Exception("Category with name not found");

            Product toUpdate = new Product();
            toUpdate.setIdProduct(product.getIdProduct());
            toUpdate.setCategoryNumber(category.getCategoryNumber());
            toUpdate.setProductName(data.get("newName"));
            toUpdate.setManufacturer(data.get("manufacturer"));
            toUpdate.setCharacteristics(data.get("characteristics"));
            productService.update(toUpdate);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PatchMapping("/updateStoreProduct")
    public ResponseEntity<Boolean> updateStoreProduct(@RequestBody Map<String, String> data) {
        try {
            Product product = productService.findByProductName(data.get("productName"));
            if(product == null) throw new Exception("Product with name not found");

            StoreProduct toUpdate = new StoreProduct();
            toUpdate.setUPC(parseIdFrom(data.get("UPC")));
            toUpdate.setIdProduct(product.getIdProduct());
            toUpdate.setSellingPrice(BigDecimal.valueOf(Double.parseDouble(data.get("sellingPrice"))));
            toUpdate.setProductsNumber(Integer.parseInt(data.get("productsNumber")));
            toUpdate.setExpirationDate(Date.valueOf(data.get("expirationDate"))); //yyyy-mm-dd (2023-04-24)
            toUpdate.setPromotionalProduct(Boolean.parseBoolean(data.get("promotionalProduct")));
            storeProductService.update(toUpdate);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PatchMapping("/updateEmployee")
    public ResponseEntity<Boolean> updateEmployee(@RequestBody EmployeeModel data) {
        try {
            if(isNullOrEmpty(data.getEmplPatronymic())) data.setEmplPatronymic(null);

            data.setIdEmployee(parseIdFrom(data.getIdEmployee()));

            employeeService.update(data);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PatchMapping("/updatePromoStoreProductList")
    public ResponseEntity<Boolean> updatePromoStoreProductList() {
        try {
            storeProductService.updatePromoList();
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PutMapping("/deleteCategory")
    public ResponseEntity<Boolean> deleteCategory(@RequestBody Map<String, String> data) {
        try {
            Category category = categoryService.findByCategoryName(data.get("categoryName"));
            if(category == null) throw new Exception("Category with name not found");

            categoryService.deleteById(category.getCategoryNumber());
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PutMapping("/deleteProduct")
    public ResponseEntity<Boolean> deleteProduct(@RequestBody Map<String, String> data) {
        try {
            Product product = productService.findByProductName(data.get("productName"));
            if(product == null) throw new Exception("Product with name not found");

            productService.deleteById(product.getIdProduct());
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PutMapping("/deleteStoreProduct")
    public ResponseEntity<Boolean> deleteStoreProduct(@RequestBody Map<String, String> data) {
        try {
            StoreProduct storeProduct = storeProductService.findById(parseIdFrom(data.get("UPC")));
            if(storeProduct == null) throw new Exception("StoreProduct with UPC not found");

            storeProductService.deleteById(storeProduct.getUPC());
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PutMapping("/deleteEmployee")
    public ResponseEntity<Boolean> deleteEmployee(@RequestBody Map<String, String> data) {
        try {
            EmployeeInfo employee = employeeService.findById(parseIdFrom(data.get("idEmployee")));
            if(employee == null) throw new Exception("Employee with idEmployee not found");

            employeeService.deleteById(employee.getIdEmployee());
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PutMapping("/deleteCustomerCard")
    public ResponseEntity<Boolean> deleteCustomerCard(@RequestBody Map<String, String> data) {
        try {
            CustomerCard customerCard = customerCardService.findById(parseIdFrom(data.get("cardNumber")));
            if(customerCard == null) throw new Exception("CustomerCard with cardNumber not found");

            customerCardService.deleteById(customerCard.getCardNumber());
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PutMapping("/deleteCheck")
    public ResponseEntity<Boolean> deleteCheck(@RequestBody Map<String, String> data) {
        try {
            Check check = checkService.findById(data.get("checkNumber"));
            if(check == null) throw new Exception("Check with checkNumber not found");

            checkService.deleteById(check.getCheckNumber());
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PutMapping("/deleteOldChecks")
    public ResponseEntity<Boolean> deleteOldChecks() {
        try {
            List<Check> oldChecks = checkService.deleteOldChecks();
            if(oldChecks.isEmpty()) throw new Exception("Old checks not found");

            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    //FUNCTIONAL OPERATIONS

    @GetMapping("/getCategoryReport")
    public ResponseEntity<List<Map<String, Object>>> getCategoryReport() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (Category category : categoryService.findAll()) {
                result.add(category.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getProductReport")
    public ResponseEntity<List<Map<String, Object>>> getProductReport() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (ProductInfo productInfo : productService.findAllInfo()) {
                result.add(productInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getStoreProductReport")
    public ResponseEntity<List<Map<String, Object>>> getStoreProductReport() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (StoreProductInfo storeProductInfo : storeProductService.findAllInfo()) {
                result.add(storeProductInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getEmployeeReport")
    public ResponseEntity<List<Map<String, Object>>> getEmployeeReport() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (EmployeeInfo employeeInfo : employeeService.findAll()) {
                result.add(employeeInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getCustomerCardReport")
    public ResponseEntity<List<Map<String, Object>>> getCustomerCardReport() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (CustomerCard customerCard : customerCardService.findAll()) {
                result.add(customerCard.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/getEmployeesSortedBySurname")
    public ResponseEntity<List<Map<String, Object>>> getEmployeesSortedBySurname() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (EmployeeInfo employeeInfo : employeeService.findAllSortedByEmplSurname()) {
                result.add(employeeInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/getCashiersSortedBySurname")
    public ResponseEntity<List<Map<String, Object>>> getCashiersSortedBySurname() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (EmployeeInfo employeeInfo : employeeService.findCashiersSortedByEmplSurname()) {
                result.add(employeeInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    //getCustomersSortedBySurname mutual

    @GetMapping("/getCategoriesSortedByName")
    public ResponseEntity<List<Map<String, Object>>> getCategoriesSortedByName() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (Category category : categoryService.findAllSortedByName()) {
                result.add(category.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    //getProductsSortedByName mutual

    @GetMapping("/getStoreProductsSortedByAmount")
    public ResponseEntity<List<Map<String, Object>>> getStoreProductsSortedByAmount() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (StoreProductInfo storeProductInfo : storeProductService.findAllSortedByAmount()) {
                result.add(storeProductInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getEmployeesBySurname")
    public ResponseEntity<List<Map<String, Object>>> getEmployeesBySurname(@RequestBody Map<String, String> data) {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (EmployeeInfo employeeInfo : employeeService.findAllBySurname(data.get("surname"))) {
                result.add(employeeInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getCustomersWithPercentSortedBySurname")
    public ResponseEntity<List<Map<String, Object>>> getCustomersWithPercentSortedBySurname(@RequestBody Map<String, String> data) {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (CustomerCard customerCard : customerCardService.findAllWithPercentSortedBySurname(Integer.parseInt(data.get("percent")))) {
                result.add(customerCard.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    //getProductsWithCategoryNameSortedByName mutual

    //getStoreProductInfoByUPC mutual

    //getPromotionalProductsSorted mutual

    //getNonPromotionalProductsSorted mutual

    @PostMapping("/getChecksInfoOfCashierInPeriod")
    public ResponseEntity<List<Map<String, Object>>> getChecksInfoOfCashierInPeriod(@RequestBody Map<String, String> data) {
        try {
            String idCashier = parseIdFrom(data.get("idCashier"));

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

    @PostMapping("/getAllChecksInfoInPeriod")
    public ResponseEntity<List<Map<String, Object>>> getAllChecksInfoInPeriod(@RequestBody Map<String, String> data) {
        try {
            Timestamp startDate = new Timestamp(Date.valueOf(data.get("startDate")).getTime());
            Timestamp endDate = new Timestamp(Date.valueOf(data.get("endDate")).getTime());

            List<Map<String, Object>> result = new ArrayList<>();
            for (CheckInfo checkInfo : checkService.getAllChecksInfoInPeriod(startDate, endDate)) {
                result.add(checkInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getTotalIncomeFromChecksOfCashierInPeriod")
    public ResponseEntity<BigDecimal> getTotalIncomeFromChecksOfCashierInPeriod(@RequestBody Map<String, String> data) {
        try {
            String idCashier = data.get("idCashier");

            Timestamp startDate = new Timestamp(Date.valueOf(data.get("startDate")).getTime());
            Timestamp endDate = new Timestamp(Date.valueOf(data.get("endDate")).getTime());

            BigDecimal result = checkService.getTotalIncomeFromChecksOfCashierInPeriod(idCashier, startDate, endDate);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(BigDecimal.valueOf(-1));
        }
    }

    @PostMapping("/getTotalIncomeFromChecksInPeriod")
    public ResponseEntity<BigDecimal> getTotalIncomeFromChecksInPeriod(@RequestBody Map<String, String> data) {
        try {
            Timestamp startDate = new Timestamp(Date.valueOf(data.get("startDate")).getTime());
            Timestamp endDate = new Timestamp(Date.valueOf(data.get("endDate")).getTime());

            BigDecimal result = checkService.getTotalIncomeFromChecksInPeriod(startDate, endDate);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(BigDecimal.valueOf(-1));
        }
    }

    @PostMapping("/getTotalAmountOfProductSoldInPeriod")
    public ResponseEntity<Long> getTotalAmountOfProductSoldInPeriod(@RequestBody Map<String, String> data) {
        try {
            String productName = data.get("productName");

            Timestamp startDate = new Timestamp(Date.valueOf(data.get("startDate")).getTime());
            Timestamp endDate = new Timestamp(Date.valueOf(data.get("endDate")).getTime());

            Product product = productService.findByProductName(productName);
            if(product == null) throw new Exception("Product with name not found");

            Long result = productService.getTotalAmountOfProductSoldInPeriod(product.getIdProduct(), startDate, endDate);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok((long)-1);
        }
    }

    //COMPLEX QUERIES

    @GetMapping("/getClientsWhoBoughtMoreThanAverage")
    public ResponseEntity<List<Map<String, Object>>> getClientsWhoBoughtMoreThanAverage() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (CustomerCardInfo customerCardInfo : customerCardService.getClientsWhoBoughtMoreThanAverage()) {
                result.add(customerCardInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/getClientsWhoBoughtAllProducts")
    public ResponseEntity<List<Map<String, Object>>> getClientsWhoBoughtAllProducts() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (CustomerCard customerCard : customerCardService.getClientsWhoBoughtAllProducts()) {
                result.add(customerCard.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/getCashiersWhoSoldMoreThanAverage")
    public ResponseEntity<List<Map<String, Object>>> getCashiersWhoSoldMoreThanAverage() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (EmployeeInfo employeeInfo : employeeService.getCashiersWhoSoldMoreThanAverage()) {
                result.add(employeeInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/getCashiersWhoSoldAllProducts")
    public ResponseEntity<List<Map<String, Object>>> getCashiersWhoSoldAllProducts() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (EmployeeInfo employeeInfo : employeeService.getCashiersWhoSoldAllProducts()) {
                result.add(employeeInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
}
