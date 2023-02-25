package fuzs.echochest.data;

import fuzs.echochest.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(ModRegistry.ECHO_CHEST_BLOCK.get())
                .define('#', Blocks.DEEPSLATE)
                .define('+', Blocks.DEEPSLATE_BRICKS)
                .define('@', Items.ECHO_SHARD)
                .pattern("#+#")
                .pattern("+@+")
                .pattern("#+#")
                .unlockedBy(getHasName(Items.ECHO_SHARD), has(Items.ECHO_SHARD))
                .save(recipeConsumer);
    }
}
