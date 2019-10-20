package net.voxelindustry.gizmos.drawables;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.util.math.Vec3d;
import net.voxelindustry.gizmos.render.BoxRenderer;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
public class OutlineBoxGizmo extends BaseGizmo
{
    private final Vec3d size;
    private final Vec3d offset;

    private final int   color;
    private final float edgeWidth;

    public OutlineBoxGizmo(Vec3d pos, Vec3d size, Vec3d offset, int color, float edgeWidth)
    {
        super(pos);
        this.size = size;
        this.offset = offset;
        this.color = color;
        this.edgeWidth = edgeWidth;
    }

    @Override
    public DrawMode draw(DrawMode currentMode, double playerX, double playerY, double playerZ)
    {
        if (currentMode != DrawMode.COLOR)
            this.useColor();

        BoxRenderer.drawOutlineBox(getPosX() + offset.x - playerX - size.x / 2,
                getPosY() + offset.y - playerY - size.y / 2,
                getPosZ() + offset.z - playerZ - size.z / 2,
                size.x, size.y, size.z,
                edgeWidth, color);

        return getDrawMode();
    }

    @Override
    public DrawMode getDrawMode()
    {
        return DrawMode.COLOR;
    }
}
