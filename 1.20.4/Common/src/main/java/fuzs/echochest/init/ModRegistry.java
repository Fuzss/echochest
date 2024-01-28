package fuzs.echochest.init;

import fuzs.echochest.EchoChest;
import fuzs.echochest.world.inventory.EchoChestMenu;
import fuzs.echochest.world.level.block.EchoChestBlock;
import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import fuzs.puzzleslib.api.core.v1.ModLoader;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.BoundTagFactory;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.gameevent.GameEvent;

public class ModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.from(EchoChest.MOD_ID);
    public static final Holder.Reference<Block> ECHO_CHEST_BLOCK = REGISTRY.registerBlock("echo_chest", () -> new EchoChestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ENDER_CHEST).lightLevel(state -> 1)));
    public static final Holder.Reference<Item> ECHO_CHEST_ITEM = REGISTRY.registerBlockItem(ECHO_CHEST_BLOCK);
    public static final Holder.Reference<BlockEntityType<EchoChestBlockEntity>> ECHO_CHEST_BLOCK_ENTITY_TYPE = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlockEntityType("echo_chest", () -> BlockEntityType.Builder.of(EchoChestBlockEntity::new, ECHO_CHEST_BLOCK.value()));
    public static final Holder.Reference<MenuType<EchoChestMenu>> ECHO_CHEST_MENU_TYPE = REGISTRY.registerMenuType("echo_chest", () -> EchoChestMenu::new);
    public static final Holder.Reference<GameEvent> ITEM_TICK_GAME_EVENT = REGISTRY.registerGameEvent("item_tick", 8);

    static final BoundTagFactory TAGS = BoundTagFactory.make(EchoChest.MOD_ID);
    public static final TagKey<GameEvent> ECHO_CHEST_CAN_LISTEN = TAGS.registerGameEventTag("echo_chest_can_listen");

    public static void touch() {

    }
}
