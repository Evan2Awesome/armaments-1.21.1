package net.armaments.item.custom;

import net.minecraft.entity.LivingEntity;

public interface GunItem {
    float getDamage();

    void shoot(LivingEntity entity);
}