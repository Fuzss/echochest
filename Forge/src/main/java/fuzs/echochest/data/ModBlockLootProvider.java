package fuzs.echochest.data;

import fuzs.echochest.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractLootProvider;
import net.minecraft.data.PackOutput;

public class ModBlockLootProvider extends AbstractLootProvider.Blocks {

    public ModBlockLootProvider(PackOutput packOutput, String modId) {
        super(packOutput, modId);
    }

    @Override
    public void generate() {
        this.add(ModRegistry.ECHO_CHEST_BLOCK.get(), this::createNameableBlockEntityTable);
    }
}
