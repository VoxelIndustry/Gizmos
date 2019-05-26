package net.voxelindustry.gizmos.drawables;

import net.minecraft.util.math.Vec3d;
import net.voxelindustry.gizmos.render.BoxRenderer;

public class EdgedBoxGizmo extends BoxGizmo
{
    private int   edgeColor;
    private float edgeWidth;

    public EdgedBoxGizmo(Vec3d pos, Vec3d size, Vec3d offset, int color, int edgeColor, float edgeWidth)
    {
        super(pos, size, offset, color);

        this.edgeColor = edgeColor;
        this.edgeWidth = edgeWidth;
    }

    @Override
    public DrawMode draw(DrawMode currentMode, double playerX, double playerY, double playerZ)
    {
        if (currentMode != DrawMode.COLOR)
            this.useColor();

        BoxRenderer.drawOutlineBox(getPos().x + getOffset().x - playerX - getSize().x / 2,
                getPos().y + getOffset().y - playerY - getSize().y / 2,
                getPos().z + getOffset().z - playerZ - getSize().z / 2,
                getSize().x, getSize().y, getSize().z,
                edgeWidth, edgeColor);

        return super.draw(currentMode, playerX, playerY, playerZ);
    }

    @Override
    public DrawMode getDrawMode()
    {
        return DrawMode.COLOR;
    }
}
