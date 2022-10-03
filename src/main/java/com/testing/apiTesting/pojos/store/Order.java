package com.testing.apiTesting.pojos.store;

import lombok.Data;

import java.time.Instant;

@Data
public class Order {

    private int id;
    private int petId;
    private int quantity;
    private String shipDate;
    private String status;
    private boolean complete;

    public Order() {
        this.petId = 5;
        this.quantity = 10;
        this.shipDate = Instant.now().toString();
        this.status = "placed";
        this.complete = false;
    }
}
