package fuzs.echochest.data;

import fuzs.echochest.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModRegistry.ECHO_CHEST_BLOCK.get())
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
