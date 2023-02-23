package fuzs.echochest;

import fuzs.echochest.init.ModRegistry;
import fuzs.puzzleslib.core.ModConstructor;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoChest implements ModConstructor {
    public static final String MOD_ID = "echochest";
    public static final String MOD_NAME = "Echo Chest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
