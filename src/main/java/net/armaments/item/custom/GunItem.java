package net.armaments.item.custom;

import net.armaments.item.ModItems;
import net.armaments.item.component.AmmoPouchComponent;
import net.armaments.item.component.ModDataComponents;
import net.armaments.util.Functions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;

public interface GunItem {
    float getDamage(ItemStack stack, LivingEntity shooter);
    int getMaxAmmo(ItemStack stack);
    int getReloadTime(ItemStack stack);
    float getKickback(ItemStack stack);
    Item ammoItem(ItemStack stack);
    float get_ads_zoom(ItemStack stack);

    default void tryShoot(PlayerEntity shooter, ItemStack gun) {if (this.canShoot(shooter, gun)) this.shoot(shooter, gun);}
    void shoot(PlayerEntity shooter, ItemStack gun);

    default int getAmmo(ItemStack stack) {
        return Math.min(this.getMaxAmmo(stack), stack.getOrDefault(ModDataComponents.AMMO, 0));
    }

    default void reload(ItemStack gun, LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            if (player.isInCreativeMode() || !Functions.getAmmo(ModItems.CREATIVE_AMMO_POUCH, player).isEmpty()) gun.set(ModDataComponents.AMMO, this.getMaxAmmo(gun));
            else {
                List<ItemStack> pouches = Functions.getOfItem(ModItems.AMMO_POUCH, player);
                if (!pouches.isEmpty()) {
                    for (ItemStack pouch : pouches) {
                        if (pouch.get(ModDataComponents.AMMO_POUCH) instanceof AmmoPouchComponent component) {
                            int magSize = this.getMaxAmmo(gun) - this.getAmmo(gun);
                            AmmoPouchComponent.Builder builder = component.builder();
                            int loading = Math.min(this.getMaxAmmo(gun), Math.min(magSize, builder.removeOfItem(this.ammoItem(gun), magSize)));
                            pouch.set(ModDataComponents.AMMO_POUCH, builder.build());
                            gun.set(ModDataComponents.AMMO, this.getAmmo(gun) + loading);
                        }
                    }
                }
                while (this.getAmmo(gun) < this.getMaxAmmo(gun) && Functions.getAmmo(this.ammoItem(gun), player) instanceof ItemStack ammo && !ammo.isEmpty()) {
                    int magSize = this.getMaxAmmo(gun) - this.getAmmo(gun);
                    int loading = Math.min(this.getMaxAmmo(gun), Math.min(magSize, ammo.getCount()));
                    ammo.decrementUnlessCreative(loading, player);
                    gun.set(ModDataComponents.AMMO, this.getAmmo(gun) + loading);
                }
            }
        } else gun.set(ModDataComponents.AMMO, this.getMaxAmmo(gun));
    }

    default boolean canShoot(PlayerEntity player, ItemStack stack) {
        return !player.isSpectator() && player.getAttackCooldownProgress(1f) == 1f && this.getAmmo(stack) > 0 && !player.getItemCooldownManager().isCoolingDown(stack.getItem());
    }

    default boolean fullyAutomatic(ItemStack gun, LivingEntity entity) {
        return false;
    }

    default boolean canADS(ItemStack gun, LivingEntity entity) {
        return false;
    }

    default boolean canUseAndShoot(ItemStack gun, LivingEntity entity) {
        return entity.getActiveItem().equals(gun);
    }
}