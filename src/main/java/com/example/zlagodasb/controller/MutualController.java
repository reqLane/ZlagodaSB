package com.example.zlagodasb.controller;

import com.example.zlagodasb.category.Category;
import com.example.zlagodasb.category.CategoryService;
import com.example.zlagodasb.check.Check;
import com.example.zlagodasb.check.CheckService;
import com.example.zlagodasb.customer_card.CustomerCard;
import com.example.zlagodasb.customer_card.model.CustomerCardModel;
import com.example.zlagodasb.customer_card.CustomerCardService;
import com.example.zlagodasb.employee.Employee;
import com.example.zlagodasb.employee.EmployeeService;
import com.example.zlagodasb.employee.model.EmployeeInfo;
import com.example.zlagodasb.product.Product;
import com.example.zlagodasb.product.ProductService;
import com.example.zlagodasb.product.model.ProductInfo;
import com.example.zlagodasb.store_product.StoreProduct;
import com.example.zlagodasb.store_product.StoreProductService;
import com.example.zlagodasb.store_product.model.StoreProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.zlagodasb.util.Utils.isNullOrEmpty;
import static com.example.zlagodasb.util.Utils.parseIdFrom;

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

    //AUTHENTICATION

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody Map<String, String> data) {
        try {
            String idEmployee = data.get("idEmployee");
            String password = data.get("password");
            Map<String, String> response = employeeService.authenticate(idEmployee, password);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("authenticated", "true");
            response.put("emplRole", null);
            response.put("idEmployee", null);
            response.put("exception", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    //INFORMATION LISTS

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
            List<String> entries = new ArrayList<>();
            for (StoreProductInfo storeProductInfo : storeProductService.findAllInfo()) {
                entries.add(storeProductInfo.getProductName() +
                        '(' + storeProductInfo.getUPC() + ')');
            }
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getStoreProductListPresent")
    public ResponseEntity<List<String>> getStoreProductListPresent() {
        try {
            List<String> entries = new ArrayList<>();
            for (StoreProductInfo storeProductInfo : storeProductService.findAllPresentInfo()) {
                entries.add(storeProductInfo.getProductName() +
                        '(' + storeProductInfo.getUPC() + ')');
            }
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getEmployeeList")
    public ResponseEntity<List<String>> getEmployeeList() {
        try {
            List<String> entries = new ArrayList<>();
            for (EmployeeInfo employeeInfo : employeeService.findAll()) {
                entries.add(employeeInfo.getEmplFullName() +
                        '(' + employeeInfo.getIdEmployee() + ')');
            }
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    @GetMapping("/getCashiersList")
    public ResponseEntity<List<String>> getCashiersList() {
        try {
            List<EmployeeInfo> employeeList = employeeService.findCashiersSortedByEmplSurname();
            List<String> entries = new ArrayList<>();
            for (EmployeeInfo employeeInfo : employeeList) {
                entries.add(employeeInfo.getEmplFullName() +
                        '(' + employeeInfo.getIdEmployee() + ')');
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
                String surname = customerCard.getCustSurname();
                String name = customerCard.getCustName();
                String patronymic = customerCard.getCustPatronymic();
                entries.add(surname + " " + name + (patronymic != null ? (" " + patronymic) : "") +
                        '(' + customerCard.getCardNumber() + ')');
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

    //DATA OPERATIONS

    @PostMapping("/createCustomerCard")
    public ResponseEntity<Boolean> createCustomerCard(@RequestBody CustomerCardModel data) {
        try {
            if(isNullOrEmpty(data.getCustPatronymic())) data.setCustPatronymic(null);
            if(isNullOrEmpty(data.getCity())) data.setCity(null);
            if(isNullOrEmpty(data.getStreet())) data.setStreet(null);
            if(isNullOrEmpty(data.getZipCode())) data.setZipCode(null);

            CustomerCard saved = customerCardService.create(data);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PatchMapping("/updateCustomerCard")
    public ResponseEntity<Boolean> updateCustomerCard(@RequestBody Map<String, String> data) {
        try {
            if(isNullOrEmpty(data.get("custPatronymic"))) data.put("custPatronymic", null);
            if(isNullOrEmpty(data.get("city"))) data.put("city", null);
            if(isNullOrEmpty(data.get("street"))) data.put("street", null);
            if(isNullOrEmpty(data.get("zipCode"))) data.put("zipCode", null);

            CustomerCard toUpdate = new CustomerCard();
            toUpdate.setCardNumber(parseIdFrom(data.get("cardNumber")));
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

    //FUNCTIONAL OPERATIONS

    @GetMapping("/getProductsSortedByName")
    public ResponseEntity<List<Map<String, Object>>> getProductsSortedByName() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (ProductInfo productInfo : productService.findAllSortedByName()) {
                result.add(productInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/getCustomersSortedBySurname")
    public ResponseEntity<List<Map<String, Object>>> getCustomersSortedBySurname() {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (CustomerCard customerCard : customerCardService.findAllCardsSortedBySurname()) {
                result.add(customerCard.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getProductsWithCategoryNameSortedByName")
    public ResponseEntity<List<Map<String, Object>>> getProductsWithCategoryNameSortedByName(@RequestBody Map<String, String> data) {
        try {
            Category category = categoryService.findByCategoryName(data.get("categoryName"));
            if(category == null) throw new Exception("Category with name not found");

            List<Map<String, Object>> result = new ArrayList<>();
            for (ProductInfo productInfo : productService.findAllWithCategorySortedByName(category.getCategoryNumber())) {
                result.add(productInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getStoreProductInfoByUPC")
    public ResponseEntity<Map<String, Object>> getStoreProductInfoByUPC(@RequestBody Map<String, String> data) {
        try {
            StoreProductInfo result = storeProductService.getInfoByUPC(parseIdFrom(data.get("UPC")));
            return ResponseEntity.ok(result.toMap());
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }

    @PostMapping("/getPromotionalProductsSorted")
    public ResponseEntity<List<Map<String, Object>>> getPromotionalProductsSorted(@RequestBody Map<String, String> data) {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (StoreProductInfo storeProductInfo : storeProductService.findAllPromotionalSortedBy(data.get("sortBy"))) {
                result.add(storeProductInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/getNonPromotionalProductsSorted")
    public ResponseEntity<List<Map<String, Object>>> getNonPromotionalProductsSorted(@RequestBody Map<String, String> data) {
        try {
            List<Map<String, Object>> result = new ArrayList<>();
            for (StoreProductInfo storeProductInfo : storeProductService.findAllNonPromotionalSortedBy(data.get("sortBy"))) {
                result.add(storeProductInfo.toMap());
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
}
