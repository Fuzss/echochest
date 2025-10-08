package fuzs.echochest.data;

import fuzs.echochest.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.AbstractLootProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModBlockLootProvider extends AbstractLootProvider.Blocks {

    public ModBlockLootProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addLootTables() {
        this.add(ModRegistry.ECHO_CHEST_BLOCK.value(), this::createNameableBlockEntityTable);
    }
}
