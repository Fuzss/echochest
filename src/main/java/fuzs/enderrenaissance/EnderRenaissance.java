package fuzs.enderrenaissance;

import fuzs.enderrenaissance.data.ModLanguageProvider;
import fuzs.enderrenaissance.data.ModLootTableProvider;
import fuzs.enderrenaissance.handler.ItemEntityJoinHandler;
import fuzs.enderrenaissance.registry.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(EnderRenaissance.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnderRenaissance {
    public static final String MOD_ID = "enderrenaissance";
    public static final String MOD_NAME = "Ender Renaissance";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModRegistry.touch();
        registerHandlers();
    }

    public static void registerHandlers() {
        ItemEntityJoinHandler itemEntityJoinHandler = new ItemEntityJoinHandler();
        MinecraftForge.EVENT_BUS.addListener(itemEntityJoinHandler::onEntityJoinWorld);
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        final ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
        generator.addProvider(new ModLootTableProvider(generator, MOD_ID));
        generator.addProvider(new ModLanguageProvider(generator, MOD_ID));
    }
}
