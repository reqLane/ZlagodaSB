package com.example.zlagodasb.check;

import com.example.zlagodasb.sale.SaleModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class CheckModel {
    private String idEmployee;
    private String cardNumber;
    private Date printDate;

    private List<SaleModel> saleModels;

    public Check toEntity() {
        Check entity = new Check();
        entity.setIdEmployee(idEmployee);
        entity.setCardNumber(cardNumber);
        entity.setPrintDate(printDate);
        return entity;
    }
}
