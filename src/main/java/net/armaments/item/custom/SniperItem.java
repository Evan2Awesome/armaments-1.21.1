package net.armaments.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SniperItem extends AbstractGunItem implements GunItem {
    public SniperItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return user.isSneaky() ? 72000 : 50;
    }

    @Override
    public float getDamage(ItemStack stack, LivingEntity shooter) {
        return 15;
    }

    @Override
    public int getMaxAmmo(ItemStack stack) {
        return 4;
    }

    @Override
    public Item ammoItem(ItemStack stack) {
        return null;
    }

    @Override
    public void shoot(PlayerEntity shooter, ItemStack gun) {}
}
