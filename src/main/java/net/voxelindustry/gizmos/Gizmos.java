package net.voxelindustry.gizmos;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.voxelindustry.gizmos.drawables.BaseGizmo;
import net.voxelindustry.gizmos.drawables.BoxGizmo;
import net.voxelindustry.gizmos.drawables.EdgedBoxGizmo;
import net.voxelindustry.gizmos.drawables.OutlineBoxGizmo;
import net.voxelindustry.gizmos.drawables.TextGizmo;
import net.voxelindustry.gizmos.event.WorldRenderHandler;
import net.voxelindustry.gizmos.unique.UniqueGizmos;
import net.voxelindustry.gizmos.util.PositionUtil;
import net.voxelindustry.gizmos.widget.WindowGizmo;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Gizmos
{
    public static final Vec3d ONE = new Vec3d(1, 1, 1);

    public static BoxGizmo box(Vec3d pos, Vec3d size, Vec3d offset, int colorRGBA)
    {
        return WorldRenderHandler.instance().addGizmo(new BoxGizmo(pos, size, offset, colorRGBA));
    }

    public static BoxGizmo box(Vec3d pos, Vec3d size, int colorRGBA)
    {
        return box(pos, size, Vec3d.ZERO, colorRGBA);
    }

    public static BoxGizmo box(Vec3d pos, int colorRGBA)
    {
        return box(pos, ONE, colorRGBA);
    }

    public static OutlineBoxGizmo outlineBox(Vec3d pos, Vec3d size, Vec3d offset, int colorRGBA, float edgeWidth)
    {
        return WorldRenderHandler.instance().addGizmo(new OutlineBoxGizmo(pos, size, offset, colorRGBA, edgeWidth));
    }

    public static OutlineBoxGizmo outlineBox(Vec3d pos, Vec3d size, int colorRGBA, float edgeWidth)
    {
        return outlineBox(pos, size, Vec3d.ZERO, colorRGBA, edgeWidth);
    }

    public static OutlineBoxGizmo outlineBox(Vec3d pos, Vec3d size, int colorRGBA)
    {
        return outlineBox(pos, size, colorRGBA, 1);
    }

    public static OutlineBoxGizmo outlineBox(Vec3d pos, int colorRGBA, float edgeWidth)
    {
        return outlineBox(pos, ONE, colorRGBA, edgeWidth);
    }

    public static OutlineBoxGizmo outlineBox(Vec3d pos, int colorRGBA)
    {
        return outlineBox(pos, colorRGBA, 1);
    }

    public static EdgedBoxGizmo edgedBox(Vec3d pos, Vec3d size, Vec3d offset, int colorRGBA, int edgeColorRGBA, float edgeWidth)
    {
        return WorldRenderHandler.instance().addGizmo(new EdgedBoxGizmo(pos, size, offset, colorRGBA, edgeColorRGBA,
                edgeWidth));
    }

    public static EdgedBoxGizmo edgedBox(Vec3d pos, Vec3d size, int colorRGBA, int edgeColorRGBA, float edgeWidth)
    {
        return edgedBox(pos, size, Vec3d.ZERO, colorRGBA, edgeColorRGBA, edgeWidth);
    }

    public static EdgedBoxGizmo edgedBox(Vec3d pos, Vec3d size, int colorRGBA, int edgeColorRGBA)
    {
        return edgedBox(pos, size, colorRGBA, edgeColorRGBA, 1);
    }

    public static EdgedBoxGizmo edgedBox(Vec3d pos, int colorRGBA, int edgeColorRGBA, float edgeWidth)
    {
        return edgedBox(pos, ONE, colorRGBA, edgeColorRGBA, edgeWidth);
    }

    public static EdgedBoxGizmo edgedBox(Vec3d pos, int colorRGBA, int edgeColorRGBA)
    {
        return edgedBox(pos, ONE, colorRGBA, edgeColorRGBA);
    }

    public static TextGizmo text(Vec3d pos, String text, int colorRGBA)
    {
        return WorldRenderHandler.instance().addGizmo(new TextGizmo(pos, text, colorRGBA));
    }

    public static <T extends BaseGizmo> void path(Function<BlockPos, BaseGizmo> gizmoGenerator, List<BlockPos> positions)
    {
        IntStream.range(0, positions.size() - 1)
                .boxed()
                .map(index -> Pair.of(index, index + 1))
                .map(indexPair -> Pair.of(positions.get(indexPair.getLeft()), positions.get(indexPair.getRight())))
                .flatMap(posPair ->
                {
                    if (PositionUtil.arePosContiguous(posPair.getLeft(), posPair.getRight()))
                        return Stream.of(posPair.getLeft(), posPair.getRight());
                    else
                        return PositionUtil.fillGaps(posPair.getLeft(), posPair.getRight()).stream();
                })
                .distinct()
                .map(gizmoGenerator)
                .forEach(WorldRenderHandler.instance()::addGizmo);
    }

    public static <T extends BaseGizmo> void path(Function<Vec3d, BaseGizmo> gizmoGenerator, List<Vec3d> positions, double step)
    {
        IntStream.range(0, positions.size() - 1)
                .boxed()
                .map(index -> Pair.of(index, index + 1))
                .map(indexPair -> Pair.of(positions.get(indexPair.getLeft()), positions.get(indexPair.getRight())))
                .flatMap(posPair ->
                {
                    if (posPair.getLeft().distanceTo(posPair.getRight()) <= step)
                        return Stream.of(posPair.getLeft(), posPair.getRight());
                    else
                        return PositionUtil.fillGaps(posPair.getLeft(), posPair.getRight(), step).stream();
                })
                .distinct()
                .map(gizmoGenerator)
                .forEach(WorldRenderHandler.instance()::addGizmo);
    }

    public static void pathIndexed(IntFunction<BaseGizmo> gizmoGenerator, List<BlockPos> positions)
    {
        path(pos -> gizmoGenerator.apply(positions.indexOf(pos)), positions);
    }

    public static void boxPath(List<BlockPos> positions, int boxColorRGBA)
    {
        path(pos -> box(new Vec3d(pos), boxColorRGBA), positions);
    }

    public static void boxPath(List<BlockPos> positions, double size, int boxColorRGBA)
    {
        path(pos -> box(new Vec3d(pos), new Vec3d(size, size, size), boxColorRGBA), positions);
    }

    public static void edgedBoxPath(List<BlockPos> positions, int boxColorRGBA, int edgeColorRGBA)
    {
        path(pos -> edgedBox(new Vec3d(pos), boxColorRGBA, edgeColorRGBA), positions);
    }

    public static void edgedBoxPath(List<BlockPos> positions, double size, int boxColorRGBA, int edgeColorRGBA)
    {
        path(pos -> edgedBox(new Vec3d(pos), new Vec3d(size, size, size), boxColorRGBA, edgeColorRGBA), positions);
    }

    public static WindowGizmo window(WindowGizmo.Parameters parameters)
    {
        return WorldRenderHandler.instance().addGizmo(new WindowGizmo(parameters));
    }

    public static WindowGizmo window(Vec3d pos, int textColorRGBA)
    {
        return window(WindowGizmo.Parameters.builder().pos(pos).textColorRGBA(textColorRGBA).build());
    }

    public static WindowGizmo window(Vec3d pos)
    {
        return window(pos, 0x40404000);
    }

    public static <T extends BaseGizmo> T getCreateByPos(Vec3d pos, String name, Supplier<T> gizmoCreator)
    {
        return (T) UniqueGizmos.getByPos(pos, name)
                .orElseGet(() -> gizmoCreator.get().uniqueByPos(pos, name));
    }

    public static <T extends BaseGizmo> T getCreateByEntity(Entity entity, String name, Supplier<T> gizmoCreator)
    {
        return (T) UniqueGizmos.getByEntity(entity, name)
                .orElseGet(() -> gizmoCreator.get().uniqueByEntity(entity, name));
    }
}
