package fuzs.echochest.init;

import fuzs.echochest.EchoChest;
import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import fuzs.echochest.world.level.block.entity.EchoChestForgeBlockEntity;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import fuzs.puzzleslib.init.builder.ModBlockEntityTypeBuilder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ForgeModRegistry {
    private static final RegistryManager REGISTRY = CommonFactories.INSTANCE.registration(EchoChest.MOD_ID);
    public static final RegistryReference<BlockEntityType<EchoChestBlockEntity>> ECHO_CHEST_BLOCK_ENTITY_TYPE = REGISTRY.registerBlockEntityTypeBuilder("echo_chest", () -> ModBlockEntityTypeBuilder.of(EchoChestForgeBlockEntity::new, ModRegistry.ECHO_CHEST_BLOCK.get()));

    public static void touch() {

    }
}
