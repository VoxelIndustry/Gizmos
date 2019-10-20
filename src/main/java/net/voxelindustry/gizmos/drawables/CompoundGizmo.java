package net.voxelindustry.gizmos.drawables;

import net.minecraft.util.math.Vec3d;

import java.util.List;

public class CompoundGizmo extends BaseGizmo
{
    private final List<BaseGizmo> gizmos;

    public CompoundGizmo(List<BaseGizmo> gizmos)
    {
        super(Vec3d.ZERO);
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
