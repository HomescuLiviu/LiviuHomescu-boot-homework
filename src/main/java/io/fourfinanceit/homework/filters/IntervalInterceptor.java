package io.fourfinanceit.homework.filters;


import io.fourfinanceit.homework.time.Clock;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class IntervalInterceptor extends HandlerInterceptorAdapter {

    private final Clock clock;

    public IntervalInterceptor(Clock clock) {
        this.clock = clock;
    }


    @Override
    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler) throws IOException, ServletException {
        if (clock.now().getHour() < 6){
             return false;
        }
        return true;
    }


}
