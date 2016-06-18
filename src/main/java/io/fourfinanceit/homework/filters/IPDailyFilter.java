package io.fourfinanceit.homework.filters;

import io.fourfinanceit.homework.data.LoanKeyBuilder;
import io.fourfinanceit.homework.data.entity.LoanAttempt;
import io.fourfinanceit.homework.data.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;


public class IPDailyFilter implements Filter {

    @Autowired
    private LoanService loanService;

    public IPDailyFilter(LoanService loanService) {
        this.loanService = loanService;
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

        if (attemptFromCache.getNumberOfAccesses() > 3){
            response.sendRedirect("/numberOfLoansError");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public LoanAttempt getLoanAttempt(String loanKey, String userDetails, String ipAddress) {
        LoanAttempt attemptFromCache = loanService.getLoanAttemptsByKey(loanKey);

        if (attemptFromCache == null){
            attemptFromCache = new LoanAttempt(userDetails, ipAddress, 0);
        }
        return attemptFromCache;
    }

    @Override
    public void destroy() {

    }
}
