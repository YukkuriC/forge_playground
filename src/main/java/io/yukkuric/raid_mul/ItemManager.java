package io.yukkuric.raid_mul;

import io.yukkuric.misc.RegHelper;
import io.yukkuric.raid_mul.item.TotemIronGolem;
import io.yukkuric.raid_mul.item.TotemSnowGolem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Function;

public class ItemManager {
    static final Function<Class<?>, RegistryObject<Item>> reg = RegHelper.Gen(
            Item.class,
            new Class[]{Item.Properties.class},
            new Item.Properties().group(ItemGroup.COMBAT)
    );

    static final RegistryObject<Item>[] golems = new RegistryObject[]{
            reg.apply(TotemIronGolem.class),
            reg.apply(TotemSnowGolem.class),
    };
}
