package com.testing.apiTesting.pojos.pets;

import lombok.Data;

@Data
public class PetCategory {

    private String id;
    private String name;

    public PetCategory() {
    }

    public PetCategory(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
