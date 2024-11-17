package org.edu.fpm.gym.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.util.function.Supplier;

@Component
public class TraineeRepositoryMetrics {

    private final MeterRegistry meterRegistry;
    private final Timer dbConnectionTimer;
    private final Timer queryExecutionTimer;
    private final Timer dataLoadTimer;

    @Autowired
    public TraineeRepositoryMetrics(MeterRegistry meterRegistry) {

        this.meterRegistry = meterRegistry;

        this.dbConnectionTimer = meterRegistry.timer("db.connection.time");

        this.queryExecutionTimer = meterRegistry.timer("db.query.execution.time");

        this.dataLoadTimer = meterRegistry.timer("db.data.fetch.time");

        meterRegistry.gauge("db.cpu.load", this, TraineeRepositoryMetrics::getCpuLoad);
    }

    public <T> T measureDbConnectionTime(Supplier<T> action) {
        return dbConnectionTimer.record(action);
    }

    public <T> T measureQueryExecutionTime(Supplier<T> action) {
        return queryExecutionTimer.record(action);
    }

    public <T> T measureDataLoadTime(Supplier<T> action) {
        return dataLoadTimer.record(action);
    }
    public double getCpuLoad() {
        return ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
    }
}
