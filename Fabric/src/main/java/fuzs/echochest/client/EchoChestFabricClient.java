package fuzs.echochest.client;

import fuzs.echochest.EchoChest;
import fuzs.puzzleslib.client.core.ClientFactories;
import fuzs.puzzleslib.core.ContentRegistrationFlags;
import net.fabricmc.api.ClientModInitializer;

public class EchoChestFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientFactories.INSTANCE.clientModConstructor(EchoChest.MOD_ID, ContentRegistrationFlags.BUILT_IN_ITEM_MODEL_RENDERERS).accept(new EchoChestClient());
    }
}
