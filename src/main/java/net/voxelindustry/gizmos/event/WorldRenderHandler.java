package net.voxelindustry.gizmos.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.voxelindustry.gizmos.Gizmos;
import net.voxelindustry.gizmos.drawables.DrawMode;
import net.voxelindustry.gizmos.drawables.BaseGizmo;
import org.lwjgl.opengl.GL11;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class WorldRenderHandler
{
    private static WorldRenderHandler INSTANCE;

    public static WorldRenderHandler instance()
    {
        if (INSTANCE == null)
            INSTANCE = new WorldRenderHandler();
        return INSTANCE;
    }

    private EntityPlayerSP player;

    private Queue<BaseGizmo> toDraw;

    private WorldRenderHandler()
    {
        toDraw = new PriorityQueue<>(Comparator.comparingInt(gizmo -> gizmo.getDrawMode().ordinal()));
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent e)
    {
        if (player == null)
            player = Minecraft.getMinecraft().player;

        double playerX = player.prevPosX + (player.posX - player.prevPosX) * e.getPartialTicks();
        double playerY = player.prevPosY + (player.posY - player.prevPosY) * e.getPartialTicks();
        double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * e.getPartialTicks();

        // Draw instant gizmos
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        DrawMode currentMode = DrawMode.TEXTURE;
        while (toDraw.size() != 0)
            currentMode = toDraw.poll().draw(currentMode, playerX, playerY, playerZ);

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();

        if (currentMode != DrawMode.TEXTURE)
            GL11.glEnable(GL11.GL_TEXTURE_2D);

        Gizmos.outlineBox(new Vec3d(playerX, playerY + 2, playerZ + 2), Gizmos.ONE, 0xFFC8C888,3);
        Gizmos.edgedBox(new Vec3d(playerX, playerY + 4, playerZ + 2), Gizmos.ONE, 0xFFC8C888,0xFF0000FF,3);
    }

    public void addGizmo(BaseGizmo gizmo)
    {
        this.toDraw.add(gizmo);
    }
}
