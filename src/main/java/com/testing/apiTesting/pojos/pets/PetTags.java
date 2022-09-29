package com.testing.apiTesting.pojos.pets;

import lombok.Data;

@Data
public class PetTags {

    private int id;
    private String dog;

    public PetTags(int id, String dog) {
        this.id = id;
        this.dog = dog;
    }
}
