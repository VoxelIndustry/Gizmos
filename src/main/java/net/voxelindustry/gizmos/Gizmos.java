package net.voxelindustry.gizmos;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.voxelindustry.gizmos.drawables.BaseGizmo;
import net.voxelindustry.gizmos.drawables.BoxGizmo;
import net.voxelindustry.gizmos.drawables.EdgedBoxGizmo;
import net.voxelindustry.gizmos.drawables.OutlineBoxGizmo;
import net.voxelindustry.gizmos.event.WorldRenderHandler;
import net.voxelindustry.gizmos.util.PositionUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
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

    public static void path(Function<BlockPos, BaseGizmo> gizmoGenerator, List<BlockPos> positions)
    {
        List<BaseGizmo> collect = IntStream.range(0, positions.size() - 1)
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
                .collect(Collectors.toList());

        collect
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
}
