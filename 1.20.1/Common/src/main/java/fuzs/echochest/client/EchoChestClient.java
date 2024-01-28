package fuzs.echochest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.echochest.client.gui.screens.inventory.EchoChestScreen;
import fuzs.echochest.init.ModRegistry;
import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.BlockEntityRenderersContext;
import fuzs.puzzleslib.api.client.core.v1.context.BuiltinModelItemRendererContext;
import fuzs.puzzleslib.api.core.v1.context.ModLifecycleContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class EchoChestClient implements ClientModConstructor {

    @Override
    public void onClientSetup(ModLifecycleContext context) {
        MenuScreens.register(ModRegistry.ECHO_CHEST_MENU_TYPE.get(), EchoChestScreen::new);
    }

    @Override
    public void onRegisterBlockEntityRenderers(BlockEntityRenderersContext context) {
        context.registerBlockEntityRenderer(ModRegistry.ECHO_CHEST_BLOCK_ENTITY_TYPE.get(), ChestRenderer::new);
    }

    @Override
    public void onRegisterBuiltinModelItemRenderers(BuiltinModelItemRendererContext context) {
        final EchoChestBlockEntity echoChest = new EchoChestBlockEntity(BlockPos.ZERO, ModRegistry.ECHO_CHEST_BLOCK.get().defaultBlockState());
        context.registerItemRenderer((ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) -> {
            Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(echoChest, matrices, vertexConsumers, light, overlay);
        }, ModRegistry.ECHO_CHEST_BLOCK.get());
    }
}
