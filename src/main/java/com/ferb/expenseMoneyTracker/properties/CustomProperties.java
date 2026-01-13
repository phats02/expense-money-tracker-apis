package com.ferb.expenseMoneyTracker.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class CustomProperties {
    @Getter
    private static String jwtSecret;
    @Getter
    private static String jwtRefreshSecret;

    public void setJwtSecret(String jwtSecret) {
        CustomProperties.jwtSecret = jwtSecret;
    }

    public void setJwtRefreshSecret(String jwtRefreshSecret) {
        CustomProperties.jwtRefreshSecret = jwtRefreshSecret;
    }

}
