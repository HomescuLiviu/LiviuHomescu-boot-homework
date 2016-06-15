package io.fourfinanceit.homework.filters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
@WithMockUser(username="admin")
public class IPDailyFilterTest {

    private IPDailyFilter ipDailyFilter = new IPDailyFilter();
    private HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    private HttpServletRequest mockRequest = mock(HttpServletRequest.class);

    @Before
    public void setup(){
        mockResponse = mock(HttpServletResponse.class);
        mockRequest = mock(HttpServletRequest.class);
        ipDailyFilter = new IPDailyFilter();
    }

    @Test
    public void testLoaningOver3TimesFromTheSAmeIPResultsInAnErrorMessage() throws Exception {
        when(mockRequest.getParameter("first_name")).thenReturn("Joe");
        when(mockRequest.getParameter("last_name")).thenReturn("Pesci");
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn("ip-address-1");

        ipDailyFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());
        ipDailyFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());
        ipDailyFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());
        ipDailyFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());

        verify(mockResponse, times(1)).sendRedirect("/numberOfLoansError");

    }

    @Test
    public void testLoaning3TimesFromTheSameIPWorks() throws Exception {
        when(mockRequest.getParameter("first_name")).thenReturn("Joe");
        when(mockRequest.getParameter("last_name")).thenReturn("Pesci");
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn("ip-address-1");

        ipDailyFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());
        ipDailyFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());
        ipDailyFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());

        verify(mockResponse, never()).sendRedirect(anyString());

    }
}