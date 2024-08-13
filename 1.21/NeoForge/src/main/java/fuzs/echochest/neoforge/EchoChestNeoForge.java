package fuzs.echochest.neoforge;

import fuzs.echochest.EchoChest;
import fuzs.echochest.data.ModBlockLootProvider;
import fuzs.echochest.data.ModBlockTagsProvider;
import fuzs.echochest.data.ModGameEventTagsProvider;
import fuzs.echochest.data.ModRecipeProvider;
import fuzs.echochest.data.client.ModLanguageProvider;
import fuzs.echochest.data.client.ModModelProvider;
import fuzs.echochest.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.puzzleslib.neoforge.api.init.v3.capability.NeoForgeCapabilityHelperV2;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EchoChest.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EchoChestNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(EchoChest.MOD_ID, EchoChest::new);
        NeoForgeCapabilityHelperV2.registerWorldlyBlockEntityContainer(ModRegistry.ECHO_CHEST_BLOCK_ENTITY_TYPE);
        DataProviderHelper.registerDataProviders(EchoChest.MOD_ID, ModBlockLootProvider::new, ModBlockTagsProvider::new,
                ModGameEventTagsProvider::new, ModModelProvider::new, ModLanguageProvider::new, ModRecipeProvider::new
        );
    }
}
