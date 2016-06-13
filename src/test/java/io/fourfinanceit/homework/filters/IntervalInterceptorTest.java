package io.fourfinanceit.homework.filters;

import io.fourfinanceit.homework.time.Clock;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalTime;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IntervalInterceptorTest {

    Clock clock = mock(Clock.class);
    IntervalInterceptor intervalInterceptor = new IntervalInterceptor(clock);

    HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    Object mockHandler = mock(Object.class);

    @Test
    public void testInterceptCallsThatAreBefore6Am() throws Exception {
        when(clock.now()).thenReturn(LocalTime.of(5, 59, 59));

        assertFalse("Interval Interceptor has allowed a call before 6 a.m", intervalInterceptor.preHandle(mockRequest, mockResponse, mockHandler));

    }

    @Test
    public void testInterceptorAllowsCallsThatAreBefore0Am() throws Exception {
        when(clock.now()).thenReturn(LocalTime.of(23, 59, 59));

        assertTrue("Interval Interceptor has not allowed a call before 0 a.m", intervalInterceptor.preHandle(mockRequest, mockResponse, mockHandler));

    }

    @Test
    public void testInterceptorAllowsCallsThatAreAfter6Am() throws Exception {
        when(clock.now()).thenReturn(LocalTime.of(6, 0, 2));

        assertTrue("Interval Interceptor has not allowed a call after 6 a.m", intervalInterceptor.preHandle(mockRequest, mockResponse, mockHandler));

    }
}