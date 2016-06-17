package io.fourfinanceit.homework.data.entity;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class Loan {

    private String id;

    @NotEmpty(message = "Please provide a first name for this loan")
    private String firstName;

    @NotEmpty(message = "Please provide a last name for this loan")
    private String lastName;


    @NotNull(message = "Please provide an amount for this loan")
    private Double amount;

    @NotNull(message = "Please provide a currency for this loan")
    private String currency;

    @NotNull(message = "Please provide a term for this loan")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime term;

    public Loan() {
    }

    public Loan(String id, String firstName, String lastName, Double amount, String currency, LocalDateTime term) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.amount = amount;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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
