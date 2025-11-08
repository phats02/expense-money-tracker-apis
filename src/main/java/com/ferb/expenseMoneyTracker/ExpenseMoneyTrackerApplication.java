package com.ferb.expenseMoneyTracker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ExpenseMoneyTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseMoneyTrackerApplication.class, args);
        log.info("🚀 App successfully started. Apis docs: http://localhost:8080/swagger-ui/index.html");
	}

}
