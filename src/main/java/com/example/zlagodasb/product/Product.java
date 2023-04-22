package com.example.zlagodasb.product;

import com.example.zlagodasb.category.Category;
import com.example.zlagodasb.supply.Supply;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_product;

    @Column(nullable = false)
    private String product_name;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private String characteristics;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<Supply> supplies = new ArrayList<>();
}
