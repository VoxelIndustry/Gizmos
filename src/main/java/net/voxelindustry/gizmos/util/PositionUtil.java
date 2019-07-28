package net.voxelindustry.gizmos.util;

import net.minecraft.util.math.BlockPos;

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
}
