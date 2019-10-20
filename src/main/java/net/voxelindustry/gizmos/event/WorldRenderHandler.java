package net.voxelindustry.gizmos.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.voxelindustry.gizmos.drawables.BaseGizmo;
import net.voxelindustry.gizmos.drawables.DrawMode;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        toDraw = new PriorityQueue<>(Comparator.comparingInt(gizmo -> gizmo == null ? Integer.MIN_VALUE : gizmo.getDrawMode().ordinal()));
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent e)
    {
        player = Minecraft.getMinecraft().player;

        double playerX = player.prevPosX + (player.posX - player.prevPosX) * e.getPartialTicks();
        double playerY = player.prevPosY + (player.posY - player.prevPosY) * e.getPartialTicks();
        double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * e.getPartialTicks();

        // Draw instant gizmos
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        DrawMode currentMode = DrawMode.TEXTURE;

        int size = toDraw.size();

        List<BaseGizmo> toAdd = new ArrayList<>();
        for (int i = 0; i < size; i++)
        {
            BaseGizmo gizmo = toDraw.poll();

            if (gizmo == null)
                continue;

            currentMode = gizmo.draw(currentMode, playerX, playerY, playerZ);
            if (gizmo.getHandle().isPresent() && !gizmo.getHandle().get().shouldExpire())
                toAdd.add(gizmo);
            else
                gizmo.expire();
        }

        toDraw.addAll(toAdd);

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();

        if (currentMode != DrawMode.TEXTURE)
            GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public synchronized <T extends BaseGizmo> T addGizmo(T gizmo)
    {
        this.toDraw.add(gizmo);
        return gizmo;
    }
}
