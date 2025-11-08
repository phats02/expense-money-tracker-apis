package com.example.expenseMoneyTracker.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class CustomProperties {
    private String jwtSecret;
}
