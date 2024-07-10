package com.example.demo.exception;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {
    private int status;
    private String message;


}
