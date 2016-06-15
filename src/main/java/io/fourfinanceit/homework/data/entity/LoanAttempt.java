package io.fourfinanceit.homework.data.entity;

public class LoanAttempt {

    private String IPaddress;
    private int numberOfAccesses;

    public LoanAttempt(String userName, String IPaddress, int numberOfAccesses) {
        this.IPaddress = IPaddress;
        this.numberOfAccesses = numberOfAccesses;
    }

    public int getNumberOfAccesses() {
        return numberOfAccesses;
    }

    public void setNumberOfAccesses(int numberOfAccesses) {
        this.numberOfAccesses = numberOfAccesses;
    }
}
