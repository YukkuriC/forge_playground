package io.yukkuric.create_legacy.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.yukkuric.create_legacy.foundation.AbstractToolItem;
import io.yukkuric.create_legacy.enums.AllToolTypes;
import io.yukkuric.create_legacy.enums.AllToolTiers;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;

import java.util.UUID;

import static io.yukkuric.create_legacy.enums.AllToolTypes.*;

public class RoseQuartzTool extends AbstractToolItem {

    public static Multimap<String, AttributeModifier> rangeModifier;
    static final UUID attributeId = UUID.fromString("02304d9a-7f8d-43cb-aa73-fbf334a3d91c"); // mechanical arm conflict

    public RoseQuartzTool(float attackDamageIn, float attackSpeedIn, Item.Properties builder, AllToolTypes... types) {
        super(attackDamageIn, attackSpeedIn, AllToolTiers.ROSE_QUARTZ, builder, types);
        if (rangeModifier == null) {
            rangeModifier = HashMultimap.create();
            rangeModifier.put(PlayerEntity.REACH_DISTANCE.getName(),
                    new AttributeModifier(attributeId, "Range modifier", 3, AttributeModifier.Operation.ADDITION));
        }
    }

    public static class RoseQuartzPickaxe extends RoseQuartzTool {
        public RoseQuartzPickaxe(Properties builder) {
            super(1, -2.8F, builder, PICKAXE);
        }
    }

    public static class RoseQuartzShovel extends RoseQuartzTool {
        public RoseQuartzShovel(Properties builder) {
            super(1.5F, -3.0F, builder, SHOVEL);
        }
    }

    public static class RoseQuartzAxe extends RoseQuartzTool {
        public RoseQuartzAxe(Properties builder) {
            super(5.0F, -3.0F, builder, AXE);
        }
    }

    public static class RoseQuartzSword extends SwordItem {
        public RoseQuartzSword(Properties builder) {
            super(AllToolTiers.ROSE_QUARTZ, 3, -2.4F, builder);
        }
    }
}
