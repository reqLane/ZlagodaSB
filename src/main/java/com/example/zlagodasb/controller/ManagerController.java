package com.example.zlagodasb.controller;

import com.example.zlagodasb.category.Category;
import com.example.zlagodasb.category.CategoryModel;
import com.example.zlagodasb.category.CategoryService;
import com.example.zlagodasb.check.Check;
import com.example.zlagodasb.check.CheckModel;
import com.example.zlagodasb.check.CheckService;
import com.example.zlagodasb.customer_card.CustomerCard;
import com.example.zlagodasb.customer_card.CustomerCardModel;
import com.example.zlagodasb.customer_card.CustomerCardService;
import com.example.zlagodasb.employee.Employee;
import com.example.zlagodasb.employee.EmployeeModel;
import com.example.zlagodasb.employee.EmployeeService;
import com.example.zlagodasb.product.Product;
import com.example.zlagodasb.product.ProductModel;
import com.example.zlagodasb.product.ProductService;
import com.example.zlagodasb.sale.Sale;
import com.example.zlagodasb.sale.SaleModel;
import com.example.zlagodasb.sale.SaleService;
import com.example.zlagodasb.store_product.StoreProduct;
import com.example.zlagodasb.store_product.StoreProductModel;
import com.example.zlagodasb.store_product.StoreProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

@RestController
@RequestMapping("/manager")
public class ManagerController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final StoreProductService storeProductService;
    private final EmployeeService employeeService;
    private final CustomerCardService customerCardService;
    private final CheckService checkService;

    @Autowired
    public ManagerController(CategoryService categoryService,
                             ProductService productService,
                             StoreProductService storeProductService,
                             EmployeeService employeeService,
                             CustomerCardService customerCardService,
                             CheckService checkService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.storeProductService = storeProductService;
        this.employeeService = employeeService;
        this.customerCardService = customerCardService;
        this.checkService = checkService;
    }

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
            ProductModel productModel = new ProductModel();
            productModel.setCategoryNumber(categoryService.findByCategoryName(data.get("categoryName")).getCategoryNumber());
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
            StoreProductModel storeProductModel = new StoreProductModel();
            storeProductModel.setUPC(data.get("UPC"));
            storeProductModel.setIdProduct(productService.findByProductName(data.get("productName")).getIdProduct());
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
            Employee saved = employeeService.create(data);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @PostMapping("/createCustomerCard")
    public ResponseEntity<Boolean> createCustomerCard(@RequestBody CustomerCardModel data) {
        try {
            CustomerCard saved = customerCardService.create(data);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PatchMapping ("/updateCategory")
    public ResponseEntity<Boolean> updateCategory(@RequestBody Map<String, String> data) {
        try {
            Category toUpdate = new Category();
            toUpdate.setCategoryNumber(categoryService.findByCategoryName(data.get("currentName")).getCategoryNumber());
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
            Product toUpdate = new Product();
            toUpdate.setIdProduct(productService.findByProductName(data.get("currentName")).getIdProduct());
            toUpdate.setCategoryNumber(categoryService.findByCategoryName(data.get("categoryName")).getCategoryNumber());
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
            StoreProduct toUpdate = new StoreProduct();
            toUpdate.setUPC(data.get("UPC"));
            toUpdate.setIdProduct(productService.findByProductName(data.get("productName")).getIdProduct());
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
            employeeService.update(data);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @DeleteMapping("/deleteCategory")
    public ResponseEntity<Boolean> deleteCategory(@RequestBody Map<String, String> data) {
        try {
            categoryService.deleteById(categoryService.findByCategoryName(data.get("categoryName")).getCategoryNumber());
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    @DeleteMapping("/deleteProduct")
    public ResponseEntity<Boolean> deleteProduct(@RequestBody Map<String, String> data) {
        try {
            productService.deleteById(productService.findByProductName(data.get("productName")).getIdProduct());
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
}
