package fuzs.echochest.init;

import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import fuzs.echochest.world.level.block.entity.EchoChestForgeBlockEntity;
import fuzs.puzzleslib.api.init.v2.RegistryReference;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ForgeModRegistry {
    public static final RegistryReference<BlockEntityType<EchoChestBlockEntity>> ECHO_CHEST_BLOCK_ENTITY_TYPE = ModRegistry.REGISTRY.registerBlockEntityType("echo_chest", () -> BlockEntityType.Builder.of(EchoChestForgeBlockEntity::new, ModRegistry.ECHO_CHEST_BLOCK.get()));

    public static void touch() {

    }
}
