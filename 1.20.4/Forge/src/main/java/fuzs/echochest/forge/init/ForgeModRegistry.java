package fuzs.echochest.forge.init;

import fuzs.echochest.EchoChest;
import fuzs.echochest.init.ModRegistry;
import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import fuzs.echochest.forge.world.level.block.entity.EchoChestForgeBlockEntity;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ForgeModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.from(EchoChest.MOD_ID);
    public static final Holder.Reference<BlockEntityType<EchoChestBlockEntity>> ECHO_CHEST_BLOCK_ENTITY_TYPE = REGISTRY.registerBlockEntityType("echo_chest", () -> BlockEntityType.Builder.of(EchoChestForgeBlockEntity::new, ModRegistry.ECHO_CHEST_BLOCK.get()));

    public static void touch() {

    }
}
