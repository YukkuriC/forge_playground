package io.yukkuric.create_legacy;

import io.yukkuric.create_legacy.item.BlazingSword;
import io.yukkuric.create_legacy.item.BlazingTool;
import io.yukkuric.create_legacy.item.RoseQuartzTool;
import io.yukkuric.create_legacy.item.ShadowSteelTool;
import io.yukkuric.misc.RegHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Function;

public class ItemManager {
    // item registry
    static final Function<Object[], Function<Class<?>, RegistryObject<Item>>>
            regMaster = RegHelper.GenGen(Item.class, new Class[]{Item.Properties.class});
    static final Function<Class<?>, RegistryObject<Item>>
            itemRegTool = regMaster.apply(new Object[]{new Item.Properties().group(ItemGroup.TOOLS)}),
            itemRegWeapon = regMaster.apply(new Object[]{new Item.Properties().group(ItemGroup.COMBAT)});

    public static final RegistryObject<Item>
            blazingPickaxe = itemRegTool.apply(BlazingTool.BlazingPickaxe.class),
            blazingShovel = itemRegTool.apply(BlazingTool.BlazingShovel.class),
            blazingAxe = itemRegTool.apply(BlazingTool.BlazingAxe.class),
            blazingSword = itemRegWeapon.apply(BlazingSword.class);

    public static final RegistryObject<Item>
            roseQuartzPickaxe = itemRegTool.apply(RoseQuartzTool.RoseQuartzPickaxe.class),
            roseQuartzShovel = itemRegTool.apply(RoseQuartzTool.RoseQuartzShovel.class),
            roseQuartzAxe = itemRegTool.apply(RoseQuartzTool.RoseQuartzAxe.class),
            roseQuartzSword = itemRegWeapon.apply(RoseQuartzTool.RoseQuartzSword.class);

    public static final RegistryObject<Item>
            shadowSteelPickaxe = itemRegTool.apply(ShadowSteelTool.ShadowSteelPickaxe.class),
            shadowSteelMattock = itemRegTool.apply(ShadowSteelTool.ShadowSteelMattock.class),
            shadowSteelSword = itemRegWeapon.apply(ShadowSteelTool.ShadowSteelSword.class);

    public static final RegistryObject<Item> zincHandle = RegHelper.DefReg(Item.class)
            .register("zinc_handle", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
}
