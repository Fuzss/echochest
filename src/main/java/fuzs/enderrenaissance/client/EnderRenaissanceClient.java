package fuzs.enderrenaissance.client;

import fuzs.enderrenaissance.EnderRenaissance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = EnderRenaissance.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EnderRenaissanceClient {

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent evt) {

    }
}
