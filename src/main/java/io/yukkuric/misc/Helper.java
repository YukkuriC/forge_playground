package io.yukkuric.misc;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public final class Helper {
    public static ITextComponent GetLang(String lang, @Nullable TextFormatting clr, Object... args) {
        ITextComponent res = new TranslationTextComponent(lang, args);
        if (clr != null) res.setStyle(new Style().setColor(clr));
        return res;
    }
}
