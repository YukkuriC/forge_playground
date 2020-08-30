package io.yukkuric.forge_greater_eye;

import io.yukkuric.forge_greater_eye.item.GreaterEye;
import io.yukkuric.misc.RegHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Function;

public final class ItemManager {
    static final Function<Class<?>, RegistryObject<Item>> regH = RegHelper.Gen(
            Item.class,
            new Class[]{Item.Properties.class},
            new Item.Properties().group(ItemGroup.MISC)
    );

    public static final RegistryObject<Item> greaterEye = regH.apply(GreaterEye.class);

    public static void register(){}
}
