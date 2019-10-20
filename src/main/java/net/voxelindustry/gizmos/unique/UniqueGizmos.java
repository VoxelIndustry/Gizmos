package net.voxelindustry.gizmos.unique;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.voxelindustry.gizmos.drawables.BaseGizmo;

import java.util.Optional;

public class UniqueGizmos
{
    private static Table<Vec3d, String, BaseGizmo>  gizmosByPos    = HashBasedTable.create();
    private static Table<Entity, String, BaseGizmo> gizmosByEntity = HashBasedTable.create();

    public static void registerUniqueByPos(Vec3d pos, String name, BaseGizmo gizmo)
    {
        gizmosByPos.put(pos, name, gizmo);
    }

    public static void registerUniqueByEntity(Entity entity, String name, BaseGizmo gizmo)
    {
        gizmosByEntity.put(entity, name, gizmo);
    }

    public static void removeUniqueByPos(Vec3d pos, String name)
    {
        gizmosByPos.remove(pos, name);
    }

    public static void removeUniqueByEntity(Entity entity, String name)
    {
        gizmosByEntity.remove(entity, name);
    }

    public static Optional<BaseGizmo> getByPos(Vec3d pos, String name)
    {
        return Optional.ofNullable(gizmosByPos.get(pos, name));
    }

    public static Optional<BaseGizmo> getByEntity(Entity entity, String name)
    {
        return Optional.ofNullable(gizmosByEntity.get(entity, name));
    }
}
