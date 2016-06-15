package io.fourfinanceit.homework;

import io.fourfinanceit.homework.data.entity.Loan;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class Application implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... strings) throws Exception {

		log.info("Creating tables");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		jdbcTemplate.execute("DROP TABLE loans IF EXISTS");
		jdbcTemplate.execute("CREATE TABLE loans(" +
				"id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255), amount NUMBER, currency VARCHAR(255) ,  term TIMESTAMP)");

		log.info("Querying for customer records where first_name = 'Josh':");
		jdbcTemplate.query(
				"SELECT id, first_name, last_name FROM loans WHERE first_name = ?", new Object[] { "Josh" },
				(rs, rowNum) -> new Loan(rs.getString("id"), rs.getString("first_name"), rs.getString("last_name"), Double.valueOf(rs.getString("amount")), rs.getString("currency"), LocalDateTime.parse(rs.getString("term"),formatter) )
		).forEach(customer -> log.info(customer.toString()));
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
		return new IPDailyFilter();
	}

}
