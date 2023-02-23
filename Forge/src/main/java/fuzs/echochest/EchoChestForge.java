package fuzs.echochest;

import fuzs.echochest.data.*;
import fuzs.echochest.handler.ItemSpawnHandler;
import fuzs.puzzleslib.core.CommonFactories;
import net.minecraft.data.DataGenerator;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(EchoChest.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EchoChestForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CommonFactories.INSTANCE.modConstructor(EchoChest.MOD_ID).accept(new EchoChest());
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final EntityJoinLevelEvent evt) -> {
            if (!evt.getLevel().isClientSide) ItemSpawnHandler.onEntityJoinServerLevel(evt.getEntity(), (ServerLevel) evt.getLevel());
        });
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator dataGenerator = evt.getGenerator();
        final ExistingFileHelper fileHelper = evt.getExistingFileHelper();
        dataGenerator.addProvider(true, new ModBlockTagsProvider(dataGenerator, EchoChest.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModGameEventTagsProvider(dataGenerator, EchoChest.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModLanguageProvider(dataGenerator, EchoChest.MOD_ID));
        dataGenerator.addProvider(true, new ModLootTableProvider(dataGenerator, EchoChest.MOD_ID));
        dataGenerator.addProvider(true, new ModRecipeProvider(dataGenerator));
    }
}
