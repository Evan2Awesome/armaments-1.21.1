package net.armaments.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface GunItem {
    float getDamage();

    void shoot(PlayerEntity shooter, ItemStack gun);
}