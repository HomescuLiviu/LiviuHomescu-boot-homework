package io.fourfinanceit.homework;

import io.fourfinanceit.homework.data.service.LoanService;
import io.fourfinanceit.homework.data.service.LoanServiceImpl;
import io.fourfinanceit.homework.filters.IPDailyFilter;
import io.fourfinanceit.homework.filters.IntervalFilter;
import io.fourfinanceit.homework.time.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Override
	public void run(String... strings) throws Exception {
		log.info("Creating tables");
		jdbcTemplate.execute("CREATE TABLE if not exists loans(" +
				"id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255), amount NUMBER, currency VARCHAR(255) ,  term DATETIME)");

		jdbcTemplate.execute("CREATE TABLE if not exists loan_attempts(" +
				"id SERIAL, loanKey VARCHAR(255), user_name VARCHAR(255), ip_address VARCHAR(255), number_of_accesses NUMBER)");
	}


	@Bean
	public IntervalFilter intervalFilter() {
		return new IntervalFilter(clock());
	}

	@Bean
	public FilterRegistrationBean intervalFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean(intervalFilter());
		registration.addUrlPatterns("/loanRequest");
		return registration;
	}

	@Bean
	public Clock clock(){
		return new Clock();
	}


	@Bean
	public FilterRegistrationBean dailyFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean(dailyFilter());
		registration.addUrlPatterns("/loanRequest");
		return registration;
	}

	@Bean
	public IPDailyFilter dailyFilter() {
		return new IPDailyFilter(loanService());
	}


	@Bean
	public LoanService loanService() {
		return new LoanServiceImpl();
	}

}
