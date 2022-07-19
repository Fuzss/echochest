package fuzs.enderrenaissance.registry;

import com.google.common.collect.ImmutableSet;
import fuzs.enderrenaissance.EnderRenaissance;
import fuzs.enderrenaissance.world.level.block.CollectorBlock;
import fuzs.enderrenaissance.world.level.block.entity.CollectorBlockEntity;
import fuzs.puzzleslib.registry.RegistryManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {
    private static final RegistryManager REGISTRY = RegistryManager.of(EnderRenaissance.MOD_ID);
    public static final RegistryObject<Block> ENDER_COLLECTOR_BLOCK = REGISTRY.registerBlock("ender_collector", () -> new CollectorBlock(BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F)));
    public static final RegistryObject<Item> ENDER_COLLECTOR_ITEM = REGISTRY.registerBlockItem("ender_collector", CreativeModeTab.TAB_REDSTONE);
    public static final RegistryObject<BlockEntityType<CollectorBlockEntity>> ENDER_COLLECTOR_BLOCK_ENTITY_TYPE = REGISTRY.registerRawBlockEntityType("ender_collector", () -> BlockEntityType.Builder.of(CollectorBlockEntity::new, ENDER_COLLECTOR_BLOCK.get()));
    public static final RegistryObject<PoiType> ENDER_COLLECTOR_POI_TYPE = REGISTRY.register(PoiType.class, "ender_collector", () -> new PoiType("ender_collector", ImmutableSet.copyOf(ENDER_COLLECTOR_BLOCK.get().getStateDefinition().getPossibleStates()), 0, 1));

    public static void touch() {

    }
}
