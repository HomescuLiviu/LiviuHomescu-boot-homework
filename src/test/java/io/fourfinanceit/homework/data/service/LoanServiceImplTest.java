package io.fourfinanceit.homework.data.service;

import io.fourfinanceit.homework.Application;
import io.fourfinanceit.homework.data.entity.Loan;
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
    }

    @Test
    public void testAddLoan() throws Exception {

        Loan loanToAdd = new Loan("1", "Joe", "John", 23.1, "USD", LocalDateTime.now());
        loanService.addLoan(loanToAdd);

        List<Loan> savedLoans = loanService.getLoansByName(loanToAdd.getFirstName(), loanToAdd.getLastName());

        assertFalse("Loan service loaded too many loans", savedLoans.size() > 1);
        assertFalse("Loan service did not load any loans", savedLoans.size() == 0);

    }
}