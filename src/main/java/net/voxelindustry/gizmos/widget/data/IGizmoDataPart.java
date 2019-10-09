package net.voxelindustry.gizmos.widget.data;

import net.voxelindustry.gizmos.drawables.DrawMode;
import org.lwjgl.opengl.GL11;

public interface IGizmoDataPart
{
    DrawMode draw(DrawMode currentMode, double playerX, double playerY, double playerZ);

    DrawMode getDrawMode();

    float getWidth();

    float getHeight();

    default void useColor()
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    default void useTexture()
    {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
