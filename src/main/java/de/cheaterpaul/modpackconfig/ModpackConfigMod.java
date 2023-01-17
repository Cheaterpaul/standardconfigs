package de.cheaterpaul.modpackconfig;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(ModpackConfigMod.MODID)
public class ModpackConfigMod {

    public static final String MODID = "modpackconfig";

    public ModpackConfigMod() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CopyConfig::copyClient);
        CopyConfig.copyCommon();
    }

}
