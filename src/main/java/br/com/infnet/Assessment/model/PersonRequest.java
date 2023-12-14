package br.com.infnet.Assessment.model;

import lombok.Data;

import java.util.List;

@Data
public class PersonRequest {
    private String name;
    private int age;
    private List<String> hobbies;
}