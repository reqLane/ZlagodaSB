package com.example.zlagodasb.sale;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Sale")
public class Sale {
    @EmbeddedId
    private SalePK PPK;

    @Column(nullable = false)
    private Integer productNumber;

    @Column(nullable = false)
    private BigDecimal sellingPrice;
}
