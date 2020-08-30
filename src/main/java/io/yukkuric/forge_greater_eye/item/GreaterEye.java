package io.yukkuric.forge_greater_eye.item;

import io.yukkuric.forge_greater_eye.ForgeGreaterEye;
import io.yukkuric.forge_greater_eye.entity.GreaterEyeOrb;
import io.yukkuric.misc.Helper;
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
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class GreaterEye extends Item {
    static final String[] structOverworld = {
            "Mineshaft",
            "Pillager_Outpost",
            "Stronghold",
            "Jungle_Pyramid",
            "Ocean_Ruin",
            "Desert_Pyramid",
            "Igloo",
            "Swamp_Hut",
            "Monument",
            "Mansion",
            "Buried_Treasure",
            "Shipwreck",
            "Village",
    };
    static final String[] structNether = {
            "Fortress",
    };
    static final String[] structEnd = {
            "EndCity",
    };
    static final HashMap<DimensionType, String[]> structPool = new HashMap<>();
    static final HashMap<DimensionType, Integer> structIndex = new HashMap<>();
    static boolean prepared = false;

    @Nullable
    static String getCurrentStructure(DimensionType dim) {
        if (!structPool.containsKey(dim)) return null;
        return structPool.get(dim)[structIndex.get(dim)];
    }

    static boolean nextStructure(DimensionType dim) {
        if (!structPool.containsKey(dim)) return false;
        int size = structPool.get(dim).length;
        structIndex.put(dim, (structIndex.get(dim) + 1) % size);
        return true;
    }

    public GreaterEye(Properties properties) {
        super(properties);

        // init structures
        if (!prepared) {
            structPool.put(DimensionType.OVERWORLD, structOverworld);
            structPool.put(DimensionType.THE_NETHER, structNether);
            structPool.put(DimensionType.THE_END, structEnd);
            for (DimensionType dim : structPool.keySet())
                structIndex.put(dim, 0);
            prepared = true;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (worldIn.isRemote) return ActionResult.resultPass(stack);
        DimensionType dim = worldIn.dimension.getType();
        if (playerIn.isSneaking()) {
            if (nextStructure(dim))
                playerIn.sendMessage(Helper.GetLang("item.forge_greater_eye.greater_eye.message1", null, getCurrentStructure(dim)));
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
        // validate dimension
        DimensionType dim = worldIn.dimension.getType();
        if (!structPool.containsKey(dim)) {
            playerIn.sendMessage(Helper.GetLang("item.forge_greater_eye.greater_eye.403", TextFormatting.DARK_RED));
            return;
        }
        String structName = getCurrentStructure(dim);

        // find structure
        BlockPos plrPos = playerIn.getPosition();
        BlockPos locPos = worldIn.findNearestStructure(structName, plrPos, 100, false);
        if (locPos == null) {
            playerIn.sendMessage(Helper.GetLang("item.forge_greater_eye.greater_eye.404", TextFormatting.DARK_RED, structName));
            return;
        }

        double
                userX = playerIn.getPosX(),
                userY = playerIn.getPosY() + playerIn.getEyeHeight(),
                userZ = playerIn.getPosZ();

        int structureDistance = MathHelper.floor(getDistance(plrPos.getX(), plrPos.getZ(), locPos.getX(), locPos.getZ()));

        playerIn.sendMessage(Helper.GetLang("item.forge_greater_eye.greater_eye.message3", null, structName, structureDistance));

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
        String struct = null;
        if (world != null)
            struct = getCurrentStructure(world.dimension.getType());
        if (struct != null)
            tooltip.add(Helper.GetLang("item.forge_greater_eye.greater_eye.message2", TextFormatting.LIGHT_PURPLE, struct));
    }
}
