package com.mibemolsoft.realworldcrud.domain;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
// User is a KEYWORD in H2, an easy workaround is to pluralize the entity
// TODO: Don't pluralise the entity, instead configure H2 to NOT use USER as a keyword
public class Users {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    // TODO: Password needs to be encrypted and salted
    @Column(nullable = false)
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
