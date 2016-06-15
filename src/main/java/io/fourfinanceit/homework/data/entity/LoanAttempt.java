package io.fourfinanceit.homework.data.entity;

import io.fourfinanceit.homework.data.LoanKeyBuilder;

public class LoanAttempt {

    private Loan loan;
    private String firstName;
    private String lastName;
    private String loanKey;
    private String IPaddress;
    private int numberOfAccesses;

    public LoanAttempt(String firstName, String lastName, String IPaddress, int numberOfAccesses) {
        this.loan = loan;
        this.loanKey = LoanKeyBuilder.buildKey( firstName, lastName, IPaddress);
        this.IPaddress = IPaddress;
        this.numberOfAccesses = numberOfAccesses;
    }

    public String getLoanKey() {
        return loanKey;
    }

    public int getNumberOfAccesses() {
        return numberOfAccesses;
    }

    public void setNumberOfAccesses(int numberOfAccesses) {
        this.numberOfAccesses = numberOfAccesses;
    }
}
