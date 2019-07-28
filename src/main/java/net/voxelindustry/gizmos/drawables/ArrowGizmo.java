package net.voxelindustry.gizmos.drawables;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.voxelindustry.gizmos.render.ArrowRenderer;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
public class ArrowGizmo extends BaseGizmo
{

    private Vec3d start;
    private Vec3d end;

    private EnumFacing.Axis axis;

    private int color;

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
