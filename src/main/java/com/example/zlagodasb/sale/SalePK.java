package com.example.zlagodasb.sale;

import com.example.zlagodasb.check.Check;
import com.example.zlagodasb.supply.Supply;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Embeddable
public class SalePK implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    private Supply supply;

    @ManyToOne(fetch = FetchType.LAZY)
    private Check check;
}
