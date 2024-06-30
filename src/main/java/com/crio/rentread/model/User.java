package com.crio.rentread.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public User(Long id, String email, String password, String firstname, String lastname, Long roleId, Boolean admin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.roleId = roleId;
        this.admin = admin;
    }

    public User() {
        //TODO Auto-generated constructor stub
    }

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private Long roleId;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean admin = false;

}
