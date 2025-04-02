package fuzs.echochest.data;

import fuzs.echochest.init.ModRegistry;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

public class ModBlockTagsProvider extends AbstractTagProvider<Block> {

    public ModBlockTagsProvider(DataProviderContext context) {
        super(Registries.BLOCK, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.GUARDED_BY_PIGLINS).add(ModRegistry.ECHO_CHEST_BLOCK.value());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModRegistry.ECHO_CHEST_BLOCK.value());
    }
}
