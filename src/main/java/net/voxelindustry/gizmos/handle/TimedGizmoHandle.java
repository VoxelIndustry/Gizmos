package net.voxelindustry.gizmos.handle;

import java.time.Duration;
import java.time.LocalTime;

public class TimedGizmoHandle implements GizmoHandle
{
    private final Duration duration;

    private LocalTime startTime;

    public TimedGizmoHandle(Duration duration)
    {
        this.duration = duration;
        this.startTime = LocalTime.now();
    }

    @Override
    public boolean shouldExpire()
    {
        return Duration.between(startTime, LocalTime.now()).compareTo(duration) >= 0;
    }

    public void reset()
    {
        startTime = LocalTime.now();
    }

    public double getExpirationProgress()
    {
        return Duration.between(startTime, LocalTime.now()).toMillis() / (double) duration.toMillis();
    }
}
