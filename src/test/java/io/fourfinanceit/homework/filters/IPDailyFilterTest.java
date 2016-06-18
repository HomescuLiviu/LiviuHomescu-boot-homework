package io.fourfinanceit.homework.filters;

import io.fourfinanceit.homework.Application;
import io.fourfinanceit.homework.data.entity.LoanAttempt;
import io.fourfinanceit.homework.data.service.LoanService;
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
@ContextConfiguration(classes= Application.class, loader = AnnotationConfigContextLoader.class)
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
@WithMockUser(username="admin")
public class IPDailyFilterTest {

    private LoanService loanService = mock(LoanService.class);

    private IPDailyFilter ipDailyFilter = new IPDailyFilter(loanService);
    private HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    private HttpServletRequest mockRequest = mock(HttpServletRequest.class);

    @Before
    public void setup(){
        mockResponse = mock(HttpServletResponse.class);
        mockRequest = mock(HttpServletRequest.class);
        ipDailyFilter = new IPDailyFilter(loanService);
    }

    @Test
    public void testLoaningOver3TimesFromTheSAmeIPResultsInAnErrorMessage() throws Exception {

        LoanAttempt testLoanAttempt = new LoanAttempt("Joe", "TestIP", 4);

        when(loanService.getLoanAttemptsByKey(anyString())).thenReturn(testLoanAttempt);

        ipDailyFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());

        verify(mockResponse, times(1)).sendRedirect("/numberOfLoansError");

    }

    @Test
    public void testLoaning3TimesFromTheSameIPWorks() throws Exception {

        LoanAttempt testLoanAttempt = new LoanAttempt("Joe", "TestIP", 2);

        when(loanService.getLoanAttemptsByKey(anyString())).thenReturn(testLoanAttempt);

        ipDailyFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());

        verify(mockResponse, never()).sendRedirect(anyString());

    }
    @Test
    public void testLoaningLessThan3TimesFromTheSameIPWorks() throws Exception {

        LoanAttempt testLoanAttempt = new LoanAttempt("Joe", "TestIP", 1);

        when(loanService.getLoanAttemptsByKey(anyString())).thenReturn(testLoanAttempt);

        ipDailyFilter.doFilter(mockRequest, mockResponse, new MockFilterChain());

        verify(mockResponse, never()).sendRedirect(anyString());

    }
}