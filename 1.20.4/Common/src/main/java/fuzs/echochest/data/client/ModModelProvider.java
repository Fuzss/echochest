package fuzs.echochest.data.client;

import fuzs.echochest.EchoChest;
import fuzs.echochest.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;

public class ModModelProvider extends AbstractModelProvider {
    public static final ModelTemplate CHEST_TEMPLATE = new ModelTemplate(
            Optional.of(decorateItemModelLocation(EchoChest.id("template_chest"))), Optional.empty(),
            TextureSlot.PARTICLE
    );

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBlockModels(BlockModelGenerators builder) {
        builder.blockEntityModels(ModelLocationUtils.getModelLocation(ModRegistry.ECHO_CHEST_BLOCK.value()),
                Blocks.SCULK
        ).createWithoutBlockItem(ModRegistry.ECHO_CHEST_BLOCK.value());
    }

    @Override
    public void addItemModels(ItemModelGenerators builder) {
        CHEST_TEMPLATE.create(ModelLocationUtils.getModelLocation(ModRegistry.ECHO_CHEST_ITEM.value()),
                TextureMapping.particle(Blocks.NETHER_BRICKS), builder.output
        );
    }
}
