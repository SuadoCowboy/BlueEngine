package io.github.suadocowboy.blueengine.core;

import java.time.Duration;
import java.time.Instant;

public class TickTimer {
    private Duration timePerTick;
    private Instant lastTickTime;

    public TickTimer(double tickRate) {
        setTickRate(tickRate);
        lastTickTime = Instant.now();
    }

    public void setTickRate(double tickRate) {
        timePerTick = Duration.ofMillis( (long) (1000.0 / tickRate) );
    }

    public boolean shouldTick() {
        Instant currentTime = Instant.now();
        Duration deltaTime = Duration.between(lastTickTime, currentTime);

        if (deltaTime.compareTo(timePerTick) >= 0) {
            lastTickTime = currentTime;
            return true;
        }

        return false;
    }
}
