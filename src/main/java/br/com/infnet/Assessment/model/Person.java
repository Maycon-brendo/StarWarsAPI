package br.com.infnet.Assessment.model;

import lombok.Data;

import java.util.List;

@Data
public class Person {
    private Long id;
    private String name;
    private int age;
    private List<String> hobbies;

    public Person(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    public Person() {
    }
}
