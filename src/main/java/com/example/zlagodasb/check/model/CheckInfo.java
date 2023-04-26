package com.example.zlagodasb.check.model;

import com.example.zlagodasb.sale.model.SaleInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CheckInfo {
    private String checkNumber;
    private String employeeFullName;
    private String customerFullName;
    private Timestamp printDate;
    private BigDecimal sumTotal;
    private BigDecimal vat;

    List<SaleInfo> salesInfo = new ArrayList<>();
}
