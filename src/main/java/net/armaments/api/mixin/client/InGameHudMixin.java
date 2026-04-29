package net.armaments.api.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.armaments.api.client.event.HudRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;

    @Unique private HudRenderEvents.AttackIndicatorPack pack = HudRenderEvents.AttackIndicatorPack.DEFAULT;

    @WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0))
    private void armaments_api$renderCrosshair(DrawContext context, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        Identifier newTexture = HudRenderEvents.MODIFY_CROSSHAIR.invoker().modify(this.client, texture);
        original.call(context, newTexture, x, y, width, height);
        HudRenderEvents.RENDER_CROSSHAIR_ADDITION.invoker().render(this.client, context, newTexture, x, y, width, height);
        this.pack = HudRenderEvents.MODIFY_ATTACK_INDICATOR.invoker().modify(this.client, HudRenderEvents.AttackIndicatorPack.DEFAULT, newTexture);
    }

    @WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1))
    private void armaments_api$indicatorFull(DrawContext context, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        original.call(context, this.pack.full(), x, y, width, height);
    }

    @WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 2))
    private void armaments_api$indicatorBackground(DrawContext context, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        original.call(context, this.pack.background(), x, y, width, height);
    }

    @WrapOperation(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIII)V"))
    private void armaments_api$indicatorProgress(DrawContext context, Identifier texture, int i, int j, int k, int l, int x, int y, int width, int height, Operation<Void> original) {
        original.call(context, this.pack.progress(), i, j, k, l, x, y, width, height);
    }
}
