package com.example.zlagodasb.controller;

import com.example.zlagodasb.category.Category;
import com.example.zlagodasb.category.model.CategoryModel;
import com.example.zlagodasb.category.CategoryService;
import com.example.zlagodasb.check.CheckService;
import com.example.zlagodasb.check.model.CheckInfo;
import com.example.zlagodasb.customer_card.CustomerCard;
import com.example.zlagodasb.customer_card.CustomerCardService;
import com.example.zlagodasb.employee.Employee;
import com.example.zlagodasb.employee.model.EmployeeModel;
import com.example.zlagodasb.employee.EmployeeService;
import com.example.zlagodasb.product.Product;
import com.example.zlagodasb.product.model.ProductModel;
import com.example.zlagodasb.product.ProductService;
import com.example.zlagodasb.sale.SaleService;
import com.example.zlagodasb.store_product.StoreProduct;
import com.example.zlagodasb.store_product.model.StoreProductModel;
import com.example.zlagodasb.store_product.StoreProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.zlagodasb.util.Utils.isNullOrEmpty;

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

            Employee saved = employeeService.create(data);
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
            toUpdate.setUPC(data.get("UPC"));
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

            employeeService.update(data);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @DeleteMapping("/deleteCategory")
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
    @DeleteMapping("/deleteProduct")
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
    @DeleteMapping("/deleteStoreProduct")
    public ResponseEntity<Boolean> deleteStoreProduct(@RequestBody Map<String, String> data) {
        try {
            storeProductService.deleteById(data.get("UPC"));
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @DeleteMapping("/deleteEmployee")
    public ResponseEntity<Boolean> deleteEmployee(@RequestBody Map<String, String> data) {
        try {
            employeeService.deleteById(data.get("idEmployee"));
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @DeleteMapping("/deleteCustomerCard")
    public ResponseEntity<Boolean> deleteCustomerCard(@RequestBody Map<String, String> data) {
        try {
            customerCardService.deleteById(data.get("cardNumber"));
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @DeleteMapping("/deleteCheck")
    public ResponseEntity<Boolean> deleteCheck(@RequestBody Map<String, String> data) {
        try {
            checkService.deleteById(data.get("checkNumber"));
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    //FUNCTIONAL OPERATIONS

    @GetMapping("/getCategoryReport")
    public ResponseEntity<List<Category>> getCategoryReport() {
        try {
            List<Category> result = categoryService.findAll();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getProductReport")
    public ResponseEntity<List<Product>> getProductReport() {
        try {
            List<Product> result = productService.findAll();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getStoreProductReport")
    public ResponseEntity<List<StoreProduct>> getStoreProductReport() {
        try {
            List<StoreProduct> result = storeProductService.findAll();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getEmployeeReport")
    public ResponseEntity<List<Employee>> getEmployeeReport() {
        try {
            List<Employee> result = employeeService.findAll();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getCustomerCardReport")
    public ResponseEntity<List<CustomerCard>> getCustomerCardReport() {
        try {
            List<CustomerCard> result = customerCardService.findAll();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/getEmployeesSortedBySurname")
    public ResponseEntity<List<Employee>> getEmployeesSortedBySurname() {
        try {
            List<Employee> result = employeeService.findAllSortedByEmplSurname();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/getCashiersSortedBySurname")
    public ResponseEntity<List<Employee>> getCashiersSortedBySurname() {
        try {
            List<Employee> result = employeeService.findCashiersSortedByEmplSurname();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    //getCustomersSortedBySurname mutual

    @GetMapping("/getCategoriesSortedByName")
    public ResponseEntity<List<Category>> getCategoriesSortedByName() {
        try {
            List<Category> result = categoryService.findAllSortedByName();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    //getProductsSortedByName mutual

    @GetMapping("/getStoreProductsSortedByAmount")
    public ResponseEntity<List<StoreProduct>> getStoreProductsSortedByNumber() {
        try {
            List<StoreProduct> result = storeProductService.findAllSortedByAmount();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getEmployeesBySurname")
    public ResponseEntity<List<Employee>> getEmployeesBySurname(@RequestBody Map<String, String> data) {
        try {
            List<Employee> result = employeeService.findAllBySurname(data.get("surname"));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getCustomersWithPercentSortedBySurname")
    public ResponseEntity<List<CustomerCard>> getCustomersWithPercentSortedBySurname(@RequestBody Map<String, String> data) {
        try {
            List<CustomerCard> result = customerCardService.findAllWithPercentSortedBySurname(Integer.parseInt(data.get("percent")));
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
    public ResponseEntity<List<CheckInfo>> getChecksInfoOfCashierInPeriod(@RequestBody Map<String, String> data) {
        try {
            String idCashier = data.get("idCashier");
            Date startDate = Date.valueOf(data.get("startDate"));
            Date endDate = Date.valueOf(data.get("endDate"));
            List<CheckInfo> result = checkService.getChecksInfoOfCashierInPeriod(idCashier, startDate, endDate);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getAllChecksInfoInPeriod")
    public ResponseEntity<List<CheckInfo>> getAllChecksInfoInPeriod(@RequestBody Map<String, String> data) {
        try {
            Date startDate = Date.valueOf(data.get("startDate"));
            Date endDate = Date.valueOf(data.get("endDate"));
            List<CheckInfo> result = checkService.getAllChecksInfoInPeriod(startDate, endDate);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getTotalIncomeFromChecksOfCashierInPeriod")
    public ResponseEntity<BigDecimal> getTotalIncomeFromChecksOfCashierInPeriod(@RequestBody Map<String, String> data) {
        try {
            String idCashier = data.get("idCashier");
            Date startDate = Date.valueOf(data.get("startDate"));
            Date endDate = Date.valueOf(data.get("endDate"));
            BigDecimal result = checkService.getTotalIncomeFromChecksOfCashierInPeriod(idCashier, startDate, endDate);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(BigDecimal.valueOf(-1));
        }
    }

    @PostMapping("/getTotalIncomeFromChecksInPeriod")
    public ResponseEntity<BigDecimal> getTotalIncomeFromChecksInPeriod(@RequestBody Map<String, String> data) {
        try {
            Date startDate = Date.valueOf(data.get("startDate"));
            Date endDate = Date.valueOf(data.get("endDate"));
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
            Date startDate = Date.valueOf(data.get("startDate"));
            Date endDate = Date.valueOf(data.get("endDate"));

            Product product = productService.findByProductName(productName);
            if(product == null) throw new Exception("Product with name not found");

            Long result = productService.getTotalAmountOfProductSoldInPeriod(product.getIdProduct(), startDate, endDate);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok((long)-1);
        }
    }
}
