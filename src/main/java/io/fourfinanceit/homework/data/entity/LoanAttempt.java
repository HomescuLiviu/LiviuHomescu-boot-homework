package io.fourfinanceit.homework.data.entity;

public class LoanAttempt {

    private String userName;
    private String IPaddress;
    private int numberOfAccesses;

    public LoanAttempt(String userName, String IPaddress, int numberOfAccesses) {
        this.userName = userName;
        this.IPaddress = IPaddress;
        this.numberOfAccesses = numberOfAccesses;
    }

    public int getNumberOfAccesses() {
        return numberOfAccesses;
    }

    public void setNumberOfAccesses(int numberOfAccesses) {
        this.numberOfAccesses = numberOfAccesses;
    }

    public String getUserName() {
        return userName;
    }

    public String getIPaddress() {
        return IPaddress;
    }

    @Override
    public boolean equals(Object obj) {


        return getNumberOfAccesses() == ((LoanAttempt)obj).getNumberOfAccesses() &&
               getUserName().equals( ((LoanAttempt)obj).getUserName()) &&
               getIPaddress().equals( ((LoanAttempt)obj).getIPaddress());

    }
}
