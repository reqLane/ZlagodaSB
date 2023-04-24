package com.example.zlagodasb.controller;

import com.example.zlagodasb.category.Category;
import com.example.zlagodasb.category.CategoryService;
import com.example.zlagodasb.check.Check;
import com.example.zlagodasb.check.CheckService;
import com.example.zlagodasb.customer_card.CustomerCard;
import com.example.zlagodasb.customer_card.CustomerCardService;
import com.example.zlagodasb.employee.Employee;
import com.example.zlagodasb.employee.EmployeeService;
import com.example.zlagodasb.product.Product;
import com.example.zlagodasb.product.ProductService;
import com.example.zlagodasb.store_product.StoreProduct;
import com.example.zlagodasb.store_product.StoreProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/general")
public class MutualController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final StoreProductService storeProductService;
    private final EmployeeService employeeService;
    private final CustomerCardService customerCardService;
    private final CheckService checkService;

    @Autowired
    public MutualController(CategoryService categoryService,
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

    @GetMapping("/getCategoryList")
    public ResponseEntity<List<String>> getCategoryList() {
        try {
            List<Category> categoryList = categoryService.findAll();
            List<String> entries = new ArrayList<>();
            for (Category category : categoryList) {
                entries.add(category.getCategoryName());
            }
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getProductList")
    public ResponseEntity<List<String>> getProductList() {
        try {
            List<Product> productList = productService.findAll();
            List<String> entries = new ArrayList<>();
            for (Product product : productList) {
                entries.add(product.getProductName());
            }
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getStoreProductList")
    public ResponseEntity<List<String>> getStoreProductList() {
        try {
            List<StoreProduct> storeProductList = storeProductService.findAll();
            List<String> entries = new ArrayList<>();
            for (StoreProduct storeProduct : storeProductList) {
                entries.add(storeProduct.getUPC());
            }
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getEmployeeList")
    public ResponseEntity<List<String>> getEmployeeList() {
        try {
            List<Employee> employeeList = employeeService.findAll();
            List<String> entries = new ArrayList<>();
            for (Employee employee : employeeList) {
                entries.add(employee.getIdEmployee());
            }
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getCustomerCardList")
    public ResponseEntity<List<String>> getCustomerCardList() {
        try {
            List<CustomerCard> customerCardList = customerCardService.findAll();
            List<String> entries = new ArrayList<>();
            for (CustomerCard customerCard : customerCardList) {
                entries.add(customerCard.getCardNumber());
            }
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getCheckList")
    public ResponseEntity<List<String>> getCheckList() {
        try {
            List<Check> checkList = checkService.findAll();
            List<String> entries = new ArrayList<>();
            for (Check check : checkList) {
                entries.add(check.getCheckNumber());
            }
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PatchMapping("/updateCustomerCard")
    public ResponseEntity<Boolean> updateCustomerCard(@RequestBody Map<String, String> data) {
        try {
            CustomerCard toUpdate = new CustomerCard();
            toUpdate.setCardNumber(data.get("cardNumber"));
            toUpdate.setCustSurname(data.get("custSurname"));
            toUpdate.setCustName(data.get("custName"));
            toUpdate.setCustPatronymic(data.get("custPatronymic"));
            toUpdate.setPhoneNumber(data.get("phoneNumber"));
            toUpdate.setCity(data.get("city"));
            toUpdate.setStreet(data.get("street"));
            toUpdate.setZipCode(data.get("zipCode"));
            toUpdate.setPercent(Integer.parseInt(data.get("percent")));
            customerCardService.update(toUpdate);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
}
