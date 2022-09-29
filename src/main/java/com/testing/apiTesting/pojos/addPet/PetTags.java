package com.testing.apiTesting.pojos.addPet;

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
