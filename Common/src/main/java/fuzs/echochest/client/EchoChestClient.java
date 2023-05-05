package fuzs.echochest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.echochest.EchoChest;
import fuzs.echochest.client.gui.screens.inventory.EchoChestScreen;
import fuzs.echochest.init.ModRegistry;
import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import fuzs.puzzleslib.client.renderer.DynamicBuiltinModelItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class EchoChestClient implements ClientModConstructor {
    public static final Material ECHO_CHEST_LOCATION = new Material(Sheets.CHEST_SHEET, EchoChest.id("entity/chest/echo"));

    @Override
    public void onRegisterBlockEntityRenderers(BlockEntityRenderersContext context) {
        context.registerBlockEntityRenderer(ModRegistry.ECHO_CHEST_BLOCK_ENTITY_TYPE.get(), ChestRenderer::new);
    }

    @Override
    public void onRegisterMenuScreens(MenuScreensContext context) {
        context.registerMenuScreen(ModRegistry.ECHO_CHEST_MENU_TYPE.get(), EchoChestScreen::new);
    }

    @Override
    public void onRegisterBuiltinModelItemRenderers(BuiltinModelItemRendererContext context) {
        context.registerItemRenderer(ModRegistry.ECHO_CHEST_BLOCK.get(), new DynamicBuiltinModelItemRenderer() {
            private EchoChestBlockEntity echoChest;

            @Override
            public void renderByItem(ItemStack stack, ItemTransforms.TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
                Objects.requireNonNull(this.echoChest, "echo chest is null");
                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(this.echoChest, matrices, vertexConsumers, light, overlay);
            }

            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                this.echoChest = new EchoChestBlockEntity(BlockPos.ZERO, ModRegistry.ECHO_CHEST_BLOCK.get().defaultBlockState());
            }
        });
    }
}
