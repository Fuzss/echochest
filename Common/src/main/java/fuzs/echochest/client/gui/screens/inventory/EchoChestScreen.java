package fuzs.echochest.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.echochest.EchoChest;
import fuzs.echochest.world.inventory.EchoChestMenu;
import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;

public class EchoChestScreen extends AbstractContainerScreen<EchoChestMenu> {
    private static final ResourceLocation CONTAINER_BACKGROUND = EchoChest.id("textures/gui/container/echo_chest.png");

    public EchoChestScreen(EchoChestMenu chestMenu, Inventory inventory, Component component) {
        super(chestMenu, inventory, component);
        this.passEvents = false;
        this.imageHeight = 200;
        this.titleLabelY = 7;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 0xCFCFCF);
        this.font.draw(poseStack, this.playerInventoryTitle, (float) this.inventoryLabelX, (float) this.inventoryLabelY, 4210752);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        if (this.isHovering(21, 29, 12, 40, mouseX, mouseY)) {
            this.renderTooltip(poseStack, Component.literal((int) (this.menu.getExperience() / EchoChestBlockEntity.EXPERIENCE_PER_BOTTLE) + "x ").append(Items.EXPERIENCE_BOTTLE.getDescription()).withStyle(ChatFormatting.YELLOW), mouseX, mouseY);
        } else {
            this.renderTooltip(poseStack, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int height = (int) (this.menu.getExperience() / (float) EchoChestBlockEntity.MAX_EXPERIENCE * 40.0F);
        this.blit(poseStack, this.leftPos + 21, this.topPos + 29 + 40 - height, 177, 1 + 40 - height, 12, height);
    }
}
