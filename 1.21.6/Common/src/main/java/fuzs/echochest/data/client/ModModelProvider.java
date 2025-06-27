package fuzs.echochest.data.client;

import fuzs.echochest.client.renderer.blockentity.EchoChestRenderer;
import fuzs.echochest.client.renderer.special.UnbakedEchoChestSpecialRenderer;
import fuzs.echochest.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Function;

public class ModModelProvider extends AbstractModelProvider {

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBlockModels(BlockModelGenerators blockModelGenerators) {
        this.createChest(ModRegistry.ECHO_CHEST_BLOCK.value(),
                Blocks.SCULK,
                EchoChestRenderer.ECHO_CHEST_TEXTURE,
                true,
                UnbakedEchoChestSpecialRenderer::new,
                blockModelGenerators);
    }

    public final void createChest(Block chestBlock, Block particleBlock, ResourceLocation texture, boolean useGiftTexture, Function<ResourceLocation, SpecialModelRenderer.Unbaked> unbakedRendererFactory, BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.createParticleOnlyBlock(chestBlock, particleBlock);
        Item item = chestBlock.asItem();
        ResourceLocation resourceLocation = ModelTemplates.CHEST_INVENTORY.create(item,
                TextureMapping.particle(particleBlock),
                blockModelGenerators.modelOutput);
        ItemModel.Unbaked unbaked = ItemModelUtils.specialModel(resourceLocation,
                unbakedRendererFactory.apply(texture));
        if (useGiftTexture) {
            ItemModel.Unbaked unbaked2 = ItemModelUtils.specialModel(resourceLocation,
                    unbakedRendererFactory.apply(ChestSpecialRenderer.GIFT_CHEST_TEXTURE));
            blockModelGenerators.itemModelOutput.accept(item, ItemModelUtils.isXmas(unbaked2, unbaked));
        } else {
            blockModelGenerators.itemModelOutput.accept(item, unbaked);
        }
    }
}
