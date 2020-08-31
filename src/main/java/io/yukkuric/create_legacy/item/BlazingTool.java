package io.yukkuric.create_legacy.item;

import io.yukkuric.create_legacy.foundation.AbstractToolItem;
import io.yukkuric.create_legacy.enums.AllToolTypes;
import io.yukkuric.create_legacy.foundation.CreateHelper;
import io.yukkuric.create_legacy.enums.AllToolTiers;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.*;

import static io.yukkuric.create_legacy.enums.AllToolTypes.*;

public class BlazingTool extends AbstractToolItem {

    static FurnaceTileEntity helperFurnace = new FurnaceTileEntity();

    public BlazingTool(float attackDamageIn, float attackSpeedIn, Item.Properties builder, AllToolTypes... types) {
        super(attackDamageIn, attackSpeedIn, AllToolTiers.BLAZING, builder, types);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos,
                                    LivingEntity entityLiving) {
        return shouldTakeDamage(worldIn, stack) ? super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving)
                : true;
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return itemStack.getMaxDamage() - itemStack.getDamage() + 1;
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setFire(2);
        return shouldTakeDamage(attacker.world, stack) ? super.hitEntity(stack, target, attacker) : true;
    }

    static boolean shouldTakeDamage(World world, ItemStack stack) {
        return world.getDimension().getType() != DimensionType.THE_NETHER;
    }

    @Override
    public boolean modifiesDrops() {
        return true;
    }

    @Override
    public void modifyDrops(Collection<ItemStack> drops, IWorld world, BlockPos pos, ItemStack tool, BlockState state) {
        super.modifyDrops(drops, world, pos, tool, state);
        World worldIn = world.getWorld();
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
        if (state == null)
            enchantmentLevel = 0;
        List<ItemStack> smeltedStacks = smeltDrops(drops, worldIn, pos, enchantmentLevel);
        drops.addAll(smeltedStacks);
    }

    public static List<ItemStack> smeltDrops(Collection<ItemStack> drops, World worldIn, BlockPos pos, int enchantmentLevel) {
        helperFurnace.setLocation(worldIn, pos);
        RecipeManager recipeManager = worldIn.getRecipeManager();
        List<ItemStack> smeltedStacks = new ArrayList<>();
        Iterator<ItemStack> dropper = drops.iterator();
        while (dropper.hasNext()) {
            ItemStack stack = dropper.next();
            helperFurnace.setInventorySlotContents(0, stack);
            Optional<FurnaceRecipe> smeltingRecipe =
                    recipeManager.getRecipe(IRecipeType.SMELTING, helperFurnace, worldIn);
            if (!smeltingRecipe.isPresent())
                continue;
            dropper.remove();
            ItemStack out = smeltingRecipe.get().getRecipeOutput().copy();

            float modifier = 1;
            if (stack.getItem() instanceof BlockItem && !(out.getItem() instanceof BlockItem))
                modifier += worldIn.getRandom().nextFloat() * enchantmentLevel;

            out.setCount((int) (out.getCount() * modifier + .4f));
            smeltedStacks.addAll(CreateHelper.multipliedOutput(stack, out));
        }
        return smeltedStacks;
    }

    @Override
    public void spawnParticles(IWorld world, BlockPos pos, ItemStack tool, BlockState state) {
        if (!canHarvestBlock(tool, state))
            return;
        for (int i = 0; i < 10; i++) {
            Vec3d flamePos = CreateHelper.offsetRandomly(CreateHelper.getCenterOf(pos), world.getRandom(), .45f);
            Vec3d smokePos = CreateHelper.offsetRandomly(CreateHelper.getCenterOf(pos), world.getRandom(), .45f);
            world.addParticle(ParticleTypes.FLAME, flamePos.getX(), flamePos.getY(), flamePos.getZ(), 0, .01f, 0);
            world.addParticle(ParticleTypes.SMOKE, smokePos.getX(), smokePos.getY(), smokePos.getZ(), 0, .1f, 0);
        }
    }

    public static class BlazingPickaxe extends BlazingTool {
        public BlazingPickaxe(Properties builder) {
            super(1, -2.8F, builder, PICKAXE);
        }
    }

    public static class BlazingShovel extends BlazingTool {
        public BlazingShovel(Properties builder) {
            super(1.5F, -3.0F, builder, SHOVEL);
        }
    }

    public static class BlazingAxe extends BlazingTool {
        public BlazingAxe(Properties builder) {
            super(5.0F, -3.0F, builder, AXE);
        }
    }
}
