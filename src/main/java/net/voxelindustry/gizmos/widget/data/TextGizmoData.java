package net.voxelindustry.gizmos.widget.data;

import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.voxelindustry.gizmos.drawables.DrawMode;

@AllArgsConstructor
public class TextGizmoData implements IGizmoDataPart
{
    private final String  text;
    private final int     colorRGBA;
    private final Integer shadowColorRGBA;

    public TextGizmoData(String text, int colorRGBA)
    {
        this(text, colorRGBA, null);
    }

    @Override
    public DrawMode draw(DrawMode currentMode, double posX, double posY, double posZ)
    {
        if (currentMode != DrawMode.TEXTURE)
            this.useTexture();

        if (Minecraft.getMinecraft().getRenderManager().options == null)
            return getDrawMode();

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.scale(0.0625f / 4, 0.0625f / 4, 0.0625f / 4);

        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);

        if (shadowColorRGBA != null)
            Minecraft.getMinecraft().fontRenderer.drawString(text, (int) posX, (int) posY, shadowColorRGBA);
        Minecraft.getMinecraft().fontRenderer.drawString(text, (int) posX, (int) posY, colorRGBA);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
        return getDrawMode();
    }

    @Override
    public DrawMode getDrawMode()
    {
        return DrawMode.TEXTURE;
    }

    @Override
    public float getWidth()
    {
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }

    @Override
    public float getHeight()
    {
        return Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
    }
}
