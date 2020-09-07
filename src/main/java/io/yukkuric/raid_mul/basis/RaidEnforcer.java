package io.yukkuric.raid_mul.basis;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.raid.RaidManager;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RaidEnforcer {
    // ======== static ========

    // config
    static final int nGroup = 3;
    static final int checkerGap = 20;
    static final float forceRainRate = 0.1f;

    // containers
    static Map<Integer, Raid> rawPool;
    static Map<Integer, RaidEnforcer> enforcerPool;

    // vars
    static int tickSkipper = 0;

    public static void load(ServerWorld world) {
        if (world.dimension.getType() != DimensionType.OVERWORLD) return;

        // init params
        rawPool = null;
        enforcerPool = null;

        // grab Raid pool
        RaidManager manager = world.getRaids();
        rawPool = manager.byId;
        enforcerPool = new HashMap<>();
        for (Map.Entry<Integer, Raid> pair : rawPool.entrySet())
            enforcerPool.put(pair.getKey(), new RaidEnforcer(pair.getValue(), world));
    }

    public static void tickWorld(ServerWorld world) {
        if (rawPool == null || ++tickSkipper < checkerGap) return;
        tickSkipper = 0;

        // remove inactive raids
        ArrayList<Integer> tmp = new ArrayList<>();
        for (Map.Entry<Integer, RaidEnforcer> pair : enforcerPool.entrySet())
            if (!pair.getValue().isActive()) tmp.add(pair.getKey());
        for (int i : tmp)
            enforcerPool.remove(i);

        // add new raids
        for (Map.Entry<Integer, Raid> pair : rawPool.entrySet()) {
            int key = pair.getKey();
            Raid raid = pair.getValue();
            if ((!enforcerPool.containsKey(key)) && raid.isActive())
                enforcerPool.put(key, new RaidEnforcer(raid, world));
        }

        // update all enforcers
        for (RaidEnforcer e : enforcerPool.values()) e.tick();
    }

    // ======== object ========
    Raid raid;
    ServerWorld world;
    int currWave;

    public RaidEnforcer(Raid target, ServerWorld serverWorld) {
        this.raid = target;
        world = serverWorld;

        // bind monitors
        currWave = raid.getGroupsSpawned();
    }

    public boolean isActive() {
        return raid.isActive();
    }

    public void tick() {
        // update wave ticker
        int newWave = raid.getGroupsSpawned();
        if (newWave != currWave) {
            currWave = newWave;
            extraWave();
        }
    }

    void extraWave() {
        BlockPos[] spawns = getSpawns(nGroup);
        for (BlockPos pos : spawns) {
            if (pos == null || world.rand.nextFloat() < forceRainRate) spawnRain();
            else spawnExtra(pos);
        }
        raid.updateBarPercentage();
        world.getRaids().markDirty();
    }

    // ======== helpers ========

    BlockPos getSpawn() {
        for (int i = 0; i <= 2; i++) {
            BlockPos cur = raid.findRandomSpawnPos(i, 3);
            if (cur != null) return cur;
        }
        return null;
    }

    BlockPos[] getSpawns(int n) {
        BlockPos[] res = new BlockPos[n];
        for (int i = 0; i < n; i++)
            res[i] = getSpawn();
        return res;
    }

    int getSpawnCount(Raid.WaveMember member) {
        return member.waveCounts[Math.min(currWave, member.waveCounts.length - 1)];
    }

    void spawnRain() {
        BlockPos center = raid.getCenter();
        for (Raid.WaveMember member : Raid.WaveMember.VALUES) {
            int spawnCount = getSpawnCount(member);
            for (int i = 0; i < spawnCount; ++i) {
                float
                        dist = world.rand.nextFloat() * 32 + 32,
                        r = world.rand.nextFloat() * ((float) Math.PI * 2F),
                        dx = MathHelper.cos(r) * dist + 0.5f,
                        dz = MathHelper.sin(r) * dist + 0.5f;
                floatDrop(member.type, center.getX() + dx, center.getY() + 32, center.getZ() + dz);
            }
        }
    }

    <T extends AbstractRaiderEntity> void floatDrop(EntityType<T> type, double x, double y, double z) {
        AbstractRaiderEntity enemy = type.create(world);
        if (enemy == null) return;
        enemy.setPosition(x, y + 32 + 32 * world.rand.nextFloat(), z);
        enemy.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 600));
        raid.joinRaid(currWave, enemy, null, true);
        enemy.onInitialSpawn(world, world.getDifficultyForLocation(raid.getCenter()), SpawnReason.EVENT, null, null);
        world.addEntity(enemy);
    }

    void spawnExtra(BlockPos target) {
        for (Raid.WaveMember member : Raid.WaveMember.VALUES) {
            int spawnCount = getSpawnCount(member);
            int riderCycle = 0;

            for (int i = 0; i < spawnCount; ++i) {
                AbstractRaiderEntity entity = member.type.create(this.world);
                raid.joinRaid(currWave, entity, target, false);
                if (member.type == EntityType.RAVAGER) {
                    AbstractRaiderEntity rider = null;
                    if (currWave == raid.getWaves(Difficulty.NORMAL)) {
                        rider = EntityType.PILLAGER.create(this.world);
                    } else if (currWave >= raid.getWaves(Difficulty.HARD)) {
                        if (riderCycle == 0) {
                            rider = EntityType.EVOKER.create(this.world);
                        } else {
                            rider = EntityType.VINDICATOR.create(this.world);
                        }
                    }

                    ++riderCycle;
                    if (rider != null) {
                        raid.joinRaid(currWave, rider, target, false);
                        rider.moveToBlockPosAndAngles(target, 0.0F, 0.0F);
                        rider.startRiding(entity);
                    }
                }
            }
        }
    }
}
