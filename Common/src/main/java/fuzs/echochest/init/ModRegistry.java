package fuzs.echochest.init;

import fuzs.echochest.EchoChest;
import fuzs.echochest.world.inventory.EchoChestMenu;
import fuzs.echochest.world.level.block.EchoChestBlock;
import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import fuzs.puzzleslib.core.CommonFactories;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import fuzs.puzzleslib.init.builder.ModBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.gameevent.GameEvent;

public class ModRegistry {
    private static final RegistryManager REGISTRY = CommonFactories.INSTANCE.registration(EchoChest.MOD_ID);
    public static final RegistryReference<Block> ECHO_CHEST_BLOCK = REGISTRY.registerBlockWithItem("echo_chest", () -> new EchoChestBlock(BlockBehaviour.Properties.copy(Blocks.ENDER_CHEST).lightLevel(state -> 1)), CreativeModeTab.TAB_DECORATIONS);
    public static final RegistryReference<BlockEntityType<EchoChestBlockEntity>> ECHO_CHEST_BLOCK_ENTITY_TYPE = REGISTRY.registerBlockEntityTypeBuilder("echo_chest", () -> ModBlockEntityTypeBuilder.of(EchoChestBlockEntity::new, ECHO_CHEST_BLOCK.get()));
    public static final RegistryReference<MenuType<EchoChestMenu>> ECHO_CHEST_MENU_TYPE = REGISTRY.registerMenuTypeSupplier("echo_chest", () -> EchoChestMenu::new);
    public static final RegistryReference<GameEvent> ITEM_TICK_GAME_EVENT = REGISTRY.register(Registry.GAME_EVENT_REGISTRY, "item_tick", () -> new GameEvent("item_tick", 8));

    public static final TagKey<GameEvent> ECHO_CHEST_CAN_LISTEN = TagKey.create(Registry.GAME_EVENT_REGISTRY, EchoChest.id("echo_chest_can_listen"));

    public static void touch() {

    }
}
