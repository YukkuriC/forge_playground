package io.yukkuric.create_legacy;

import io.yukkuric.create_legacy.foundation.TooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvents {

    private static final String itemPrefix = "item." + CreateLegacy.ID;

    @SubscribeEvent
    public static void addToItemTooltip(ItemTooltipEvent event) {
        if (Minecraft.getInstance().player == null)
            return;

        ItemStack stack = event.getItemStack();
        String translationKey = stack.getItem().getTranslationKey(stack);
        if (!translationKey.startsWith(itemPrefix))
            return;

        if (TooltipHelper.hasTooltip(stack)) {
            List<ITextComponent> itemTooltip = event.getToolTip();
            List<ITextComponent> toolTip = new ArrayList<>();
            toolTip.add(itemTooltip.remove(0));
            TooltipHelper.getTooltip(stack).addInformation(toolTip);
            itemTooltip.addAll(0, toolTip);
        }

    }

}
