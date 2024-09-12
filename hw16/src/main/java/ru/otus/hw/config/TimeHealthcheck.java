package ru.otus.hw.config;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class TimeHealthcheck extends AbstractHealthIndicator {

    private final LocalTime openAfter = LocalTime.of(7, 0, 0);

    private final LocalTime closeAfter = LocalTime.of(20, 0, 0);


    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        var status = processStatus();

        builder.status(status).build();
    }

    private Status processStatus() {
        var now = LocalTime.now();
        if (isValidTime(now)) {
            return Status.UP;
        }
        return Status.DOWN;

    }

    private boolean isValidTime(LocalTime time) {
        return time.isAfter(openAfter) && time.isBefore(closeAfter);
    }
}
