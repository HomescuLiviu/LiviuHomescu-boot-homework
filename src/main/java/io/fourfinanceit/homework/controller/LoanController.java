package io.fourfinanceit.homework.controller;

import io.fourfinanceit.homework.data.LoanKeyBuilder;
import io.fourfinanceit.homework.data.entity.Loan;
import io.fourfinanceit.homework.data.entity.LoanAttempt;
import io.fourfinanceit.homework.data.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.UUID;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class LoanController extends WebMvcConfigurerAdapter {

    @Autowired
    private LoanService loanService;


    @RequestMapping(value = "/loan", method = GET)
    public String loan(Model model) {
        model.addAttribute("loanForm",new Loan());
        return  "loanRequest";
    }

    @RequestMapping(value = "/loanRequest", method = POST)
    public String addLoan(HttpServletRequest request, @Valid Loan loan, BindingResult bindingResult, Model model) {
        User userDetails = (User) getContext().getAuthentication().getPrincipal();
        String ipAddress = getIPAddressFromRequest(request);

        if (bindingResult.hasErrors()) {
            return "loanRequest";
        }
        loan.setId(UUID.randomUUID().toString());
        loanService.storeLoan(loan);
        storeLoanAttempt(userDetails, ipAddress);
        model.addAttribute("loanId", loan.getId() );
        model.addAttribute("loanForm", loan);

        return  "loanResponse";
    }

    private String getIPAddressFromRequest(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.isEmpty()){
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    private void storeLoanAttempt(User userDetails, String ipAddress) {
        String loanKey = LoanKeyBuilder.buildKey( userDetails.getUsername(), ipAddress);

        LoanAttempt attemptFromCache = loanService.getLoanAttemptsByKey(loanKey);

        if (attemptFromCache == null){
            loanService.storeLoanAttempt(new LoanAttempt(userDetails.getUsername(), ipAddress, 1));
        } else{
            loanService.storeLoanAttempt(attemptFromCache);
        }
    }
}
