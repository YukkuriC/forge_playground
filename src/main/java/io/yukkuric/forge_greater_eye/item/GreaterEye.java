package io.yukkuric.forge_greater_eye.item;

import io.yukkuric.forge_greater_eye.ForgeGreaterEye;
import io.yukkuric.forge_greater_eye.entity.GreaterEyeOrb;
import io.yukkuric.forge_greater_eye.misc.Helper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class GreaterEye extends Item {
    static final Structure[] structureTypes = {
            Structure.STRONGHOLD,
            Structure.VILLAGE,
            Structure.MINESHAFT,
            Structure.SHIPWRECK,
            Structure.PILLAGER_OUTPOST,
            Structure.OCEAN_MONUMENT,
            Structure.WOODLAND_MANSION,
            Structure.DESERT_PYRAMID,
            Structure.JUNGLE_TEMPLE,
    };
    static HashMap<Structure, Structure> nextStruct;
    static boolean prepared = false;

    Structure currentStruct = structureTypes[0];

    public GreaterEye(Properties properties) {
        super(properties);
        if (!prepared) {
            nextStruct = new HashMap<>();
            for (int i = 0; i < structureTypes.length; i++)
                nextStruct.put(structureTypes[i], structureTypes[(i + 1) % structureTypes.length]);

            prepared = true;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (worldIn.isRemote) return ActionResult.resultPass(stack);
        if (playerIn.isSneaking()) {
            currentStruct = nextStruct.get(currentStruct);
            playerIn.sendMessage(Helper.GetLang("item.forge_greater_eye.greater_eye.message1", null, currentStruct.getStructureName()));
        } else {
            playerIn.setActiveHand(handIn);
            if (worldIn instanceof ServerWorld) {
                findStructureAndShoot((ServerWorld) worldIn, playerIn, stack);
                playerIn.swing(handIn, true);
            }
        }
        return ActionResult.resultSuccess(stack);
    }

    void findStructureAndShoot(ServerWorld worldIn, PlayerEntity playerIn, ItemStack stack) {
        if (!worldIn.dimension.isSurfaceWorld()) {
            playerIn.sendMessage(Helper.GetLang("item.forge_greater_eye.greater_eye.403", TextFormatting.DARK_RED, currentStruct.getStructureName()));
            return;
        }
        ChunkGenerator chunkGen = worldIn.getChunkProvider().getChunkGenerator();
        BlockPos plrPos = playerIn.getPosition();
        BlockPos locPos = currentStruct.findNearest(worldIn, chunkGen, plrPos, 100, false);

        if (locPos == null) {
            playerIn.sendMessage(Helper.GetLang("item.forge_greater_eye.greater_eye.404", TextFormatting.DARK_RED, currentStruct.getStructureName()));
            return;
        }
        double
                userX = playerIn.getPosX(),
                userY = playerIn.getPosY() + playerIn.getEyeHeight(),
                userZ = playerIn.getPosZ();

        int structureDistance = MathHelper.floor(getDistance(plrPos.getX(), plrPos.getZ(), locPos.getX(), locPos.getZ()));

        playerIn.sendMessage(Helper.GetLang("item.forge_greater_eye.greater_eye.message3", null, currentStruct.getStructureName(), structureDistance));

        // use success
        GreaterEyeOrb finder = new GreaterEyeOrb(worldIn, userX, userY, userZ);
        finder.moveTowards(locPos);
        worldIn.addEntity(finder);

        worldIn.playSound(userX, userY, userZ, SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL,
                0.5F, 0.4F / (ForgeGreaterEye.rand.nextFloat() * 0.4F + 0.8F), true);
        playerIn.addStat(Stats.ITEM_USED.get(this));
        if (!playerIn.isCreative()) stack.shrink(1);
    }

    private static float getDistance(int x1, int z1, int x2, int z2) {
        int i = x2 - x1;
        int j = z2 - z1;
        return MathHelper.sqrt((float) (i * i + j * j));
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(Helper.GetLang("item.forge_greater_eye.greater_eye.line1", TextFormatting.YELLOW));
        tooltip.add(Helper.GetLang("item.forge_greater_eye.greater_eye.line2", TextFormatting.YELLOW));
        tooltip.add(Helper.GetLang("item.forge_greater_eye.greater_eye.message2", TextFormatting.LIGHT_PURPLE, currentStruct.getStructureName()));
    }
}
