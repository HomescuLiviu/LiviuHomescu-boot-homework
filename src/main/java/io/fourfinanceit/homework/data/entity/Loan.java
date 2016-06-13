package io.fourfinanceit.homework.data.entity;

import java.time.LocalDateTime;

public class Loan {

    String id;
    String firstName;
    String lastName;
    Double value;
    String currency;
    LocalDateTime term;

    public Loan() {
    }

    public Loan(String id, String firstName, String lastName, Double value, String currency, LocalDateTime term) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.value = value;
        this.currency = currency;
        this.term = term;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getTerm() {
        return term;
    }

    public void setTerm(LocalDateTime term) {
        this.term = term;
    }
}
