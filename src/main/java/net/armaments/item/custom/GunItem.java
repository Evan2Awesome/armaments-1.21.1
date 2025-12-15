package net.armaments.item.custom;

import net.armaments.item.component.AmmoComponent;
import net.armaments.item.component.ModDataComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface GunItem {
    float getDamage(ItemStack stack);
    int getMaxAmmo(ItemStack stack);

    default int getAmmo(ItemStack stack) {
        return Math.min(this.getMaxAmmo(stack), stack.getOrDefault(ModDataComponents.AMMO, AmmoComponent.DEFAULT).ammo());
    }

    void shoot(PlayerEntity shooter, ItemStack gun);
}