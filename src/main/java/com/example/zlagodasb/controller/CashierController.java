package com.example.zlagodasb.controller;

import com.example.zlagodasb.category.CategoryService;
import com.example.zlagodasb.check.Check;
import com.example.zlagodasb.check.model.CheckModel;
import com.example.zlagodasb.check.CheckService;
import com.example.zlagodasb.customer_card.CustomerCardService;
import com.example.zlagodasb.employee.EmployeeService;
import com.example.zlagodasb.product.ProductService;
import com.example.zlagodasb.sale.Sale;
import com.example.zlagodasb.sale.model.SaleModel;
import com.example.zlagodasb.sale.SaleService;
import com.example.zlagodasb.store_product.StoreProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
