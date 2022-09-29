package com.testing.apiTesting.pojos.pets;

import lombok.Data;

@Data
public class PetCategory {

    private int id;
    private String name;

    public PetCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
