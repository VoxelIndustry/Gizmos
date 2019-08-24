package net.voxelindustry.gizmos.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class PositionUtil
{
    public static boolean arePosContiguous(BlockPos first, BlockPos second)
    {
        return abs(first.getX() - second.getX()) <= 1 &&
                abs(first.getY() - second.getY()) <= 1 &&
                abs(first.getZ() - second.getZ()) <= 1;
    }

    public static List<BlockPos> fillGaps(BlockPos first, BlockPos second)
    {
        List<BlockPos> positions = new ArrayList<>();

        positions.add(first);

        int xStart = min(first.getX(), second.getX());
        int x;
        for (x = 0; x < abs(first.getX() - second.getX()); x++)
            positions.add(new BlockPos(xStart + x, first.getY(), first.getZ()));

        int yStart = min(first.getY(), second.getY());
        int y;
        for (y = 0; y < abs(first.getY() - second.getY()); y++)
            positions.add(new BlockPos(x, yStart + y, first.getZ()));

        int zStart = min(first.getZ(), second.getZ());
        int z;
        for (z = 0; z < abs(first.getZ() - second.getZ()); z++)
            positions.add(new BlockPos(x, y, zStart + z));

        return positions;
    }

    public static List<Vec3d> fillGaps(Vec3d first, Vec3d second, double step)
    {
        List<Vec3d> positions = new ArrayList<>();

        positions.add(first);

        double xStart = min(first.x, second.x);
        double x;
        for (x = 0; x < abs(first.x - second.x); x += step)
            positions.add(new Vec3d(xStart + x, first.y, first.z));

        double yStart = min(first.y, second.y);
        double y;
        for (y = 0; y < abs(first.y - second.y); y += step)
            positions.add(new Vec3d(x, yStart + y, first.z));

        double zStart = min(first.z, second.z);
        double z;
        for (z = 0; z < abs(first.z - second.z); z += step)
            positions.add(new Vec3d(x, y, zStart + z));

        return positions;
    }
}
