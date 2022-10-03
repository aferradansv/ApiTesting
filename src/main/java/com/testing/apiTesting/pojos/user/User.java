package com.testing.apiTesting.pojos.user;

import com.github.javafaker.Faker;
import lombok.Data;

import java.util.Locale;

@Data
public class User {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private int userStatus;

    public User(){
        Faker faker = new Faker(new Locale("en-GB"));
        this.username = faker.name().username();
        this.firstName = faker.name().firstName();
        this.lastName = faker.name().lastName();
        this.email = faker.internet().emailAddress(username);
        this.password = faker.internet().password();
        this.phone = faker.phoneNumber().cellPhone();
        this.userStatus = 2;
    }
}
