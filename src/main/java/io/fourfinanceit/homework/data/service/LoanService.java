package io.fourfinanceit.homework.data.service;

import io.fourfinanceit.homework.data.entity.Loan;
import io.fourfinanceit.homework.data.entity.LoanAttempt;

import java.util.List;

public interface LoanService {

    void storeLoan(Loan loan);

    List<Loan> getLoansByName(String firstName, String lastName);

    LoanAttempt getLoanAttemptsByKey(String loanKey);

    void storeLoanAttempt(LoanAttempt loanAttempt);

}
