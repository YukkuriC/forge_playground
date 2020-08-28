package io.yukkuric.forge_greater_eye.entity;

import io.yukkuric.forge_greater_eye.ItemManager;
import net.minecraft.entity.item.EyeOfEnderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GreaterEyeOrb extends EyeOfEnderEntity {
    public GreaterEyeOrb(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public ItemStack getItem() {
        return ItemManager.greaterEye.get().getDefaultInstance();
    }
}
