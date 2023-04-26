package com.example.zlagodasb.check.model;

import com.example.zlagodasb.sale.model.SaleInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Check ID", checkNumber);
        map.put("Employee", employeeFullName);
        map.put("Customer", customerFullName);
        map.put("Print date", printDate);
        map.put("Total sum", sumTotal);
        map.put("VAT", vat);
        List<Map<String, Object>> salesInfoMaps = new ArrayList<>();
        for (SaleInfo saleInfo : salesInfo) {
            salesInfoMaps.add(saleInfo.toMap());
        }
        map.put("salesInfo", salesInfoMaps);
        return map;
    }
}
