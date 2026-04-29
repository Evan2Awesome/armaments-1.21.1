package net.armaments.client;

import net.armaments.Armaments;
import net.armaments.api.client.event.HudRenderEvents;
import net.armaments.item.custom.GunItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class ModGui {
    public static final Identifier GUN_CROSSHAIR_TEXTURE = Armaments.id("hud/gun_crosshair");

    public static final HudRenderEvents.AttackIndicatorPack GUN_PACK = new HudRenderEvents.AttackIndicatorPack(
            Armaments.id("hud/bullet_attack_indicator_full"),
            Armaments.id("hud/bullet_attack_indicator_background"),
            Armaments.id("hud/bullet_attack_indicator_progress")
    );

    public static void register() {
        HudRenderEvents.MODIFY_CROSSHAIR.register((client, texture) -> {
            if (client.player != null && client.player.getMainHandStack().getItem() instanceof GunItem) return GUN_CROSSHAIR_TEXTURE;
            else return null;
        });

        HudRenderEvents.RENDER_CROSSHAIR_ADDITION.register((client, context, texture, x, y, width, height) -> {
            if (!texture.equals(GUN_CROSSHAIR_TEXTURE) || client.player == null) return;
            ItemStack stack = client.player.getMainHandStack();
            GunItem gunItem = ((GunItem)stack.getItem());
            Text text = Text.literal(gunItem.getAmmo(stack) + "/" + gunItem.getMaxAmmo(stack));
            context.drawCenteredTextWithShadow(client.textRenderer, text, x + (int) (width * 0.5f), y + 26, 0xFFFFFF);
        });

        HudRenderEvents.MODIFY_ATTACK_INDICATOR.register((client, defaultPack, crosshairTexture) -> {
            if (!crosshairTexture.equals(GUN_CROSSHAIR_TEXTURE) || client.player == null) return null;
            if (!(client.player.getMainHandStack().getItem() instanceof GunItem gunItem)) return null;
            return gunItem.getAmmo(client.player.getMainHandStack()) > 0 ? GUN_PACK : null;
        });
    }
}
