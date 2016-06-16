package io.fourfinanceit.homework.filters;

import io.fourfinanceit.homework.time.Clock;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalTime;

import static org.mockito.Mockito.*;

public class IntervalFilterTest {

    Clock clock = mock(Clock.class);
    IntervalFilter intervalFilter = new IntervalFilter(clock);

    HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    Object mockHandler = mock(Object.class);

    @Test
    public void testInterceptCallsThatAreBefore6AmAndOverAmount() throws Exception {
        when(clock.now()).thenReturn(LocalTime.of(5, 59, 59));
        when(mockRequest.getParameter("amount")).thenReturn("200.1");

        intervalFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());
        verify(mockResponse, times(1)).sendRedirect("/maximumAmountAtNightError");

    }

    @Test
    public void testFilterAllowsCallsThatAreBefore0Am() throws Exception {
        when(clock.now()).thenReturn(LocalTime.of(23, 59, 59));
        when(mockRequest.getParameter("amount")).thenReturn("300.1");

        intervalFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());
        verify(mockResponse, never()).sendRedirect("/maximumAmountAtNightError");

    }

    @Test
    public void testFilterAllowsCallsThatAreAfter6Am() throws Exception {
        when(clock.now()).thenReturn(LocalTime.of(6, 0, 2));
        when(mockRequest.getParameter("amount")).thenReturn("300.1");

        intervalFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());
        verify(mockResponse, never()).sendRedirect("/maximumAmountAtNightError");

    }

    @Test
    public void testFilterAllowsCallsThatAreBeore6AmAndUnderAmount() throws Exception {
        when(clock.now()).thenReturn(LocalTime.of(5, 59, 59));
        when(mockRequest.getParameter("amount")).thenReturn("199");

        intervalFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());
        verify(mockResponse, never()).sendRedirect("/maximumAmountAtNightError");

    }

    @Test
    public void testFilterAllowsCallsThatAreBeore6AmAndOfExactAmount() throws Exception {
        when(clock.now()).thenReturn(LocalTime.of(5, 59, 59));
        when(mockRequest.getParameter("amount")).thenReturn("200");

        intervalFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());
        verify(mockResponse, never()).sendRedirect("/maximumAmountAtNightError");

    }
}