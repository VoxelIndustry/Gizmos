package net.voxelindustry.gizmos.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.voxelindustry.gizmos.drawables.BaseGizmo;
import net.voxelindustry.gizmos.drawables.DrawMode;
import net.voxelindustry.gizmos.handle.GizmoHandle;
import net.voxelindustry.gizmos.widget.data.IDataHolderGizmo;
import net.voxelindustry.gizmos.widget.data.IGizmoDataPart;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class WindowGizmo extends BaseGizmo implements IDataHolderGizmo
{
    private List<Pair<IGizmoDataPart, GizmoHandle>> lines = new ArrayList<>();

    private Vec3d pos;

    public WindowGizmo(Vec3d pos)
    {
        this.pos = pos;
    }

    @Override
    public DrawMode draw(DrawMode currentMode, double playerX, double playerY, double playerZ)
    {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.translate(pos.x - playerX, pos.y - playerY, pos.z - playerZ);

        GlStateManager.rotate(
                (float) (Minecraft.getMinecraft().getRenderManager().options.thirdPersonView == 2 ? 1 : -1)
                        * Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.scale(0.0625f / 4, 0.0625f / 4, 0.0625f / 4);

        DrawMode loopCurrent = currentMode;
        double currentY = 0;
        for (Pair<IGizmoDataPart, GizmoHandle> line : lines)
        {
            loopCurrent = line.getKey().draw(loopCurrent, 0, currentY, 0);
            currentY += line.getKey().getHeight();
        }

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

        lines.removeIf(dataByHandle -> dataByHandle.getValue().shouldExpire());
        return getDrawMode();
    }

    @Override
    public DrawMode getDrawMode()
    {
        return DrawMode.TEXTURE;
    }

    @Override
    public void appendData(IGizmoDataPart dataPart, GizmoHandle handle)
    {
        lines.add(Pair.of(dataPart, handle));
    }

    @Override
    public void removeData(IGizmoDataPart dataPart)
    {
        lines.removeIf(dataByHandle -> dataByHandle.getKey() == dataPart);
    }
}
