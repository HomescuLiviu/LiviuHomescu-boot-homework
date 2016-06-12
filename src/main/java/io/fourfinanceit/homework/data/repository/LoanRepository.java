package io.fourfinanceit.homework.data.repository;

import io.fourfinanceit.homework.data.entity.Loan;
import org.springframework.data.repository.CrudRepository;


public interface LoanRepository extends CrudRepository<Loan, Long> {

}
