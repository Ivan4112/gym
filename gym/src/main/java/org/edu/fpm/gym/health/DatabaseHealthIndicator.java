package org.edu.fpm.gym.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if(connection != null) {
                return Health.up().withDetail("db", "Database is connected").build();
            } else {
                return Health.down().withDetail("db", "No database connected").build();
            }
        }catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
