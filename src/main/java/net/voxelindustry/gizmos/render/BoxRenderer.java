package net.voxelindustry.gizmos.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class BoxRenderer
{
    public static void drawFilledBox(double posX, double posY, double posZ, double width, double height, double length,
                                     int rgba)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        float red = (rgba >> 24 & 0xFF) / 255f;
        float green = (rgba >> 16 & 0xFF) / 255f;
        float blue = (rgba >> 8 & 0xFF) / 255f;
        float alpha = (rgba & 0xFF) / 255f;

        buffer.begin(5, DefaultVertexFormats.POSITION_COLOR);

        buffer.pos(posX, posY, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY + height, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY + height, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY + height, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY + height, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY + height, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY + height, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY + height, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY + height, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY + height, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY + height, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY + height, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY + height, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY + height, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY + height, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY + height, posZ + length).color(red, green, blue, alpha).endVertex();

        tessellator.draw();
    }

    public static void drawOutlineBox(double posX, double posY, double posZ, double width, double height,
                                      double length, float thinness, int rgba)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        float previousLineWidth = GL11.glGetFloat(GL11.GL_LINE_WIDTH);

        GL11.glLineWidth(thinness);
        float red = (rgba >> 24 & 0xFF) / 255f;
        float green = (rgba >> 16 & 0xFF) / 255f;
        float blue = (rgba >> 8 & 0xFF) / 255f;
        float alpha = (rgba & 0xFF) / 255f;

        buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);

        buffer.pos(posX, posY, posZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(posX, posY, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY + height, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY + height, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY + height, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY + height, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY + height, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY + height, posZ + length).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(posX, posY, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY + height, posZ + length).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(posX + width, posY, posZ + length).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY + height, posZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(posX + width, posY, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY, posZ).color(red, green, blue, 0.0F).endVertex();

        tessellator.draw();

        GL11.glLineWidth(previousLineWidth);
    }
}
