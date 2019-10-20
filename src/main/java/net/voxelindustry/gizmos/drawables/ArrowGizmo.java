package net.voxelindustry.gizmos.drawables;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.util.math.Vec3d;
import net.voxelindustry.gizmos.render.ArrowRenderer;

import static net.minecraft.util.EnumFacing.Axis;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
public class ArrowGizmo extends BaseGizmo
{
    private final Vec3d start;
    private final Vec3d end;

    private final Axis axis;

    private final int color;

    public ArrowGizmo(Vec3d pos, Vec3d start, Vec3d end, Axis axis, int color)
    {
        super(pos);
        this.start = start;
        this.end = end;
        this.axis = axis;
        this.color = color;
    }

    @Override
    public DrawMode draw(DrawMode currentMode, double playerX, double playerY, double playerZ)
    {
        if (currentMode != DrawMode.TEXTURE)
            this.useTexture();

        ArrowRenderer.drawHorArrow(start.x - playerX, start.y - playerY, start.z - playerZ,
                end.x - playerX, end.y - playerY, end.z - playerZ, color);

        return getDrawMode();
    }

    @Override
    public DrawMode getDrawMode()
    {
        return DrawMode.TEXTURE;
    }
}
