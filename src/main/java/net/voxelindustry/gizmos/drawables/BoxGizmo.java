package net.voxelindustry.gizmos.drawables;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.util.math.Vec3d;
import net.voxelindustry.gizmos.render.BoxRenderer;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
public class BoxGizmo extends BaseGizmo
{
    private Vec3d size;
    private Vec3d offset;

    private int color;

    public BoxGizmo(Vec3d pos, Vec3d size, Vec3d offset, int color)
    {
        super(pos);
        this.size = size;
        this.offset = offset;
        this.color = color;
    }

    @Override
    public DrawMode draw(DrawMode currentMode, double playerX, double playerY, double playerZ)
    {
        if (currentMode != DrawMode.COLOR)
            this.useColor();

        BoxRenderer.drawFilledBox(getPosX() + offset.x - playerX - size.x / 2,
                getPosY() + offset.y - playerY - size.y / 2,
                getPosZ() + offset.z - playerZ - size.z / 2,
                size.x, size.y, size.z,
                color);

        return getDrawMode();
    }

    @Override
    public DrawMode getDrawMode()
    {
        return DrawMode.COLOR;
    }
}
