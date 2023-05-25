package fuzs.echochest.init;

import fuzs.echochest.EchoChest;
import fuzs.echochest.world.inventory.EchoChestMenu;
import fuzs.echochest.world.level.block.EchoChestBlock;
import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import fuzs.puzzleslib.api.core.v1.ModLoader;
import fuzs.puzzleslib.api.init.v2.RegistryManager;
import fuzs.puzzleslib.api.init.v2.RegistryReference;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.gameevent.GameEvent;

public class ModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.instant(EchoChest.MOD_ID);
    public static final RegistryReference<Block> ECHO_CHEST_BLOCK = REGISTRY.registerBlock("echo_chest", () -> new EchoChestBlock(BlockBehaviour.Properties.copy(Blocks.ENDER_CHEST).lightLevel(state -> 1)));
    public static final RegistryReference<Item> ECHO_CHEST_ITEM = REGISTRY.registerBlockItem(ECHO_CHEST_BLOCK);
    public static final RegistryReference<BlockEntityType<EchoChestBlockEntity>> ECHO_CHEST_BLOCK_ENTITY_TYPE = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlockEntityType("echo_chest", () -> BlockEntityType.Builder.of(EchoChestBlockEntity::new, ECHO_CHEST_BLOCK.get()));
    public static final RegistryReference<MenuType<EchoChestMenu>> ECHO_CHEST_MENU_TYPE = REGISTRY.registerMenuType("echo_chest", () -> EchoChestMenu::new);
    public static final RegistryReference<GameEvent> ITEM_TICK_GAME_EVENT = REGISTRY.register(Registries.GAME_EVENT, "item_tick", () -> new GameEvent("item_tick", 8));

    public static final TagKey<GameEvent> ECHO_CHEST_CAN_LISTEN = REGISTRY.registerGameEventTag("echo_chest_can_listen");

    public static void touch() {

    }
}
