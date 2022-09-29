package com.testing.apiTesting.pojos.pets;

import lombok.Data;

@Data
public class ErrorMessage {

    String code;
    String type;
    String message;
}
