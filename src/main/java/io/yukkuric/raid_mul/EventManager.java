package io.yukkuric.raid_mul;

import io.yukkuric.raid_mul.basis.RaidEnforcer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class EventManager {
    public static DamageSource tmp = new DamageSource("create.mechanical_drill")
            .setDamageBypassesArmor().setDamageAllowedInCreativeMode();

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load e) {
        IWorld w = e.getWorld();
        if (!(w instanceof ServerWorld)) return;
        RaidEnforcer.load((ServerWorld) w);
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent e) {
        if (e.phase == TickEvent.Phase.START || !(e.world instanceof ServerWorld)) return;
        RaidEnforcer.tickWorld((ServerWorld) e.world);
    }
}
