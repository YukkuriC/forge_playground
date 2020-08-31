package io.yukkuric.misc;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;

import java.util.HashMap;
import java.util.function.Function;

public class RegHelper {
    static IEventBus modBus;
    static String modId;
    static HashMap<Class<?>, DeferredRegister<?>> defRegCache;

    public static void Init(String modid) {
        modId = modid;
        modBus = FMLJavaModLoadingContext.get().getModEventBus();
        defRegCache = new HashMap<>();
    }

    // pick DeferredRegister
    public static <bC extends IForgeRegistryEntry<bC>> DeferredRegister<bC> DefReg(Class<bC> baseCls) {
        DeferredRegister<bC> defReg;
        if (!defRegCache.containsKey(baseCls)) {
            defReg = new DeferredRegister<>(RegistryManager.ACTIVE.getRegistry(baseCls), modId);
            defRegCache.put(baseCls, defReg);
            defReg.register(modBus);
        } else defReg = (DeferredRegister<bC>) defRegCache.get(baseCls);
        return defReg;
    }

    // (register from class) register
    public static <bC extends IForgeRegistryEntry<bC>> Function<Object[], Function<Class<?>, RegistryObject<bC>>>
    GenGen(Class<bC> baseCls, Class<?>[] constructors) {
        Function<Object[], Function<Class<?>, RegistryObject<bC>>> x=(Object... params) -> Gen(baseCls, constructors, params);
        return (Object... params) -> Gen(baseCls, constructors, params);
    }

    // register from class
    public static <bC extends IForgeRegistryEntry<bC>> Function<Class<?>, RegistryObject<bC>>
    Gen(Class<bC> baseCls, Class<?>[] constructors, Object... params) {
        DeferredRegister<bC> defReg = DefReg(baseCls);

        // gen function
        return (Class<?> regCls) -> {
            String idGen = Helper.Camel2Snake(regCls.getSimpleName());
            return defReg.register(idGen, () -> {
                bC res = null;
                try {
                    res = (bC) (regCls
                            .getConstructor(constructors)
                            .newInstance(params));
                } catch (Exception e) {
                }
                return res;
            });
        };
    }
}
