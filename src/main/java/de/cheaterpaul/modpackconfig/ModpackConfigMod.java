package de.cheaterpaul.modpackconfig;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod(ModpackConfigMod.MODID)
public class ModpackConfigMod {

    public static final String MODID = "modpackconfig";

    public ModpackConfigMod() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> "", (incoming, isNetwork) -> true));
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CopyConfig::copyClient);
        CopyConfig.copyCommon();
    }

}
