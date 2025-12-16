package net.armaments.item.custom;

import net.armaments.item.ModItems;
import net.armaments.item.component.AmmoComponent;
import net.armaments.item.component.ModDataComponents;
import net.armaments.util.Functions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface GunItem {
    float getDamage(ItemStack stack);
    int getMaxAmmo(ItemStack stack);
    Item ammoItem(ItemStack stack);

    void shoot(PlayerEntity shooter, ItemStack gun);

    default int getAmmo(ItemStack stack) {
        return Math.min(this.getMaxAmmo(stack), stack.getOrDefault(ModDataComponents.AMMO, AmmoComponent.DEFAULT).ammo());
    }

    default void reload(ItemStack gun, LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            if (player.isInCreativeMode() || !Functions.getAmmo(ModItems.CREATIVE_AMMO_POUCH, player).isEmpty()) gun.set(ModDataComponents.AMMO, new AmmoComponent(this.getMaxAmmo(gun)));
            else while (this.getAmmo(gun) < this.getMaxAmmo(gun) && Functions.getAmmo(this.ammoItem(gun), player) instanceof ItemStack ammo && !ammo.isEmpty()) {
                int magSize = this.getMaxAmmo(gun) - this.getAmmo(gun);
                int loading = Math.min(this.getMaxAmmo(gun), Math.min(magSize, ammo.getCount()));
                ammo.decrementUnlessCreative(loading, player);
                gun.set(ModDataComponents.AMMO, new AmmoComponent(this.getAmmo(gun) + loading));
            }
        } else gun.set(ModDataComponents.AMMO, new AmmoComponent(this.getMaxAmmo(gun)));
    }
}