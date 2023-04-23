package com.example.zlagodasb.check;

import com.example.zlagodasb.sale.Sale;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Check {
    public static final String TABLE_NAME = "Checks";

    private String checkNumber;
    private String idEmployee;
    private String cardNumber;
    private Date printDate;
    private BigDecimal sumTotal;
    private BigDecimal vat;

    private List<Sale> sales = new ArrayList<>();
}
