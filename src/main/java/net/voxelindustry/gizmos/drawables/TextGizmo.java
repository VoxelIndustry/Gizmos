package net.voxelindustry.gizmos.drawables;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
public class TextGizmo extends BaseGizmo
{
    private Vec3d  pos;
    private String text;
    private int    colorRGBA;

    @Override
    public DrawMode draw(DrawMode currentMode, double playerX, double playerY, double playerZ)
    {
        if (currentMode != DrawMode.TEXTURE)
            this.useTexture();

        if(Minecraft.getMinecraft().getRenderManager().options == null)
            return getDrawMode();

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.translate(pos.x - playerX, pos.y - playerY, pos.z - playerZ);

        GlStateManager.rotate(
                (float) (Minecraft.getMinecraft().getRenderManager().options.thirdPersonView == 2 ? 1 : -1)
                        * Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180, 0, 0,1);
        GlStateManager.scale(0.0625f / 4, 0.0625f / 4, 0.0625f / 4);

        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);

        Minecraft.getMinecraft().fontRenderer.drawString(text, 0, 0, colorRGBA);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
        return getDrawMode();
    }

    @Override
    public DrawMode getDrawMode()
    {
        return DrawMode.TEXTURE;
    }
}
