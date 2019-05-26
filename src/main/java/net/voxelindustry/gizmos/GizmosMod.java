package net.voxelindustry.gizmos;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.voxelindustry.gizmos.event.WorldRenderHandler;
import org.apache.logging.log4j.Logger;

@Mod(modid = GizmosMod.MODID, name = GizmosMod.NAME, version = GizmosMod.VERSION)
public class GizmosMod
{
    public static final String MODID = "gizmos";
    public static final String NAME = "GizmosMod Debug Mod";
    public static final String VERSION = "0.1.0";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

        if(event.getSide() == Side.CLIENT)
            MinecraftForge.EVENT_BUS.register(WorldRenderHandler.instance());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    }
}
