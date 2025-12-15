package net.armaments.mixin;

import net.armaments.entity.ModDamageSources;
import net.armaments.util.ModDamages;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageSources.class)
public class DamageSourcesMixin implements ModDamages {
    @Unique private ModDamageSources sources;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void armaments$addSources(DynamicRegistryManager registryManager, CallbackInfo ci) {
        this.sources = new ModDamageSources((DamageSources) (Object) this);
    }

    @Override
    public ModDamageSources armaments$sources() {
        return this.sources;
    }
}
