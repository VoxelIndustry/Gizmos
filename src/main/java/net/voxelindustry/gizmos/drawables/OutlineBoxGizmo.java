package net.voxelindustry.gizmos.drawables;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.util.math.Vec3d;
import net.voxelindustry.gizmos.render.BoxRenderer;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
public class OutlineBoxGizmo extends BaseGizmo
{
    private Vec3d pos;
    private Vec3d size;
    private Vec3d offset;

    private int   color;
    private float edgeWidth;

    @Override
    public DrawMode draw(DrawMode currentMode, double playerX, double playerY, double playerZ)
    {
        if (currentMode != DrawMode.COLOR)
            this.useColor();

        BoxRenderer.drawOutlineBox(pos.x + offset.x - playerX - size.x / 2,
                pos.y + offset.y - playerY - size.y / 2,
                pos.z + offset.z - playerZ - size.z / 2,
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
