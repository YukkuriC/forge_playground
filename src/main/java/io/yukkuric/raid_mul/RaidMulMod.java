package io.yukkuric.raid_mul;

import io.yukkuric.misc.RegHelper;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(RaidMulMod.ID)
public class RaidMulMod {
    public static final String ID = "raid_mul";
    public static final Logger logger = LogManager.getLogger();

    public RaidMulMod() {
        RegHelper.Init(ID);
        new ItemManager();
    }
}
