package net.voxelindustry.gizmos.drawables;

import lombok.RequiredArgsConstructor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.voxelindustry.gizmos.handle.GizmoHandle;
import net.voxelindustry.gizmos.unique.GizmoUniqueness;
import net.voxelindustry.gizmos.unique.UniqueGizmos;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

import static net.voxelindustry.gizmos.unique.GizmoUniqueness.*;

@RequiredArgsConstructor
public abstract class BaseGizmo
{
    private GizmoHandle     handle;
    private GizmoUniqueness uniqueness = NONE;

    private Entity entity;
    private final Vec3d  pos;

    private String uniqueName;

    public abstract DrawMode draw(DrawMode currentMode, double playerX, double playerY, double playerZ);

    public abstract DrawMode getDrawMode();

    protected void useColor()
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    protected void useTexture()
    {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public Optional<GizmoHandle> getHandle()
    {
        return Optional.ofNullable(handle);
    }

    public BaseGizmo handle(GizmoHandle handle)
    {
        this.handle = handle;
        return this;
    }

    public BaseGizmo uniqueByPos(Vec3d pos, String uniqueName)
    {
        UniqueGizmos.registerUniqueByPos(pos, uniqueName, this);
        this.uniqueName = uniqueName;
        uniqueness = POS;
        return this;
    }

    public BaseGizmo uniqueByEntity(Entity entity, String uniqueName)
    {
        UniqueGizmos.registerUniqueByEntity(entity, uniqueName, this);
        this.uniqueName = uniqueName;
        uniqueness = ENTITY;
        return this;
    }

    public void expire()
    {
        switch (uniqueness)
        {
            case POS:
                UniqueGizmos.removeUniqueByPos(pos, uniqueName);
                break;
            case ENTITY:
                UniqueGizmos.removeUniqueByEntity(entity, uniqueName);
                break;
            case NONE:
                break;
        }
    }

    public Vec3d getPos()
    {
        if (entity == null)
            return pos;
        return new Vec3d(pos.x + entity.posX, pos.y + entity.posY, pos.z + entity.posZ);
    }

    public double getPosX()
    {
        if (entity == null)
            return pos.x;
        return pos.x + entity.posX;
    }

    public double getPosY()
    {
        if (entity == null)
            return pos.y;
        return pos.y + entity.posY;
    }

    public double getPosZ()
    {
        if (entity == null)
            return pos.z;
        return pos.z + entity.posZ;
    }
}
