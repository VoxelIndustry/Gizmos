package net.voxelindustry.gizmos.drawables;

import java.util.List;

public class CompoundGizmo extends BaseGizmo
{
    private final List<BaseGizmo> gizmos;

    public CompoundGizmo(List<BaseGizmo> gizmos)
    {
        this.gizmos = gizmos;
    }

    @Override
    public DrawMode draw(DrawMode currentMode, double playerX, double playerY, double playerZ)
    {
        gizmos.forEach(gizmo -> gizmo.draw(currentMode, playerX, playerY, playerZ));

        return DrawMode.MIXED;
    }

    @Override
    public DrawMode getDrawMode()
    {
        return DrawMode.MIXED;
    }
}
