package com.example.zlagodasb.controller;

import com.example.zlagodasb.category.CategoryService;
import com.example.zlagodasb.check.CheckService;
import com.example.zlagodasb.customer_card.CustomerCardService;
import com.example.zlagodasb.employee.EmployeeService;
import com.example.zlagodasb.product.ProductService;
import com.example.zlagodasb.store_product.StoreProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cashier")
public class CashierController {
    private CategoryService categoryService;
    private ProductService productService;
    private StoreProductService storeProductService;
    private EmployeeService employeeService;
    private CustomerCardService customerCardService;
    private CheckService checkService;

    @Autowired
    public CashierController(CategoryService categoryService,
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
}
