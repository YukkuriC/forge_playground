package io.yukkuric.create_legacy.item;

import io.yukkuric.create_legacy.foundation.AbstractToolItem;
import io.yukkuric.create_legacy.enums.AllToolTypes;
import io.yukkuric.create_legacy.foundation.CreateHelper;
import io.yukkuric.create_legacy.enums.AllToolTiers;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;

import java.util.Collection;

import static io.yukkuric.create_legacy.enums.AllToolTypes.*;

public class ShadowSteelTool extends AbstractToolItem {

    public ShadowSteelTool(float attackDamageIn, float attackSpeedIn, Item.Properties builder, AllToolTypes... types) {
        super(attackDamageIn, attackSpeedIn, AllToolTiers.SHADOW_STEEL, builder, types);
    }

    @Override
    public boolean modifiesDrops() {
        return true;
    }

    @Override
    public void modifyDrops(Collection<ItemStack> drops, IWorld world, BlockPos pos, ItemStack tool, BlockState state) {
        drops.clear();
    }

    @Override
    public void spawnParticles(IWorld world, BlockPos pos, ItemStack tool, BlockState state) {
        if (!canHarvestBlock(tool, state))
            return;
        Vec3d smokePos = CreateHelper.offsetRandomly(CreateHelper.getCenterOf(pos), world.getRandom(), .15f);
        world.addParticle(ParticleTypes.SMOKE, smokePos.getX(), smokePos.getY(), smokePos.getZ(), 0, .01f, 0);
    }

    public static class ShadowSteelPickaxe extends ShadowSteelTool {
        public ShadowSteelPickaxe(Properties builder) {
            super(2.5F, -2.0F, builder, PICKAXE);
        }
    }

    public static class ShadowSteelMattock extends ShadowSteelTool {
        public ShadowSteelMattock(Properties builder) {
            super(2.5F, -1.5F, builder, SHOVEL, AXE, HOE);
        }
    }

    public static class ShadowSteelSword extends SwordItem {
        public ShadowSteelSword(Properties builder) {
            super(AllToolTiers.SHADOW_STEEL, 3, -2.4F, builder);
        }
    }
}
