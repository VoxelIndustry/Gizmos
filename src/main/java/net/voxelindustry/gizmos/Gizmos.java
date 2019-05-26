package net.voxelindustry.gizmos;

import net.minecraft.util.math.Vec3d;
import net.voxelindustry.gizmos.drawables.BoxGizmo;
import net.voxelindustry.gizmos.drawables.EdgedBoxGizmo;
import net.voxelindustry.gizmos.drawables.OutlineBoxGizmo;
import net.voxelindustry.gizmos.event.WorldRenderHandler;

public class Gizmos
{
    public static final Vec3d ONE = new Vec3d(1, 1, 1);

    public static void box(Vec3d pos, Vec3d size, Vec3d offset, int colorRGBA)
    {
        WorldRenderHandler.instance().addGizmo(new BoxGizmo(pos, size, offset, colorRGBA));
    }

    public static void box(Vec3d pos, Vec3d size, int colorRGBA)
    {
        box(pos, size, Vec3d.ZERO, colorRGBA);
    }

    public static void box(Vec3d pos, int colorRGBA)
    {
        box(pos, ONE, colorRGBA);
    }

    public static void outlineBox(Vec3d pos, Vec3d size, Vec3d offset, int colorRGBA, float edgeWidth)
    {
        WorldRenderHandler.instance().addGizmo(new OutlineBoxGizmo(pos, size, offset, colorRGBA, edgeWidth));
    }

    public static void outlineBox(Vec3d pos, Vec3d size, int colorRGBA, float edgeWidth)
    {
        outlineBox(pos, size, Vec3d.ZERO, colorRGBA, edgeWidth);
    }

    public static void outlineBox(Vec3d pos, Vec3d size, int colorRGBA)
    {
        outlineBox(pos, size, colorRGBA, 1);
    }

    public static void outlineBox(Vec3d pos, int colorRGBA, float edgeWidth)
    {
        outlineBox(pos, ONE, colorRGBA, edgeWidth);
    }

    public static void outlineBox(Vec3d pos, int colorRGBA)
    {
        outlineBox(pos, colorRGBA, 1);
    }

    public static void edgedBox(Vec3d pos, Vec3d size, Vec3d offset, int colorRGBA, int edgeColorRGBA, float edgeWidth)
    {
        WorldRenderHandler.instance().addGizmo(new EdgedBoxGizmo(pos, size, offset, colorRGBA, edgeColorRGBA,
                edgeWidth));
    }

    public static void edgedBox(Vec3d pos, Vec3d size, int colorRGBA, int edgeColorRGBA, float edgeWidth)
    {
        edgedBox(pos, size, Vec3d.ZERO, colorRGBA, edgeColorRGBA, edgeWidth);
    }

    public static void edgedBox(Vec3d pos, Vec3d size, int colorRGBA, int edgeColorRGBA)
    {
        edgedBox(pos, size, colorRGBA, edgeColorRGBA, 1);
    }

    public static void edgedBox(Vec3d pos, int colorRGBA, int edgeColorRGBA, float edgeWidth)
    {
        edgedBox(pos, ONE, colorRGBA, edgeColorRGBA, edgeWidth);
    }

    public static void edgedBox(Vec3d pos, int colorRGBA, int edgeColorRGBA)
    {
        edgedBox(pos, ONE, colorRGBA, edgeColorRGBA);
    }

}
