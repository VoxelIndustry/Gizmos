package net.voxelindustry.gizmos.util;

import com.google.common.collect.Table;
import lombok.Value;

public class GridUtil
{
    public static IntPair getFreeSquare(Table<Integer, Integer, ?> grid, int rows, int columns, int width, int height)
    {
        int selectedRow = -1;
        int selectedColumn = -1;

        for (int columnIndex = 0; columnIndex < columns; columnIndex++)
        {
            if (columnIndex + width > columns)
                break;

            boolean canPlace = true;
            for (int rowIndex = 0; rowIndex < rows; rowIndex++)
            {
                if (rowIndex + height > rows)
                {
                    canPlace = false;
                    break;
                }

                canPlace = true;
                for (int cellRowIndex = 0; cellRowIndex < width; cellRowIndex++)
                {
                    for (int cellColumnIndex = 0; cellColumnIndex < height; cellColumnIndex++)
                    {
                        if (grid.contains(rowIndex, columnIndex))
                        {
                            canPlace = false;
                            break;
                        }
                    }
                    if (!canPlace)
                        break;
                }

                if (canPlace)
                {
                    selectedRow = rowIndex;
                    break;
                }
            }

            if (canPlace)
            {
                selectedColumn = columnIndex;
                break;
            }
        }
        return IntPair.of(selectedRow, selectedColumn);
    }

    @Value(staticConstructor = "of")
    public static class IntPair
    {
        int x;
        int y;
    }
}
