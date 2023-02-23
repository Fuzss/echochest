package fuzs.echochest.handler;

import fuzs.echochest.init.ModRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

public class ItemSpawnHandler {

    public static void onEntityJoinServerLevel(Entity entity, ServerLevel level) {
        if (entity instanceof ItemEntity) entity.gameEvent(ModRegistry.ITEM_SPAWN_GAME_EVENT.get());
    }
}
