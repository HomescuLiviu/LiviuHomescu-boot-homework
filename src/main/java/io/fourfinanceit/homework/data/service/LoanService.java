package io.fourfinanceit.homework.data.service;

import io.fourfinanceit.homework.data.entity.Loan;

import java.util.List;

public interface LoanService {

    void addLoan(Loan loan);

    public List<Loan> getLoansByName(String firstName, String lastName);


}
