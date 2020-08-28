package io.yukkuric.forge_greater_eye;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(ForgeGreaterEye.ID)
public class ForgeGreaterEye {
    public static final String ID = "forge_greater_eye";
    public static final String NAME = "Forge Greater Eye";
    public static final String VERSION = "0.1";
    public static final Random rand = new Random();
    public static final Logger logger = LogManager.getLogger();

    public ForgeGreaterEye() {
        ItemManager.REG.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
