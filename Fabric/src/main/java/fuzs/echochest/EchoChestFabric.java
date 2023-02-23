package fuzs.echochest;

import fuzs.echochest.handler.ItemSpawnHandler;
import fuzs.echochest.init.ModRegistry;
import fuzs.puzzleslib.core.CommonFactories;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.entity.EntityInLevelCallback;

public class EchoChestFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonFactories.INSTANCE.modConstructor(EchoChest.MOD_ID).accept(new EchoChest());
        registerHandlers();
    }

    private static void registerHandlers() {
        ServerEntityEvents.ENTITY_LOAD.register(ItemSpawnHandler::onEntityJoinServerLevel);
    }
}
