package net.voxelindustry.gizmos.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.voxelindustry.gizmos.GizmosMod;
import org.lwjgl.opengl.GL11;

public class ArrowRenderer
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(GizmosMod.MODID, "textures/arrow.png");

    public static void drawHorArrow(double startX, double startY, double startZ, double endX,double endY, double endZ, int rgba)
    {
        float red = (rgba >> 24 & 0xFF) / 255f;
        float green = (rgba >> 16 & 0xFF) / 255f;
        float blue = (rgba >> 8 & 0xFF) / 255f;
        float alpha = (rgba & 0xFF) / 255f;

        Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        buffer.pos(startX, startY, startZ)
                .tex(0, 1).color(red, green, blue, alpha).endVertex();

        buffer.pos(endX, endY, endZ)
                .tex(0, 0).color(red, green, blue, alpha).endVertex();

        buffer.pos(endX+0.5f, endY - 0.5f, endZ)
                .tex(1, 0).color(red, green, blue, alpha).endVertex();

        buffer.pos(startX+0.5f, startY - 0.5f, startZ)
                .tex(1, 1).color(red, green, blue, alpha).endVertex();

        tessellator.draw();
    }
}
