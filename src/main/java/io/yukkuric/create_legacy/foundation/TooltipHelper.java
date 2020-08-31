package io.yukkuric.create_legacy.foundation;

import com.mojang.bridge.game.Language;
import io.yukkuric.create_legacy.enums.AllToolTiers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TooltipHelper {

    public static final int maxCharsPerLine = 35;
    public static final Map<String, ItemDescription> cachedTooltips = new HashMap<>();
    public static Language cachedLanguage;

    public static List<String> cutString(String s, TextFormatting defaultColor, TextFormatting highlightColor) {
        return cutString(s, defaultColor, highlightColor, 0);
    }

    public static List<String> cutString(String s, TextFormatting defaultColor, TextFormatting highlightColor,
                                         int indent) {
        String lineStart = defaultColor.toString();
        for (int i = 0; i < indent; i++)
            lineStart += " ";

        // Apply markup
        String markedUp = s.replaceAll("_([^_]+)_", highlightColor + "$1" + defaultColor);

        String[] words = markedUp.split(" ");
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder(lineStart);
        String word;
        boolean firstWord = true;
        boolean lastWord;

        // Apply hard wrap
        for (int i = 0; i < words.length; i++) {
            word = words[i];
            lastWord = i == words.length - 1;

            if (!lastWord && !firstWord && currentLine.length() + word.length() > maxCharsPerLine) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(lineStart);
                firstWord = true;
            }

            currentLine.append((firstWord ? "" : " ") + word);
            firstWord = false;
        }

        if (!firstWord) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    private static void checkLocale() {
        Language currentLanguage = Minecraft.getInstance().getLanguageManager().getCurrentLanguage();
        if (cachedLanguage != currentLanguage) {
            cachedTooltips.clear();
            cachedLanguage = currentLanguage;
        }
    }

    public static boolean hasTooltip(ItemStack stack) {
        checkLocale();

        String key = getTooltipTranslationKey(stack);
        if (cachedTooltips.containsKey(key))
            return cachedTooltips.get(key) != ItemDescription.MISSING;
        return findTooltip(stack);
    }

    public static ItemDescription getTooltip(ItemStack stack) {
        checkLocale();
        String key = getTooltipTranslationKey(stack);
        if (cachedTooltips.containsKey(key)) {
            ItemDescription itemDescription = cachedTooltips.get(key);
            if (itemDescription != ItemDescription.MISSING)
                return itemDescription;
        }
        return null;
    }

    private static boolean findTooltip(ItemStack stack) {
        String key = getTooltipTranslationKey(stack);
        if (I18n.hasKey(key)) {
            cachedTooltips.put(key, buildToolTip(key, stack));
            return true;
        }
        cachedTooltips.put(key, ItemDescription.MISSING);
        return false;
    }

    private static ItemDescription buildToolTip(String translationKey, ItemStack stack) {
        ItemDescription tooltip = new ItemDescription(ItemDescription.Palette.Purple);
        String summaryKey = translationKey + ".summary";

        // Summary
        if (I18n.hasKey(summaryKey))
            tooltip = tooltip.withSummary(I18n.format(summaryKey));

        // Behaviours
        for (int i = 1; i < 100; i++) {
            String conditionKey = translationKey + ".condition" + i;
            String behaviourKey = translationKey + ".behaviour" + i;
            if (!I18n.hasKey(conditionKey))
                break;
            tooltip.withBehaviour(I18n.format(conditionKey), I18n.format(behaviourKey));
        }

        // Controls
        for (int i = 1; i < 100; i++) {
            String controlKey = translationKey + ".control" + i;
            String actionKey = translationKey + ".action" + i;
            if (!I18n.hasKey(controlKey))
                break;
            tooltip.withControl(I18n.format(controlKey), I18n.format(actionKey));
        }

        return tooltip.createTabs();
    }

    public static String getTooltipTranslationKey(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof TieredItem) {
            TieredItem tieredItem = (TieredItem) stack.getItem();
            if (tieredItem.getTier() instanceof AllToolTiers) {
                AllToolTiers allToolTiers = (AllToolTiers) tieredItem.getTier();
                return "tool.create_legacy." + Lang.asId(allToolTiers.name()) + ".tooltip";
            }
        }

        return stack.getItem().getTranslationKey(stack) + ".tooltip";
    }

}
