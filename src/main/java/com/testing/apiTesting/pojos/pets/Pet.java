package com.testing.apiTesting.pojos.pets;

import com.github.javafaker.Faker;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Data
@XmlRootElement(name = "Pet")
public class Pet {

    private String id;
    private PetCategory category;
    private String name;
    private List<String> photoUrls;
    private List<PetTags> tags;
    private String status;


    public Pet() {
        Faker faker = new Faker(new Locale("en-GB"));
        this.category = new PetCategory(String.valueOf(faker.number().randomDigit()), "dog");
        this.name = faker.dog().name();
        this.photoUrls = new ArrayList<>() {{
            add("http://puppy_dog_pictures.jpg");
        }};
        this.tags = new ArrayList<>() {{
            add(new PetTags(faker.number().randomDigit(), "doggies"));
        }};
        this.status = "available";

    }

}
