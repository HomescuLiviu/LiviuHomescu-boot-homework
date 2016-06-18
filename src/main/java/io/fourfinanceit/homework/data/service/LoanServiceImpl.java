package io.fourfinanceit.homework.data.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.fourfinanceit.homework.data.LoanKeyBuilder;
import io.fourfinanceit.homework.data.entity.Loan;
import io.fourfinanceit.homework.data.entity.LoanAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class LoanServiceImpl implements LoanService {

    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ScheduledExecutorService cacheCleaner = Executors.newScheduledThreadPool(10);

    LoadingCache<String, LoanAttempt> entityCache = CacheBuilder.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build(
                    new CacheLoader<String, LoanAttempt>() {
                        @Override
                        public LoanAttempt load(String key) throws Exception {
                            return null;
                        }
                    }
            );

    {
        cacheCleaner.scheduleAtFixedRate( () -> entityCache.cleanUp(), 0, 3, TimeUnit.HOURS );
    }



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
        return entityCache.getIfPresent(loanKey);
    }

    public void storeLoanAttempt(LoanAttempt loanAttempt) {

        LoanAttempt attemptFromCache = getLoanAttempt(loanAttempt);

        if (attemptFromCache == null){
            loanAttempt.setNumberOfAccesses(1);
            entityCache.put(LoanKeyBuilder.buildKey(loanAttempt.getUserName(), loanAttempt.getIPaddress()), loanAttempt);
        } else {
            attemptFromCache.setNumberOfAccesses( 1 + attemptFromCache.getNumberOfAccesses());
            entityCache.put(LoanKeyBuilder.buildKey(attemptFromCache.getUserName(), attemptFromCache.getIPaddress()), attemptFromCache);
        }
    }

    private LoanAttempt getLoanAttempt(LoanAttempt loanAttempt) {
        return entityCache.getIfPresent(LoanKeyBuilder.buildKey(loanAttempt.getUserName(), loanAttempt.getIPaddress()));
    }

}
