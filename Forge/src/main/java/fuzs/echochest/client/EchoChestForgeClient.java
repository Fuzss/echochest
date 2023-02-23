package fuzs.echochest.client;

import fuzs.echochest.EchoChest;
import fuzs.puzzleslib.client.core.ClientFactories;
import fuzs.puzzleslib.core.ContentRegistrationFlags;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = EchoChest.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EchoChestForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientFactories.INSTANCE.clientModConstructor(EchoChest.MOD_ID, ContentRegistrationFlags.BUILT_IN_ITEM_MODEL_RENDERERS).accept(new EchoChestClient());
    }
}
