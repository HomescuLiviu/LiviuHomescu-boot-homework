package io.fourfinanceit.homework.data.service;

import io.fourfinanceit.homework.Application;
import io.fourfinanceit.homework.data.LoanKeyBuilder;
import io.fourfinanceit.homework.data.entity.Loan;
import io.fourfinanceit.homework.data.entity.LoanAttempt;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= Application.class, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
@WebAppConfiguration
public class LoanServiceImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LoanService loanService;

   @Before
   public void setup(){
        jdbcTemplate.execute("CREATE TABLE if not exists loans(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255), amount NUMBER, currency VARCHAR(255) ,  term DATETIME)");
        jdbcTemplate.execute("CREATE TABLE if not exists loan_attempts(" +
               "id SERIAL, loanKey VARCHAR(255), user_name VARCHAR(255), ip_address VARCHAR(255), number_of_accesses NUMBER)");
   }

    @Test
    public void testStoreLoan() throws Exception {

        Loan loanToAdd = new Loan("1", "Joe", "John", 23.1, "USD", LocalDateTime.now());
        loanService.storeLoan(loanToAdd);

        List<Loan> savedLoans = loanService.getLoansByName(loanToAdd.getFirstName(), loanToAdd.getLastName());

        assertFalse("Loan service loaded too many loans", savedLoans.size() > 1);
        assertFalse("Loan service did not load any loans", savedLoans.size() == 0);

    }

    @Test
    public void testStoreLoanAttempt() throws Exception {

        LoanAttempt loanAttemptToAdd = new LoanAttempt("johnJ", "123.23.21.21.test", 4);
        loanService.storeLoanAttempt(loanAttemptToAdd);

        LoanAttempt savedLoanAttempt = loanService.getLoanAttemptsByKey(LoanKeyBuilder.buildKey(loanAttemptToAdd.getUserName(), loanAttemptToAdd.getIPaddress()));

        assertTrue("Loan service did not store the correct loan attempt", savedLoanAttempt.equals(loanAttemptToAdd));
        assertFalse("Loan service did not load any loans", savedLoanAttempt == null);

    }

    @Test
    public void testCanupdateLoanAttempt() throws Exception {

        LoanAttempt loanAttemptToAdd = new LoanAttempt("johnJ", "123.23.21.21.test", 1);
        loanService.storeLoanAttempt(loanAttemptToAdd);

        loanAttemptToAdd.setNumberOfAccesses(2);
        loanService.storeLoanAttempt(loanAttemptToAdd);

        LoanAttempt savedLoanAttempt = loanService.getLoanAttemptsByKey(LoanKeyBuilder.buildKey(loanAttemptToAdd.getUserName(), loanAttemptToAdd.getIPaddress()));

        assertTrue("Loan service did not update the loan attempt properly", savedLoanAttempt.equals(loanAttemptToAdd));
        assertFalse("Loan service did not load any loans", savedLoanAttempt == null);

    }
}