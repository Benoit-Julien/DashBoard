package com.epitech.dashboard;

import org.springframework.data.annotation.Id;

public class User {
    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String pseudo;
    private String password;

    public User(
            final String firstName,
            final String lastName,
            final String pseudo,
            final String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pseudo = pseudo;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
