package com.example.zlagodasb.supply;

import com.example.zlagodasb.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Store_Product")
public class Supply {
    @Id
    private String UPC;

    @Column(nullable = false)
    private BigDecimal sellingPrice;

    @Column(nullable = false)
    private Integer productsNumber;

    @Column(nullable = false)
    private Date expirationDate;

    @Column(nullable = false)
    private boolean promotionalProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
}
