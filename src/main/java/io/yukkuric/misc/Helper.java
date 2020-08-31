package io.yukkuric.misc;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class Helper {
    public static ITextComponent GetLang(String lang, @Nullable TextFormatting clr, Object... args) {
        ITextComponent res = new TranslationTextComponent(lang, args);
        if (clr != null) res.setStyle(new Style().setColor(clr));
        return res;
    }

    public static String Camel2Snake(String camel) {
        return Arrays.stream(camel.split("(?<=[a-z])(?=[A-Z])"))
                .map(String::toLowerCase)
                .collect(Collectors.joining("_"));
    }
}
