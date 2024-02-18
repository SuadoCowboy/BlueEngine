package engine.core;

import java.time.Duration;
import java.time.Instant;

public class TickTime {
    private Duration timePerTick;
    private Instant lastTickTime;

    public TickTime(double tickRate) {
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
