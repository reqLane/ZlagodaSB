package com.example.zlagodasb.check;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class Check {
    public static final String TABLE_NAME = "Checks";

    private String checkNumber;
    private String idEmployee;
    private String cardNumber;
    private Timestamp printDate;
    private BigDecimal sumTotal;
    private BigDecimal vat;
}
