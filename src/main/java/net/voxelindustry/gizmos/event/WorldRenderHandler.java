package net.voxelindustry.gizmos.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.voxelindustry.gizmos.Gizmos;
import net.voxelindustry.gizmos.drawables.ArrowGizmo;
import net.voxelindustry.gizmos.drawables.BaseGizmo;
import net.voxelindustry.gizmos.drawables.DrawMode;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
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

        Vec3d begin = new Vec3d(375, 67, 455);
        Vec3d end = new Vec3d(375 + 0.25f, 68.5f, 455);


        addGizmo(new ArrowGizmo(begin, end, EnumFacing.Axis.X, 0xFFFFFFFF));

        List<BlockPos> pos = Arrays.asList(
                new BlockPos(375, 67, 455),
                new BlockPos(376, 67, 455),
                new BlockPos(377, 67, 455),
                new BlockPos(378, 67, 455),
                new BlockPos(378, 67, 456));
        Gizmos.edgedBoxPath(pos, 0.3D, 0x77000055, 0xFF0000FF);

        Gizmos.box(begin, new Vec3d(0.25f, 0.25f, 0.25f), 0xFF0000FF);
        Gizmos.box(end, new Vec3d(0.25f, 0.25f, 0.25f), 0x00FF00FF);
    }

    public <T extends BaseGizmo> T addGizmo(T gizmo)
    {
        this.toDraw.add(gizmo);
        return gizmo;
    }
}
