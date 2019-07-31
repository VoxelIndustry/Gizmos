package net.voxelindustry.gizmos.handle;

import java.time.Duration;
import java.time.LocalTime;

public class TimedGizmoHandle extends GizmoHandle
{
    private final Duration  duration;
    private final LocalTime startTime;

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
}
