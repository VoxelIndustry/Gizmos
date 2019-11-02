package net.voxelindustry.gizmos.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GridUtilTest
{
    @Test
    void getFreeSquare_givenEmptyTable_withSameSizeCell_thenShouldPlaceAtZero()
    {
        Table<Integer, Integer, String> emptyTable = HashBasedTable.create();

        GridUtil.IntPair freeSquare = GridUtil.getFreeSquare(emptyTable, 2, 2, 2, 2);

        assertThat(freeSquare.getX()).isEqualTo(0);
        assertThat(freeSquare.getY()).isEqualTo(0);
    }

    @Test
    void getFreeSquare_givenEmptyTable_withTooLargeCell_thenShouldFailToPlace()
    {
        Table<Integer, Integer, String> emptyTable = HashBasedTable.create();

        GridUtil.IntPair freeSquare = GridUtil.getFreeSquare(emptyTable, 2, 2, 3, 2);

        assertThat(freeSquare.getX()).isEqualTo(-1);
        assertThat(freeSquare.getY()).isEqualTo(-1);
    }

    @Test
    void getFreeSquare_givenTable_withOneCellTaken_withSmallEnoughCell_thenShouldPlaceAfterTakenCell()
    {
        Table<Integer, Integer, String> table = HashBasedTable.create();
        table.put(0, 0, "X");

        GridUtil.IntPair freeSquare = GridUtil.getFreeSquare(table, 3, 3, 2, 2);

        assertThat(freeSquare.getX()).isEqualTo(1);
        assertThat(freeSquare.getY()).isEqualTo(0);
    }

    @Test
    void getFreeSquare_givenTable_withTwoLineTaken_withSmallEnoughCell_thenShouldPlaceAtOnlyPossibleCell()
    {
        Table<Integer, Integer, String> emptyTable = HashBasedTable.create();
        emptyTable.put(0, 0, "X");
        emptyTable.put(0, 1, "X");
        emptyTable.put(0, 2, "X");
        emptyTable.put(1, 0, "X");
        emptyTable.put(2, 0, "X");

        GridUtil.IntPair freeSquare = GridUtil.getFreeSquare(emptyTable, 3, 3, 2, 2);

        assertThat(freeSquare.getX()).isEqualTo(1);
        assertThat(freeSquare.getY()).isEqualTo(1);
    }
}