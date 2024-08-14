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
import fuzs.puzzleslib.neoforge.api.init.v3.capability.NeoForgeCapabilityHelper;
import net.neoforged.fml.common.Mod;

@Mod(EchoChest.MOD_ID)
public class EchoChestNeoForge {

    public EchoChestNeoForge() {
        ModConstructor.construct(EchoChest.MOD_ID, EchoChest::new);
        NeoForgeCapabilityHelper.registerWorldlyBlockEntityContainer(ModRegistry.ECHO_CHEST_BLOCK_ENTITY_TYPE);
        DataProviderHelper.registerDataProviders(EchoChest.MOD_ID, ModBlockLootProvider::new, ModBlockTagsProvider::new,
                ModGameEventTagsProvider::new, ModModelProvider::new, ModLanguageProvider::new, ModRecipeProvider::new
        );
    }
}
