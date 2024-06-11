package com.phildev.front.mls.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Pattern(regexp ="^[A-Z][a-z]+$", message="Firstname should start with capital letter followed by lowercase letter(s)")
    @Column(name = "firstname", nullable = false)
    private String firstName;


    @Pattern(regexp ="^[A-Z][a-z]+$", message="Lastname should start with capital letter followed by lowercase letter(s)")
    @Column(name = "lastname", nullable = false)
    private String lastName;


    @Column(name = "societe", nullable = false)
    private String societe;


    @Column(name = "role", nullable = false)
    private String role;

    @Pattern(regexp="^[\\w.-]+@[a-zA-Z\\d.-]+.[a-zA-Z]{2,6}$", message="Invalid email format")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

}
