package fuzs.echochest.data;

import fuzs.echochest.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.GameEventTagsProvider;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModGameEventTagsProvider extends GameEventTagsProvider {

    public ModGameEventTagsProvider(DataGenerator dataGenerator, String modId, @Nullable ExistingFileHelper fileHelper) {
        super(dataGenerator, modId, fileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ModRegistry.ECHO_CHEST_CAN_LISTEN).add(GameEvent.ENTITY_DIE, GameEvent.HIT_GROUND, ModRegistry.ITEM_TICK_GAME_EVENT.get());
    }
}
