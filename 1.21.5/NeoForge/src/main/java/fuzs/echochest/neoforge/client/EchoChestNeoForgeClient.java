package fuzs.echochest.neoforge.client;

import fuzs.echochest.EchoChest;
import fuzs.echochest.client.EchoChestClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = EchoChest.MOD_ID, dist = Dist.CLIENT)
public class EchoChestNeoForgeClient {

    public EchoChestNeoForgeClient() {
        ClientModConstructor.construct(EchoChest.MOD_ID, EchoChestClient::new);
    }
}
