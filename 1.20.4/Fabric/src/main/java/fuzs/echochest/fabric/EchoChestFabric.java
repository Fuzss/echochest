package fuzs.echochest.fabric;

import fuzs.echochest.EchoChest;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class EchoChestFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(EchoChest.MOD_ID, EchoChest::new);
    }
}
