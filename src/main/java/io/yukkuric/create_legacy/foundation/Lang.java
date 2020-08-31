package io.yukkuric.create_legacy.foundation;

import io.yukkuric.create_legacy.CreateLegacy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Lang {

	public static String translate(String key, Object... args) {
		return createTranslationTextComponent(key, args).getFormattedText();
	}

	public static TranslationTextComponent createTranslationTextComponent(String key, Object... args) {
		return new TranslationTextComponent(CreateLegacy.ID + "." + key, args);
	}

	public static String asId(String name) {
		return name.toLowerCase(Locale.ENGLISH);
	}

}
