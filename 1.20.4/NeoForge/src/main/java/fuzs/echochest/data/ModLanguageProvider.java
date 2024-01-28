package fuzs.echochest.data;

import fuzs.echochest.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractLanguageProvider;
import net.minecraft.data.PackOutput;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(PackOutput packOutput, String modId) {
        super(packOutput, modId);
    }

    @Override
    protected void addTranslations() {
        this.add(ModRegistry.ECHO_CHEST_BLOCK.get(), "Echo Chest");
        this.add("container.echo_chest", "Echo Chest");
        this.add("block.echochest.echo_chest.description", "Collects items and experience from mobs dropped nearby in a radius of 8 blocks.");
    }
}
