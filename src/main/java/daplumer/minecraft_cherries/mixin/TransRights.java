package daplumer.minecraft_cherries.mixin;

import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SplashTextResourceSupplier.class)
public abstract class TransRights {
    @Inject(method = "get", at = @At("HEAD"),cancellable = true)
    private void injected(CallbackInfoReturnable<SplashTextRenderer> cir){
        cir.setReturnValue(new SplashTextRenderer("Trans Rights!"));
    }
}