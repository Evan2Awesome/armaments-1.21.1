package net.armaments.item.custom;

import net.armaments.item.component.AmmoPouchComponent;
import net.armaments.item.component.AmmoPouchTooltipComponent;
import net.armaments.item.component.ModDataComponents;
import net.armaments.item.component.StackHolder;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.Fraction;

import java.util.List;
import java.util.Optional;

public class AmmoPouchItem extends BundleItem {
    public static final int ITEM_BAR_COLOR = MathHelper.packRgb(0.4F, 0.4F, 1.0F);

    public AmmoPouchItem(Settings settings) {
        super(settings);
    }

    public static float getAmountFilled(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.AMMO_POUCH, AmmoPouchComponent.DEFAULT).occupancy().floatValue();
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        } else {
            if (stack.get(ModDataComponents.AMMO_POUCH) instanceof AmmoPouchComponent component) {
                ItemStack clickedStack = slot.getStack();
                StackHolder.Builder<AmmoPouchComponent> builder = StackHolder.builder(component);
                if (clickedStack.isEmpty()) {
                    this.playRemoveOneSound(player);
                    ItemStack retrievedStack = builder.removeFirst();
                    if (!retrievedStack.isEmpty()) {
                        ItemStack insertedStack = slot.insertStack(retrievedStack);
                        builder.add(insertedStack);
                    }
                } else if (component.canInsert(clickedStack)) {
                    if (builder.add(slot, player) > 0) this.playInsertSound(player);
                }
                stack.set(ModDataComponents.AMMO_POUCH, builder.build());
                return true;
            } else return false;
        }
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            if (stack.get(ModDataComponents.AMMO_POUCH) instanceof AmmoPouchComponent component) {
                StackHolder.Builder<AmmoPouchComponent> builder = StackHolder.builder(component);
                if (otherStack.isEmpty()) {
                    ItemStack retrievedStack = builder.removeFirst();
                    if (!retrievedStack.isEmpty()) {
                        this.playRemoveOneSound(player);
                        cursorStackReference.set(retrievedStack);
                    }
                } else if (component.canInsert(otherStack)) {
                    if (builder.add(otherStack) > 0) this.playInsertSound(player);
                }
                stack.set(ModDataComponents.AMMO_POUCH, builder.build());
                return true;
            } else return false;
        } else {
            return false;
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (stack.get(ModDataComponents.AMMO_POUCH) instanceof AmmoPouchComponent component && component.dropAllItems(user)) {
            stack.set(ModDataComponents.AMMO_POUCH, AmmoPouchComponent.DEFAULT);
            this.playDropContentsSound(user);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.success(stack, world.isClient());
        } else {
            return TypedActionResult.fail(stack);
        }
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        AmmoPouchComponent component = stack.getOrDefault(ModDataComponents.AMMO_POUCH, AmmoPouchComponent.DEFAULT);
        return component.occupancy().compareTo(Fraction.ZERO) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        AmmoPouchComponent component = stack.getOrDefault(ModDataComponents.AMMO_POUCH, AmmoPouchComponent.DEFAULT);
        return Math.min(1 + MathHelper.multiplyFraction(component.occupancy(), 12), 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return ITEM_BAR_COLOR;
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return !stack.contains(DataComponentTypes.HIDE_TOOLTIP) && !stack.contains(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)
                ? Optional.ofNullable(stack.get(ModDataComponents.AMMO_POUCH)).map(AmmoPouchTooltipData::new)
                : Optional.empty();
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (stack.get(ModDataComponents.AMMO_POUCH) instanceof AmmoPouchComponent component) {
            tooltip.add(Text.translatable("item.minecraft.bundle.fullness",
                    MathHelper.multiplyFraction(component.occupancy(), 256),
                    256).formatted(Formatting.GRAY));
        }
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        if (entity.getStack().get(ModDataComponents.AMMO_POUCH) instanceof AmmoPouchComponent component) {
            entity.getStack().set(ModDataComponents.AMMO_POUCH, AmmoPouchComponent.DEFAULT);
            ItemUsage.spawnItemContents(entity, component.iterateCopy());
        }
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    public static void registerTooltip() {
        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof AmmoPouchTooltipData(AmmoPouchComponent contents)) return new AmmoPouchTooltipComponent(contents);
            else return null;
        });
    }

    public record AmmoPouchTooltipData(AmmoPouchComponent contents) implements TooltipData {}
}
