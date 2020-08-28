package io.yukkuric.forge_greater_eye;

import io.yukkuric.forge_greater_eye.item.GreaterEye;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class ItemManager {
    public static final DeferredRegister<Item> REG = new DeferredRegister<>(ForgeRegistries.ITEMS, ForgeGreaterEye.ID);
    public static final RegistryObject<Item> greaterEye = regHelper(GreaterEye.class);

    static RegistryObject<Item> regHelper(Class<?> itemCls) {
        // auto-gen register id
        String idGen = Arrays.stream(itemCls.getSimpleName().split("(?<=[a-z])(?=[A-Z])"))
                .map(String::toLowerCase)
                .collect(Collectors.joining("_"));

        return REG.register(idGen, () -> {
            Item res = null;
            try {
                Object instance = itemCls
                        .getConstructor(Item.Properties.class)
                        .newInstance(Prop());
                res = (Item) instance;
            } catch (Exception ignored) {
            }
            return res;
        });
    }

    static Item.Properties Prop() {
        return new Item.Properties().group(ItemGroup.MISC);
    }
}
