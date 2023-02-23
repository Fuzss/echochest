package fuzs.echochest;

import fuzs.puzzleslib.core.CommonFactories;
import net.fabricmc.api.ModInitializer;

public class EchoChestFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonFactories.INSTANCE.modConstructor(EchoChest.MOD_ID).accept(new EchoChest());
    }
}
