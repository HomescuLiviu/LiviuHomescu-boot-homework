package io.fourfinanceit.homework.data.service;

import io.fourfinanceit.homework.data.LoanKeyBuilder;
import io.fourfinanceit.homework.data.entity.Loan;
import io.fourfinanceit.homework.data.entity.LoanAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void storeLoan(Loan loan) {
        String dateString = formatter.format(loan.getTerm());
        jdbcTemplate.execute(
                String.format("insert into loans (first_name, last_name, amount, term) values ('%s', '%s', '%s', '%s');", loan.getFirstName(), loan.getLastName(), loan.getAmount(), dateString)
         );
    }

    public List<Loan> getLoansByName(String firstName, String lastName){
        return jdbcTemplate.query(
                "SELECT * FROM loans WHERE first_name = ? and last_name = ? ", new Object[] { firstName, lastName },
                (rs, rowNum) -> { return new Loan(rs.getString("id"), rs.getString("first_name"), rs.getString("last_name"),
                                    Double.valueOf(rs.getString("amount")), rs.getString("currency"),
                                    LocalDateTime.parse(rs.getString("term").substring(0, rs.getString("term").length() - 2),formatter));
                });
    }

    public LoanAttempt getLoanAttemptsByKey(String loanKey){
        final LoanAttempt[] result = new LoanAttempt[1];
        jdbcTemplate.query(
                "SELECT * FROM loan_attempts WHERE loanKey = ?", new Object[] { loanKey },
                (rs, rowNum) -> result[0] = new LoanAttempt(rs.getString("user_name"), rs.getString("ip_address"), Integer.parseInt(rs.getString("number_of_accesses"))));

        return result[0];
    }

    public void storeLoanAttempt(LoanAttempt loanAttempt) {
        jdbcTemplate.execute(
                String.format("insert into loan_attempts (loanKey, user_name, ip_address, number_of_accesses) values ('%s', '%s', '%s', '%s');",
                                LoanKeyBuilder.buildKey(loanAttempt.getUserName(), loanAttempt.getIPaddress()),
                                loanAttempt.getUserName(), loanAttempt.getIPaddress(), loanAttempt.getNumberOfAccesses()));

    }

}
