package fuzs.echochest.client.gui.screens.inventory;

import fuzs.echochest.EchoChest;
import fuzs.echochest.world.inventory.EchoChestMenu;
import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;

public class EchoChestScreen extends AbstractContainerScreen<EchoChestMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = EchoChest.id("textures/gui/container/echo_chest.png");

    public EchoChestScreen(EchoChestMenu chestMenu, Inventory inventory, Component component) {
        super(chestMenu, inventory, component);
        this.imageHeight = 200;
        this.titleLabelY = 7;
        this.inventoryLabelY = this.imageHeight - 93;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFCFCFCF, false);
        guiGraphics.drawString(this.font,
                this.playerInventoryTitle,
                this.inventoryLabelX,
                this.inventoryLabelY,
                0xFF404040,
                false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (this.isHovering(21, 29, 12, 40, mouseX, mouseY)) {
            guiGraphics.setTooltipForNextFrame(this.font,
                    Component.literal(
                                    (int) (this.menu.getExperience() / EchoChestBlockEntity.EXPERIENCE_PER_BOTTLE) + "x ")
                            .append(Items.EXPERIENCE_BOTTLE.getName())
                            .withStyle(ChatFormatting.YELLOW),
                    mouseX,
                    mouseY);
        } else {
            this.renderTooltip(guiGraphics, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                CONTAINER_BACKGROUND,
                this.leftPos,
                this.topPos,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                256,
                256);
        int height = (int) (this.menu.getExperience() / (float) EchoChestBlockEntity.MAX_EXPERIENCE * 40.0F);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                CONTAINER_BACKGROUND,
                this.leftPos + 21,
                this.topPos + 29 + 40 - height,
                177,
                1 + 40 - height,
                12,
                height,
                256,
                256);
    }
}
