package fuzs.echochest.client.renderer.special;

import com.mojang.serialization.MapCodec;
import fuzs.echochest.client.renderer.blockentity.EchoChestRenderer;
import net.minecraft.client.model.ChestModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public record UnbakedEchoChestSpecialRenderer(ResourceLocation texture,
                                              float openness) implements SpecialModelRenderer.Unbaked {
    public static final MapCodec<UnbakedEchoChestSpecialRenderer> MAP_CODEC = ChestSpecialRenderer.Unbaked.MAP_CODEC.xmap(
            (ChestSpecialRenderer.Unbaked unbaked) -> new UnbakedEchoChestSpecialRenderer(unbaked.texture(),
                    unbaked.openness()),
            (UnbakedEchoChestSpecialRenderer unbaked) -> new ChestSpecialRenderer.Unbaked(unbaked.texture(),
                    unbaked.openness()));

    public UnbakedEchoChestSpecialRenderer(ResourceLocation texture) {
        this(texture, 0.0F);
    }

    @Override
    public MapCodec<UnbakedEchoChestSpecialRenderer> type() {
        return MAP_CODEC;
    }

    @Override
    public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
        // we have to use the bake our own model layer, as the echo chest does not have a lock model part like other chests
        // using the vanilla model will render the lock black, since a solid render type is used, not cutout
        ChestModel chestModel = new ChestModel(modelSet.bakeLayer(EchoChestRenderer.ECHO_CHEST_MODEL_LAYER_LOCATION));
        Material material = Sheets.CHEST_MAPPER.apply(this.texture);
        return new ChestSpecialRenderer(chestModel, material, this.openness);
    }
}
