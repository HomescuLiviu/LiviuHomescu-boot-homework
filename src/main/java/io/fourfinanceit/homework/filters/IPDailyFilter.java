package io.fourfinanceit.homework.filters;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.fourfinanceit.homework.data.LoanKeyBuilder;
import io.fourfinanceit.homework.data.entity.LoanAttempt;
import io.fourfinanceit.homework.data.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private LoanService loanService;

    private ScheduledExecutorService cacheCleaner = Executors.newScheduledThreadPool(10);

    LoadingCache<String, LoanAttempt> entityCache = CacheBuilder.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build(
                    new CacheLoader<String, LoanAttempt>() {
                        @Override
                        public LoanAttempt load(String key) throws Exception {
                            return loanService.getLoanAttemptsByKey(key);
                        }
                    }
            );

    public IPDailyFilter() {
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

        String ipAddress = request.getHeader("x-forwarded-for");

        if (ipAddress == null || ipAddress.isEmpty()){
            ipAddress = request.getRemoteAddr();
        }

        String loanKey = LoanKeyBuilder.buildKey( userDetails.getUsername(), ipAddress);

        LoanAttempt attemptFromCache = getLoanAttempt(loanKey, userDetails.getUsername(), ipAddress);

        if (attemptFromCache.getNumberOfAccesses() > 2){
            response.sendRedirect("/numberOfLoansError");
        } else {
            attemptFromCache.setNumberOfAccesses( 1 + attemptFromCache.getNumberOfAccesses());
           // loanService.storeLoanAttempt(attemptFromCache);
            entityCache.put(LoanKeyBuilder.buildKey(attemptFromCache.getUserName(), attemptFromCache.getIPaddress()), attemptFromCache);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public LoanAttempt getLoanAttempt(String loanKey, String userDetails, String ipAddress) {
        LoanAttempt attemptFromCache = entityCache.getIfPresent(loanKey);

        if (attemptFromCache == null){
            attemptFromCache = new LoanAttempt(userDetails, ipAddress, 0);
        }
        return attemptFromCache;
    }

    @Override
    public void destroy() {

    }
}
