package io.fourfinanceit.homework.controller;

import io.fourfinanceit.homework.data.entity.Loan;
import io.fourfinanceit.homework.data.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.validation.Valid;

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
    public String addLoan(@Valid Loan loan, BindingResult bindingResult, Model model) {
        model.addAttribute("loanForm", loan);
        if (bindingResult.hasErrors()) {
            return "loanRequest";
        }
        loanService.storeLoan(loan);
        return  "loanResponse";
    }
}
