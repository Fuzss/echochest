package fuzs.enderrenaissance.handler;

import com.google.common.base.Predicates;
import fuzs.enderrenaissance.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class ItemEntityJoinHandler {

    @SubscribeEvent
    public void onEntityJoinWorld(final EntityJoinWorldEvent evt) {
        if (!evt.getWorld().isClientSide && evt.getEntity() instanceof ItemEntity) {
            Stream<BlockPos> pois = ((ServerLevel) evt.getWorld()).getPoiManager().findAll(poiType -> poiType == ModRegistry.ENDER_COLLECTOR_POI_TYPE.get(), pos -> true, evt.getEntity().blockPosition(), 64, PoiManager.Occupancy.ANY);

        }
    }
}
