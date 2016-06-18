package io.fourfinanceit.homework.filters;

import io.fourfinanceit.homework.data.LoanKeyBuilder;
import io.fourfinanceit.homework.data.entity.LoanAttempt;
import io.fourfinanceit.homework.data.service.LoanService;
import org.springframework.security.core.userdetails.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;


public class IPDailyFilter implements Filter {

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

        String ipAddress = getIpAddressFromRequest(request);

        String loanKey = LoanKeyBuilder.buildKey( userDetails.getUsername(), ipAddress);

        LoanAttempt attemptFromCache = getLoanAttempt(loanKey, userDetails.getUsername(), ipAddress);

        if (attemptFromCache.getNumberOfAccesses() > 2){
            System.out.println("------------ numberOfLoansError ");
            response.sendRedirect("/numberOfLoansError");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getIpAddressFromRequest(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");

        if (ipAddress == null || ipAddress.isEmpty()){
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public LoanAttempt getLoanAttempt(String loanKey, String userDetails, String ipAddress) {
        LoanAttempt attemptFromCache = loanService.getLoanAttemptsByKey(loanKey);

        System.out.println("------------- Filter key :" + loanKey);

        if (attemptFromCache == null){
            attemptFromCache = new LoanAttempt(userDetails, ipAddress, 1);
        }
        return attemptFromCache;
    }

    @Override
    public void destroy() {

    }
}
