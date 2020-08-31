package io.yukkuric.create_legacy.foundation;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.util.text.TextFormatting.*;

public class ItemDescription {

    public static final ItemDescription MISSING = new ItemDescription(null);

    public enum Palette {

        Blue(BLUE, AQUA),
        Green(DARK_GREEN, GREEN),
        Yellow(GOLD, YELLOW),
        Red(DARK_RED, RED),
        Purple(DARK_PURPLE, LIGHT_PURPLE),

        ;

        private Palette(TextFormatting primary, TextFormatting highlight) {
            color = primary;
            hColor = highlight;
        }

        public TextFormatting color;
        public TextFormatting hColor;
    }

    private List<ITextComponent> lines;
    private List<ITextComponent> linesOnShift;
    private List<ITextComponent> linesOnCtrl;
    private Palette palette;

    public ItemDescription(Palette palette) {
        this.palette = palette;
        lines = new ArrayList<>();
        linesOnShift = new ArrayList<>();
        linesOnCtrl = new ArrayList<>();
    }

    public ItemDescription withSummary(String summary) {
        add(linesOnShift, TooltipHelper.cutString(summary, palette.color, palette.hColor));
        add(linesOnShift, "");
        return this;
    }

    public ItemDescription withBehaviour(String condition, String behaviour) {
        add(linesOnShift, GRAY + condition);
        add(linesOnShift, TooltipHelper.cutString(behaviour, palette.color, palette.hColor, 1));
        return this;
    }

    public ItemDescription withControl(String condition, String action) {
        add(linesOnCtrl, GRAY + condition);
        add(linesOnCtrl, TooltipHelper.cutString(action, palette.color, palette.hColor, 1));
        return this;
    }

    public ItemDescription createTabs() {
        boolean hasDescription = !linesOnShift.isEmpty();
        boolean hasControls = !linesOnCtrl.isEmpty();

        if (hasDescription || hasControls) {
            String[] holdKey = Lang.translate("tooltip.holdKey", "$").split("\\$");
            String[] holdKeyOrKey = Lang.translate("tooltip.holdKeyOrKey", "$", "$").split("\\$");
            String keyShift = Lang.translate("tooltip.keyShift");
            String keyCtrl = Lang.translate("tooltip.keyCtrl");
            for (List<ITextComponent> list : Arrays.asList(lines, linesOnShift, linesOnCtrl)) {
                boolean shift = list == linesOnShift;
                boolean ctrl = list == linesOnCtrl;

                if (holdKey.length != 2 || holdKeyOrKey.length != 3) {
                    list.add(0, new StringTextComponent("Invalid lang formatting!"));
                    continue;
                }

                StringBuilder tabBuilder = new StringBuilder();
                tabBuilder.append(DARK_GRAY);
                if (hasDescription && hasControls) {
                    tabBuilder.append(holdKeyOrKey[0]);
                    tabBuilder.append(shift ? palette.hColor : palette.color);
                    tabBuilder.append(keyShift);
                    tabBuilder.append(DARK_GRAY);
                    tabBuilder.append(holdKeyOrKey[1]);
                    tabBuilder.append(ctrl ? palette.hColor : palette.color);
                    tabBuilder.append(keyCtrl);
                    tabBuilder.append(DARK_GRAY);
                    tabBuilder.append(holdKeyOrKey[2]);

                } else {
                    tabBuilder.append(holdKey[0]);
                    tabBuilder.append((hasDescription ? shift : ctrl) ? palette.hColor : palette.color);
                    tabBuilder.append(hasDescription ? keyShift : keyCtrl);
                    tabBuilder.append(DARK_GRAY);
                    tabBuilder.append(holdKey[1]);
                }

                list.add(0, new StringTextComponent(tabBuilder.toString()));
                if (shift || ctrl)
                    list.add(1, new StringTextComponent(""));
            }
        }

        if (!hasDescription)
            linesOnShift = lines;
        if (!hasControls)
            linesOnCtrl = lines;

        return this;
    }

    public static void add(List<ITextComponent> infoList, List<String> textLines) {
        textLines.forEach(s -> add(infoList, s));
    }

    public static void add(List<ITextComponent> infoList, String line) {
        infoList.add(new StringTextComponent(line));
    }

    public Palette getPalette() {
        return palette;
    }

    public List<ITextComponent> addInformation(List<ITextComponent> tooltip) {
        if (Screen.hasShiftDown()) {
            tooltip.addAll(linesOnShift);
            return tooltip;
        }

        if (Screen.hasControlDown()) {
            tooltip.addAll(linesOnCtrl);
            return tooltip;
        }

        tooltip.addAll(lines);
        return tooltip;
    }

}
