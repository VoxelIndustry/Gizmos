package net.voxelindustry.gizmos.drawables;

import net.voxelindustry.gizmos.handle.GizmoHandle;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

public abstract class BaseGizmo
{
    private GizmoHandle handle;

    public abstract DrawMode draw(DrawMode currentMode, double playerX, double playerY, double playerZ);

    public abstract DrawMode getDrawMode();

    protected  void useColor()
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    protected void useTexture()
    {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public Optional<GizmoHandle> getHandle()
    {
        return Optional.ofNullable(handle);
    }

    public BaseGizmo handle(GizmoHandle handle)
    {
        this.handle = handle;
        return this;
    }
}
