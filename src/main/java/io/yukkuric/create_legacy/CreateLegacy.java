package io.yukkuric.create_legacy;

import io.yukkuric.create_legacy.enums.AllPackets;
import io.yukkuric.misc.RegHelper;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(CreateLegacy.ID)
public class CreateLegacy {
    public static final String ID = "create_legacy";
    public static final Random rand = new Random();
    public static final Logger logger = LogManager.getLogger();

    public CreateLegacy() {
        RegHelper.Init(ID);
        AllPackets.registerPackets();
        new ItemManager();
    }
}
