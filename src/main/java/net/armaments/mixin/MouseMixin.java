package net.armaments.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.armaments.item.custom.GunItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.util.math.Smoother;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Final
    private Smoother cursorXSmoother;

    @Shadow
    @Final
    private Smoother cursorYSmoother;

    @Shadow
    private double cursorDeltaX;

    @Shadow
    private double cursorDeltaY;

    @WrapMethod(method = "updateMouse")
    public void armaments$slowMouse(double timeDelta, Operation<Void> original) {
        assert this.client.player != null;
        if (this.client.player.isUsingItem() && this.client.player.getActiveItem().getItem() instanceof GunItem gunItem && gunItem.canADS(this.client.player.getActiveItem(), this.client.player)  && this.client.options.getPerspective().isFirstPerson()) {
            double d = (this.client.options.getMouseSensitivity().getValue() * 0.6F + 0.2F)*0.7;
            double i;
            double j;

            this.cursorXSmoother.clear();
            this.cursorYSmoother.clear();
            i = this.cursorDeltaX * d;
            j = this.cursorDeltaY * d;


            int k = 1;
            if (this.client.options.getInvertYMouse().getValue()) {
                k = -1;
            }

            this.client.getTutorialManager().onUpdateMouse(i, j);
            if (this.client.player != null) {
                this.client.player.changeLookDirection(i, j * k);
            }
        } else {
            original.call(timeDelta);
        }
    }

    /*
    @WrapOperation(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingSpyglass()Z"))
    public boolean armaments$slowMouse(ClientPlayerEntity player, Operation<Boolean> original) {
        return original.call(player) || (player.getActiveItem().getItem() instanceof GunItem gunItem && player.isUsingItem() && gunItem.canADS(player.getActiveItem(), player));
    }
     */
}
