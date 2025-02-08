package fuzs.echochest.data;

import fuzs.echochest.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.gameevent.GameEvent;

public class ModGameEventTagsProvider extends AbstractTagProvider<GameEvent> {

    public ModGameEventTagsProvider(DataProviderContext context) {
        super(Registries.GAME_EVENT, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(ModRegistry.ECHO_CHEST_CAN_LISTEN_GAME_EVENT_TAG)
                .add(GameEvent.ENTITY_DIE, GameEvent.HIT_GROUND, ModRegistry.ITEM_TICK_GAME_EVENT);
    }
}
