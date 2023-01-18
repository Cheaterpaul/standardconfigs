package de.cheaterpaul.standardconfigs.mixin;

import de.cheaterpaul.standardconfigs.StandardConfigsMod;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Main.class)
public class GameSettingsMixin {

    @Inject(method = "main", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/GameConfiguration;<init>(Lnet/minecraft/client/GameConfiguration$UserInformation;Lnet/minecraft/client/renderer/ScreenSize;Lnet/minecraft/client/GameConfiguration$FolderInformation;Lnet/minecraft/client/GameConfiguration$GameInformation;Lnet/minecraft/client/GameConfiguration$ServerInformation;)V"))
    private static void loadConfigPresets(CallbackInfo ci) {
        StandardConfigsMod.copyConfigs();
    }
}
