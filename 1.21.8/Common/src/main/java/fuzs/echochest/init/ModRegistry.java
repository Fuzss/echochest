package fuzs.echochest.init;

import fuzs.echochest.EchoChest;
import fuzs.echochest.world.inventory.EchoChestMenu;
import fuzs.echochest.world.level.block.EchoChestBlock;
import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import fuzs.puzzleslib.api.init.v3.tags.TagFactory;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Collections;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(EchoChest.MOD_ID);
    public static final Holder.Reference<Block> ECHO_CHEST_BLOCK = REGISTRIES.registerBlock("echo_chest",
            EchoChestBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.ENDER_CHEST).lightLevel((BlockState blockState) -> 1));
    public static final Holder.Reference<Item> ECHO_CHEST_ITEM = REGISTRIES.registerBlockItem(ECHO_CHEST_BLOCK);
    public static final Holder.Reference<BlockEntityType<EchoChestBlockEntity>> ECHO_CHEST_BLOCK_ENTITY_TYPE = REGISTRIES.registerBlockEntityType(
            "echo_chest",
            EchoChestBlockEntity::new,
            () -> Collections.singleton(ECHO_CHEST_BLOCK.value()));
    public static final Holder.Reference<MenuType<EchoChestMenu>> ECHO_CHEST_MENU_TYPE = REGISTRIES.registerMenuType(
            "echo_chest",
            EchoChestMenu::new);
    public static final Holder.Reference<GameEvent> ITEM_TICK_GAME_EVENT = REGISTRIES.registerGameEvent("item_tick", 8);

    static final TagFactory TAGS = TagFactory.make(EchoChest.MOD_ID);
    public static final TagKey<GameEvent> ECHO_CHEST_CAN_LISTEN_GAME_EVENT_TAG = TAGS.registerGameEventTag(
            "echo_chest_can_listen");

    public static void bootstrap() {
        // NO-OP
    }
}
