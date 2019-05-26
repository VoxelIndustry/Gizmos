package net.voxelindustry.gizmos.drawables;

import org.lwjgl.opengl.GL11;

public abstract class BaseGizmo
{
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
}
