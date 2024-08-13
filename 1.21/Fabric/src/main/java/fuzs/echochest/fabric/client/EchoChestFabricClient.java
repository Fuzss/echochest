package fuzs.echochest.fabric.client;

import fuzs.echochest.EchoChest;
import fuzs.echochest.client.EchoChestClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class EchoChestFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(EchoChest.MOD_ID, EchoChestClient::new);
    }
}
