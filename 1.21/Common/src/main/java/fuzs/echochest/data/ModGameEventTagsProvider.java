package fuzs.echochest.data;

import fuzs.echochest.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractTagProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.gameevent.GameEvent;

public class ModGameEventTagsProvider extends AbstractTagProvider.GameEvents {

    public ModGameEventTagsProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ModRegistry.ECHO_CHEST_CAN_LISTEN).add(GameEvent.ENTITY_DIE, GameEvent.HIT_GROUND, ModRegistry.ITEM_TICK_GAME_EVENT.value());
    }
}
