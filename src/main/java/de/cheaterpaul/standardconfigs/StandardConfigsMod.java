package de.cheaterpaul.standardconfigs;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod(StandardConfigsMod.MODID)
public class StandardConfigsMod {

    public static final String MODID = "standardconfigs";

    public StandardConfigsMod() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> "", (incoming, isNetwork) -> true));
        copyConfigs();
    }

    private static boolean copied;

    /**
     * Since some mods force the config to be loaded early on or are using a different config system, we need to copy the config files as early as possible.
     */
    public static void copyConfigs() {
        if(copied) return;
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CopyConfig::copyClient);
        CopyConfig.copyCommon();
        copied = true;
    }

}
