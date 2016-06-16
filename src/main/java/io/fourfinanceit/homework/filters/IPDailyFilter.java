package io.fourfinanceit.homework.filters;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.fourfinanceit.homework.data.LoanKeyBuilder;
import io.fourfinanceit.homework.data.entity.LoanAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;


public class IPDailyFilter implements Filter {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ScheduledExecutorService cacheCleaner = Executors.newScheduledThreadPool(10);

    LoadingCache<String, LoanAttempt> entityCache = CacheBuilder.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build(
                    new CacheLoader<String, LoanAttempt>() {
                        @Override
                        public LoanAttempt load(String key) throws Exception {
                            return getLoanAttemptFromDatabase(key);
                        }
                    }
            );

    public IPDailyFilter() {
    }

    private LoanAttempt getLoanAttemptFromDatabase(String key) {
        final LoanAttempt[] result = new LoanAttempt[1];
        jdbcTemplate.query(
                "SELECT * FROM loan_attempts WHERE loanKey = ?", new Object[] { key },
                (rs, rowNum) -> result[0] = new LoanAttempt(rs.getString("user_name"), rs.getString("ip_address"), 1));

        return result[0];
    }

    {
        cacheCleaner.scheduleAtFixedRate( () -> entityCache.cleanUp(), 0, 3, TimeUnit.HOURS );
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        User userDetails = (User) getContext().getAuthentication().getPrincipal();

        userDetails.getUsername();
        String ipAddress = request.getHeader("x-forwarded-for");

        if (ipAddress == null || ipAddress.isEmpty()){
            ipAddress = request.getRemoteAddr();
        }

        String loanKey = LoanKeyBuilder.buildKey( userDetails.getUsername(), ipAddress);

        LoanAttempt attemptFromCache = getLoanAttempt(loanKey);

        if (attemptFromCache != null ){
            if (attemptFromCache.getNumberOfAccesses() > 2){
                response.sendRedirect("/numberOfLoansError");
            } else {
                attemptFromCache.setNumberOfAccesses( 1 + attemptFromCache.getNumberOfAccesses());
                entityCache.put(loanKey, attemptFromCache);
            }
        } else {
            entityCache.put(loanKey, new LoanAttempt(userDetails.getUsername(), ipAddress, 1));
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public LoanAttempt getLoanAttempt(String loanKey) {
        return entityCache.getIfPresent(loanKey);
    }

    @Override
    public void destroy() {

    }
}
