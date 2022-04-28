package ru.seven.tech.account.data.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class Person {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private int age;
}
