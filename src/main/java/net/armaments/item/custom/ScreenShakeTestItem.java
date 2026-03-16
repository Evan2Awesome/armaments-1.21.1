package net.armaments.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ScreenShakeTestItem extends Item {
    public ScreenShakeTestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        player.setCurrentHand(hand);
        return super.use(world, player, hand);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            player.setPitch(player.getPitch() - player.getRandom().nextBetweenExclusive(-2, 3));
            player.setYaw(player.getYaw() - player.getRandom().nextBetweenExclusive(-2, 3));
        }
        super.usageTick(world, user, stack, remainingUseTicks);
    }
}
