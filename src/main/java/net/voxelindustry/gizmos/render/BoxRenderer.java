package net.voxelindustry.gizmos.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing.Axis;
import org.lwjgl.opengl.GL11;

import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static net.minecraft.util.EnumFacing.Axis.X;
import static net.minecraft.util.EnumFacing.Axis.Y;

public class BoxRenderer
{
    public static void drawColoredElbowLine(double startX, double startY, double endX, double endY, double posZ, double thinness, int rgba)
    {
        double width = abs(endX - startX);
        double height = abs(endY - startY);

        if (width > height)
        {
            if (height == 0)
                drawColoredAxisLine(X, startX, endX, startY, posZ, thinness, rgba);
            else
            {
                drawColoredAxisLine(Y, startY, signum(endY) * (abs(endY) - height / 2), startX, posZ, thinness, rgba);
                drawColoredAxisLine(X, startX - thinness / 2, endX + thinness / 2, signum(endY) * (abs(endY) - height / 2 - thinness / 2), posZ, thinness, rgba);
                drawColoredAxisLine(Y, signum(endY) * (abs(endY) - height / 2 - thinness), endY, endX, posZ, thinness, rgba);
            }
        }
        else
        {
            if (width == 0)
                drawColoredAxisLine(Y, startY, endY, startX, posZ, thinness, rgba);
            else
            {
                drawColoredAxisLine(X, startX, signum(endX) * (abs(endX) - width / 2), startY, posZ, thinness, rgba);
                drawColoredAxisLine(Y, startY - thinness / 2, endY + thinness / 2, signum(endX) * (abs(endX) - width / 2 - thinness / 2), posZ, thinness, rgba);
                drawColoredAxisLine(X, signum(endX) * (abs(endX) - width / 2 - thinness), endX, endY, posZ, thinness, rgba);
            }
        }
    }

    public static void drawColoredAxisLine(Axis axis, double start, double end, double axisPos, double posZ, double thinness, int rgba)
    {
        if (start > end)
        {
            double tmp = start;
            start = end;
            end = tmp;
        }

        if (axis == X)
            drawColoredQuad(start, axisPos - thinness / 2, posZ, end - start, thinness, rgba);
        else
            drawColoredQuad(axisPos - thinness / 2, start, posZ, thinness, end - start, rgba);
    }

    public static void drawColoredProgressEmptyQuad(double posX, double posY, double posZ, double width, double height, double thinness, int rgba, double progress)
    {
        double perimeter = width * 2 + height * 2;
        double widthPart = width / perimeter;
        double heightPart = height / perimeter;

        if (progress < widthPart)
        {
            double upperPart = progress / widthPart;
            drawColoredQuad(posX + thinness + ((width - thinness) * upperPart), posY, posZ, (width - thinness) * (1 - upperPart), thinness, rgba);
            drawColoredQuad(posX + width - thinness, posY + thinness, posZ, thinness, height - thinness, rgba);
            drawColoredQuad(posX + thinness, posY + height - thinness, posZ, width - thinness, thinness, rgba);
            drawColoredQuad(posX, posY + thinness, posZ, thinness, height - thinness, rgba);
        }
        else if (progress < widthPart + heightPart)
        {
            double rightPart = (progress - widthPart) / heightPart;
            drawColoredQuad(posX + width - thinness, posY + (height - thinness) * rightPart, posZ, thinness, (height - thinness) * (1 - rightPart), rgba);
            drawColoredQuad(posX + thinness, posY + height - thinness, posZ, width - thinness, thinness, rgba);
            drawColoredQuad(posX, posY + thinness, posZ, thinness, height - thinness, rgba);
        }
        else if (progress < widthPart * 2 + heightPart)
        {
            double bottomPart = (progress - widthPart - heightPart) / widthPart;
            drawColoredQuad(posX, posY + height - thinness, posZ, (width - thinness) * (1 - bottomPart), thinness, rgba);
            drawColoredQuad(posX, posY + thinness, posZ, thinness, height - thinness, rgba);
        }
        else
        {
            double rightPart = (progress - widthPart * 2 - heightPart) / heightPart;
            drawColoredQuad(posX, posY + thinness, posZ, thinness, (height - thinness) * (1 - rightPart), rgba);
        }
    }

    public static void drawColoredEmptyQuad(double posX, double posY, double posZ, double width, double height, double thinness, int rgba)
    {
        drawColoredQuad(posX, posY, posZ, width, thinness, rgba);
        drawColoredQuad(posX, posY + height - thinness, posZ, width, thinness, rgba);

        drawColoredQuad(posX, posY + thinness, posZ, thinness, height - thinness * 2, rgba);
        drawColoredQuad(posX + width - thinness, posY + thinness, posZ, thinness, height - thinness * 2, rgba);
    }

    public static void drawColoredQuad(double posX, double posY, double posZ, double width, double height, int rgba)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        float red = (rgba >> 24 & 0xFF) / 255f;
        float green = (rgba >> 16 & 0xFF) / 255f;
        float blue = (rgba >> 8 & 0xFF) / 255f;
        float alpha = (rgba & 0xFF) / 255f;

        buffer.begin(5, DefaultVertexFormats.POSITION_COLOR);

        buffer.pos(posX + width, posY + height, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY + height, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX + width, posY + height, posZ).color(red, green, blue, alpha).endVertex();

        buffer.pos(posX + width, posY, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY + height, posZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(posX, posY, posZ).color(red, green, blue, alpha).endVertex();

        tessellator.draw();
    }

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
