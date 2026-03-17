package net.armaments.item.component;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.math.Fraction;

public record AmmoPouchTooltipComponent(AmmoPouchComponent component) implements TooltipComponent {
    public static final Identifier BACKGROUND_TEXTURE = Identifier.ofVanilla("container/bundle/background");
    public static final Identifier BLOCKED_SLOT_TEXTURE = Identifier.ofVanilla("container/bundle/blocked_slot");
    public static final Identifier SLOT_TEXTURE = Identifier.ofVanilla("container/bundle/slot");

    @Override
    public int getHeight() {
        return this.getRowsHeight() + 4;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.getColumnsWidth();
    }

    private int getColumnsWidth() {
        return this.getColumns() * 18 + 2;
    }

    private int getRowsHeight() {
        return this.getRows() * 20 + 2;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        int i = this.getColumns();
        int j = this.getRows();
        context.drawGuiTexture(BACKGROUND_TEXTURE, x, y, this.getColumnsWidth(), this.getRowsHeight());
        boolean bl = this.component.occupancy().compareTo(Fraction.ONE) >= 0;
        int k = 0;

        for (int l = 0; l < j; l++) {
            for (int m = 0; m < i; m++) {
                int n = x + m * 18 + 1;
                int o = y + l * 20 + 1;
                this.drawSlot(n, o, k++, bl, context, textRenderer);
            }
        }
    }

    private void drawSlot(int x, int y, int index, boolean shouldBlock, DrawContext context, TextRenderer textRenderer) {
        if (index >= this.component.size()) {
            this.draw(context, x, y, shouldBlock ? BLOCKED_SLOT_TEXTURE : SLOT_TEXTURE);
        } else {
            ItemStack itemStack = this.component.get(index);
            this.draw(context, x, y, SLOT_TEXTURE);
            context.drawItem(itemStack, x + 1, y + 1, index);
            context.drawItemInSlot(textRenderer, itemStack, x + 1, y + 1);
            if (index == 0) {
                HandledScreen.drawSlotHighlight(context, x + 1, y + 1, 0);
            }
        }
    }

    private void draw(DrawContext context, int x, int y, Identifier texture) {
        context.drawGuiTexture(texture, x, y, 0, 18, 20);
    }

    private int getColumns() {
        return Math.max(2, (int)Math.ceil(Math.sqrt(this.component.size() + 1.0)));
    }

    private int getRows() {
        return (int)Math.ceil((this.component.size() + 1.0) / this.getColumns());
    }
}
