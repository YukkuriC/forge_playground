package io.yukkuric.raid_mul.item;

import io.yukkuric.raid_mul.RaidMulMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TotemBase extends Item {
    EntityType<?> toSummon;

    public TotemBase(Properties properties, EntityType<?> mob) {
        super(properties);
        toSummon = mob;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (!world.isRemote) {
            BlockPos pos = context.getPos();
            PlayerEntity plr = context.getPlayer();
            ItemStack item = context.getItem();

            Entity mob = toSummon.create(world);
            mob.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            world.addEntity(mob);

            item.shrink(1);
        }

        return ActionResultType.SUCCESS;
    }
}
