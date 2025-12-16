package net.armaments.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Shadow @Final private List<DefaultedList<ItemStack>> combinedInventory;

    @Shadow @Final public PlayerEntity player;

    @Shadow public int selectedSlot;

    /**
     * @author ntrdeal
     * @reason just take a look at the vanilla code
     */
    @Overwrite
    public void updateItems() {
        int realSlot = 0;
        for (DefaultedList<ItemStack> defaultedList : this.combinedInventory) {
            for (ItemStack stack : defaultedList) {
                if (!stack.isEmpty()) {
                    stack.inventoryTick(this.player.getWorld(), this.player, realSlot, this.selectedSlot == realSlot);
                }
                realSlot++;
            }
        }
    }
}
