package io.yukkuric.misc;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RegHelper {
    static IEventBus modBus;
    static String modId;
    static HashMap<Class<?>, DeferredRegister<?>> defRegCache;

    public static void Init(String modid) {
        modId = modid;
        modBus = FMLJavaModLoadingContext.get().getModEventBus();
        defRegCache = new HashMap<>();
    }

    public static <bC extends IForgeRegistryEntry<bC>> Function<Class<?>, RegistryObject<bC>>
    Gen(Class<bC> baseCls, Class<?>[] constructors, Object... params) {
        // pick DeferredRegister
        DeferredRegister<bC> defReg;
        if (!defRegCache.containsKey(baseCls)) {
            defReg = new DeferredRegister<>(RegistryManager.ACTIVE.getRegistry(baseCls), modId);
            defRegCache.put(baseCls, defReg);
            defReg.register(modBus);
        } else defReg = (DeferredRegister<bC>) defRegCache.get(baseCls);

        // gen function
        return (Class<?> regCls) -> {
            // snake_case from CamelCase
            String idGen = Arrays.stream(regCls.getSimpleName().split("(?<=[a-z])(?=[A-Z])"))
                    .map(String::toLowerCase)
                    .collect(Collectors.joining("_"));

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
