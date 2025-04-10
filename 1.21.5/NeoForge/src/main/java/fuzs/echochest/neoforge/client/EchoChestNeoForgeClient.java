package fuzs.echochest.neoforge.client;

import fuzs.echochest.EchoChest;
import fuzs.echochest.client.EchoChestClient;
import fuzs.echochest.data.client.ModLanguageProvider;
import fuzs.echochest.data.client.ModModelProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = EchoChest.MOD_ID, dist = Dist.CLIENT)
public class EchoChestNeoForgeClient {

    public EchoChestNeoForgeClient() {
        ClientModConstructor.construct(EchoChest.MOD_ID, EchoChestClient::new);
        DataProviderHelper.registerDataProviders(EchoChest.MOD_ID, ModModelProvider::new, ModLanguageProvider::new);
    }
}
