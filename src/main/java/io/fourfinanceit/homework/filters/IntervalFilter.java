package io.fourfinanceit.homework.filters;


import io.fourfinanceit.homework.time.Clock;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IntervalFilter implements Filter {

    private final Clock clock;

    public IntervalFilter(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Double amount = Double.parseDouble(request.getParameter("amount"));
        if (clock.now().getHour() < 6 && amount > 200){
            response.sendRedirect("/maximumAmountAtNightError");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
