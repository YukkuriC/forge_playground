package io.yukkuric.create_legacy.item;

import io.yukkuric.create_legacy.enums.AllToolTiers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class BlazingSword extends SwordItem {

    public BlazingSword(Properties builder) {
        super(AllToolTiers.BLAZING, 3, -2.4F, builder);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setFire(2);
        return BlazingTool.shouldTakeDamage(attacker.world, stack) ? super.hitEntity(stack, target, attacker)
                : true;
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return itemStack.getMaxDamage() - itemStack.getDamage() + 1;
    }

}
