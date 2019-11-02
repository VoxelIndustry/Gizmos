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
import net.voxelindustry.gizmos.handle.TimedGizmoHandle;
import net.voxelindustry.gizmos.render.BoxRenderer;
import net.voxelindustry.gizmos.util.RectSide;
import net.voxelindustry.gizmos.widget.data.CounterValue;
import net.voxelindustry.gizmos.widget.data.IDataHolderGizmo;
import net.voxelindustry.gizmos.widget.data.JoinerValue;
import net.voxelindustry.gizmos.widget.data.MergeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static net.voxelindustry.gizmos.widget.data.Placeholders.PLACEHOLDER_PATTERN;

@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
public class WindowGizmo extends BaseGizmo implements IDataHolderGizmo
{
    private static final double TEXT_SCALE   = 0.0625F / 4;
    private static final double PADDING      = 0.04D;
    private static final double BORDER_WIDTH = 0.02D;

    private Table<Integer, String, MergeValue> placeholders = HashBasedTable.create();

    private List<Line> lines = new ArrayList<>();

    private double maxWidth;
    private double maxHeight;

    private final int textColorRGBA;
    private final int boxColorRGBA;
    private final int progressColorRGBA;
    private final int borderColorRGBA;

    private final RectSide side;
    private final Vec3d    offset;
    private final int      fixedLineCount;

    public WindowGizmo(Parameters parameters)
    {
        super(parameters.pos);
        this.textColorRGBA = parameters.textColorRGBA;
        this.borderColorRGBA = parameters.borderColorRGBA;
        this.boxColorRGBA = parameters.boxColorRGBA;
        this.progressColorRGBA = parameters.progressColorRGBA;
        this.side = parameters.side;
        this.offset = parameters.offset;
        this.fixedLineCount = parameters.fixedLineCount;
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

        drawBox();
        this.useTexture();

        GlStateManager.translate(PADDING, PADDING, 0);
        GlStateManager.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);

        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);

        int currentY = -getHeight() / 2;
        for (Line line : lines)
        {
            // FIXME: convert RGBA to ARGB for minecraft text coloring
            Minecraft.getMinecraft().fontRenderer.drawString(line.getComputed(),
                    0, currentY, textColorRGBA);
            currentY += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        }

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

        if (lines.removeIf(line -> line.getHandle().shouldExpire()))
            maxHeight = getHeight() * TEXT_SCALE;
        return getDrawMode();
    }

    private void drawBox()
    {
        GlStateManager.color(1, 1, 1, 1);

        BoxRenderer.drawColoredQuad(-BORDER_WIDTH * 2, -BORDER_WIDTH * 2, TEXT_SCALE, BORDER_WIDTH * 4, BORDER_WIDTH * 4, borderColorRGBA);
        BoxRenderer.drawColoredEmptyQuad(-BORDER_WIDTH * 4, -BORDER_WIDTH * 4, TEXT_SCALE, BORDER_WIDTH * 8, BORDER_WIDTH * 8, BORDER_WIDTH, borderColorRGBA);

        drawOffsetArrow();

        BoxRenderer.drawColoredEmptyQuad(-0.02D,
                -maxHeight / 2 - PADDING,
                TEXT_SCALE,
                maxWidth + PADDING * 3,
                maxHeight + PADDING * 3,
                0.02D,
                borderColorRGBA);

        getHandle().ifPresent(handle ->
        {
            if (handle instanceof TimedGizmoHandle)
                BoxRenderer.drawColoredProgressEmptyQuad(0,
                        -maxHeight / 2 - PADDING + BORDER_WIDTH,
                        TEXT_SCALE,
                        maxWidth + PADDING * 2,
                        maxHeight + PADDING * 2,
                        0.015D,
                        progressColorRGBA,
                        ((TimedGizmoHandle) handle).getExpirationProgress());
        });
        BoxRenderer.drawColoredQuad(0,
                -maxHeight / 2 - PADDING + BORDER_WIDTH,
                TEXT_SCALE,
                maxWidth + PADDING * 2,
                maxHeight + PADDING * 2,
                boxColorRGBA);
    }

    private void drawOffsetArrow()
    {
        if (side == RectSide.RIGHT || side == RectSide.TOP)
        {
            if (side == RectSide.TOP)
                BoxRenderer.drawColoredElbowLine(0, BORDER_WIDTH * 4, offset.x - BORDER_WIDTH, offset.y, 0, BORDER_WIDTH, borderColorRGBA);
            else
                BoxRenderer.drawColoredElbowLine(BORDER_WIDTH * 4, 0, offset.x - BORDER_WIDTH, offset.y, 0, BORDER_WIDTH, borderColorRGBA);

            GlStateManager.translate(offset.x, offset.y, offset.z);
        }
        else if (side == RectSide.LEFT)
        {
            BoxRenderer.drawColoredElbowLine(-BORDER_WIDTH * 4, 0, offset.x + BORDER_WIDTH * 2 + PADDING, offset.y, 0, BORDER_WIDTH, borderColorRGBA);
            GlStateManager.translate(offset.x - getMaxWidth(), offset.y, offset.z);
        }
        else if (side == RectSide.BOTTOM)
        {
            BoxRenderer.drawColoredElbowLine(0, -BORDER_WIDTH * 4, offset.x - BORDER_WIDTH, offset.y, 0, BORDER_WIDTH, borderColorRGBA);
            GlStateManager.translate(offset.x, offset.y - getHeight(), offset.z);
        }
    }

    @Override
    public DrawMode getDrawMode()
    {
        return DrawMode.MIXED;
    }

    @Override
    public void appendData(String text, GizmoHandle handle, Object... values)
    {
        Line line = new Line(text, handle);
        lines.add(line);

        extractPlaceholders(lines.size() - 1, text, values);

        maxHeight = getHeight() * TEXT_SCALE;

        computeLine(line);
    }

    @Override
    public void removeData(String text)
    {
        int index = lines.indexOf(lines.stream()
                .filter(line -> line.getRawText().equals(text))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown line in WindowGizmo! line=" + text)));
        lines.remove(index);
        placeholders.row(index).clear();

        maxWidth = lines.stream().mapToInt(lineToSize -> Minecraft.getMinecraft().fontRenderer.getStringWidth(lineToSize.getComputed())).max().orElse(0) * TEXT_SCALE;
        maxHeight = lines.size() * Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * TEXT_SCALE;
    }

    @Override
    public <T> void updateValue(String key, T value)
    {
        placeholders.column(key).values().forEach(mergeValue -> mergeValue.merge(value));
        placeholders.column(key).keySet().forEach(index -> computeLine(lines.get(index)));
    }

    public <T> void updateValue(String key, int lineIndex, T value)
    {
        if (lines.size() <= lineIndex)
            return;

        placeholders.get(lineIndex, key).merge(value);

        Line line = lines.get(lineIndex);
        if (line.getHandle() instanceof TimedGizmoHandle)
            ((TimedGizmoHandle) line.getHandle()).reset();
        computeLine(line);
    }

    private void computeLine(Line line)
    {
        final String[] outputLine = {line.getRawText()};

        placeholders.row(lines.indexOf(line)).forEach((key, value) ->
                outputLine[0] = outputLine[0].replace("{{" + key + "}}", value.getValue().toString()));

        line.setComputed(outputLine[0]);

        double lineLength = Minecraft.getMinecraft().fontRenderer.getStringWidth(outputLine[0]) * TEXT_SCALE;
        if (lineLength > maxWidth)
            maxWidth = lineLength;
    }

    private void extractPlaceholders(int index, String line, Object... values)
    {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(line);

        int group = 0;
        while (matcher.find())
        {
            placeholders.put(index, matcher.group(1), getMergeValueFromObject(values[group]));
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

    private int getHeight()
    {
        if (this.fixedLineCount != 0)
            return this.fixedLineCount * Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        return this.lines.size() * Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Line
    {
        private final String      rawText;
        private final GizmoHandle handle;

        @Setter
        private String computed;
    }

    @Builder
    public static class Parameters
    {
        private Vec3d pos;
        @Builder.Default
        private Vec3d offset            = Vec3d.ZERO;
        @Builder.Default
        private int   textColorRGBA     = 0x40404000;
        @Builder.Default
        private int   boxColorRGBA      = 0x00669955;
        @Builder.Default
        private int   progressColorRGBA = 0x00527AFF;
        @Builder.Default
        private int   borderColorRGBA   = 0x999999FF;
        private int   fixedLineCount;

        @Builder.Default
        private RectSide side = RectSide.RIGHT;
    }
}
