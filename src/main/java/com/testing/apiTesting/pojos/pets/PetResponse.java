package com.testing.apiTesting.pojos.pets;

import lombok.Data;

import java.util.List;

@Data
public class PetResponse {

    private String id;
    private PetCategory category;
    private String name;
    private List<String> photoUrls;
    private List<PetTags> tags;
    private String status;

}
