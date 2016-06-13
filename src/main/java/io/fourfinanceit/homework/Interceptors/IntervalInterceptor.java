package io.fourfinanceit.homework.Interceptors;


import io.fourfinanceit.homework.time.Clock;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IntervalInterceptor extends HandlerInterceptorAdapter {

    private final Clock clock;

    public IntervalInterceptor(Clock clock) {
        this.clock = clock;
    }


    @Override
    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler) throws IOException, ServletException {
        if (clock.now().getHour() < 6 ){

            System.out.println(" -------------------------- "+ clock.now().getHour());
             return false;
        }


        return true;
    }


}
