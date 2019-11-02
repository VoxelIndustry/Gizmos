package net.voxelindustry.gizmos.widget;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.voxelindustry.gizmos.drawables.BaseGizmo;
import net.voxelindustry.gizmos.drawables.DrawMode;
import net.voxelindustry.gizmos.handle.GizmoHandle;
import net.voxelindustry.gizmos.render.BoxRenderer;
import net.voxelindustry.gizmos.util.GridUtil;
import net.voxelindustry.gizmos.widget.data.CounterValue;
import net.voxelindustry.gizmos.widget.data.IDataHolderGizmo;
import net.voxelindustry.gizmos.widget.data.JoinerValue;
import net.voxelindustry.gizmos.widget.data.MergeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static net.voxelindustry.gizmos.widget.data.Placeholders.PLACEHOLDER_PATTERN;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
public class GridWidget extends BaseGizmo implements IDataHolderGizmo
{
    private static final double TEXT_SCALE   = 0.0625F / 4;
    private static final double BORDER_WIDTH = 0.02D;

    private Table<Cell, String, MergeValue> placeholders = HashBasedTable.create();
    private Table<Integer, Integer, Cell>   cellTable    = HashBasedTable.create();

    private List<Cell> cells = new ArrayList<>();

    private double width;
    private double height;

    private double rowCellGap;
    private double columnCellGap;

    private int columns;
    private int rows;

    public GridWidget(Parameters parameters)
    {
        super(parameters.pos);

        this.width = parameters.width;
        this.height = parameters.height;
        this.columns = parameters.columns;
        this.rows = parameters.rows;
        this.rowCellGap = parameters.rowCellGap;
        this.columnCellGap = parameters.columnCellGap;
    }

    @Override
    public DrawMode draw(DrawMode currentMode, double playerX, double playerY, double playerZ)
    {
        if (currentMode != DrawMode.COLOR)
            this.useColor();

        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GlStateManager.translate(getPosX() - playerX, getPosY() - playerY, getPosZ() - playerZ);

        GlStateManager.rotate(
                (float) (Minecraft.getMinecraft().getRenderManager().options.thirdPersonView == 2 ? 1 : -1)
                        * Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

        GlStateManager.rotate(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);

        GlStateManager.rotate(180, 0, 0, 1);

        GlStateManager.color(1, 1, 1, 1);
        for (Cell cell : this.cells)
        {
            BoxRenderer.drawColoredQuad(cell.x *);
        }

        return getDrawMode();
    }

    @Override
    public DrawMode getDrawMode()
    {
        return DrawMode.MIXED;
    }

    @Override
    public void appendData(String text, GizmoHandle handle, Object... values)
    {
        appendCell(text, 1, 1, handle, values);
    }

    public void appendCell(String text, int width, int height, GizmoHandle handle, Object... values)
    {
        appendCell(text, -1, -1, width, height, handle, values);
    }

    public void appendCell(String text, int row, int column, int width, int height, GizmoHandle handle, Object... values)
    {
        Cell cell = new Cell(text, handle, width, height);

        extractPlaceholders(cell, text, values);
        computeCell(cell);

        if (row != -1 || column != -1)
        {
            for (int rowIndex = 0; rowIndex < width; rowIndex++)
            {
                for (int columnIndex = 0; columnIndex < height; columnIndex++)
                    cellTable.put(rowIndex, columnIndex, cell);
            }
            return;
        }

        GridUtil.IntPair freeSquare = GridUtil.getFreeSquare(cellTable, this.rows, this.columns, width, height);

        if (freeSquare.getX() == -1 || freeSquare.getY() == -1)
            return;

        for (int cellRowIndex = 0; cellRowIndex < width; cellRowIndex++)
        {
            for (int cellColumnIndex = 0; cellColumnIndex < height; cellColumnIndex++)
                this.cellTable.put(cellRowIndex + freeSquare.getX(), cellColumnIndex + freeSquare.getY(), cell);
        }

        cell.setX(freeSquare.getX());
        cell.setY(freeSquare.getY());
    }

    @Override
    public void removeData(String text)
    {
        List<Cell> matching = cells.stream().filter(cell -> cell.getRawText().equals(text)).collect(Collectors.toList());

        cells.removeAll(matching);
        matching.forEach(cell -> placeholders.row(cell).clear());
    }

    @Override
    public <T> void updateValue(String key, T value)
    {
        placeholders.column(key).values().forEach(mergeValue -> mergeValue.merge(value));
        placeholders.column(key).keySet().forEach(this::computeCell);
    }

    private void computeCell(Cell cell)
    {
        final String[] outputLine = {cell.getRawText()};

        placeholders.row(cell).forEach((key, value) ->
                outputLine[0] = outputLine[0].replace("{{" + key + "}}", value.getValue().toString()));

        cell.setComputed(outputLine[0]);
    }

    private void extractPlaceholders(Cell cell, String line, Object... values)
    {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(line);

        int group = 0;
        while (matcher.find())
        {
            placeholders.put(cell, matcher.group(1), getMergeValueFromObject(values[group]));
            group++;
        }
    }

    private MergeValue getMergeValueFromObject(Object o)
    {
        if (o instanceof Integer)
            return new CounterValue((Integer) o);
        if (o instanceof String)
            return new JoinerValue((String) o);
        throw new RuntimeException("Unknown MergeValue type specified. Object=" + o.toString());
    }

    @Getter
    @RequiredArgsConstructor
    public static class Cell
    {
        private final String      rawText;
        private final GizmoHandle handle;

        @Setter
        private String computed;

        private final int width;
        private final int height;

        @Setter
        private int x;
        @Setter
        private int y;
    }

    @Builder
    public static class Parameters
    {
        private Vec3d pos;

        @Builder.Default
        private int columns = 2;
        @Builder.Default
        private int rows    = 2;

        @Builder.Default
        private double width  = 1;
        @Builder.Default
        private double height = 1;

        @Builder.Default
        private double rowCellGap    = 0.02D;
        @Builder.Default
        private double columnCellGap = 0.02D;
    }
}
