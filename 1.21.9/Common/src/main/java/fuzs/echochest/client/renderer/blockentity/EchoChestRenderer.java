package fuzs.echochest.client.renderer.blockentity;

import fuzs.echochest.EchoChest;
import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import fuzs.puzzleslib.api.client.init.v1.ModelLayerFactory;
import fuzs.puzzleslib.api.client.renderer.v1.SingleChestRenderer;
import net.minecraft.client.model.ChestModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class EchoChestRenderer extends SingleChestRenderer<EchoChestBlockEntity, ChestModel, SingleChestRenderer.SingleChestRenderState> {
    static final ModelLayerFactory MODEL_LAYERS = ModelLayerFactory.from(EchoChest.MOD_ID);
    public static final ModelLayerLocation ECHO_CHEST_MODEL_LAYER_LOCATION = MODEL_LAYERS.registerModelLayer(
            "echo_chest");
    public static final ResourceLocation ECHO_CHEST_TEXTURE = EchoChest.id("echo");
    private static final Material ECHO_CHEST_LOCATION = Sheets.CHEST_MAPPER.apply(ECHO_CHEST_TEXTURE);

    public EchoChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new ChestModel(context.bakeLayer(ECHO_CHEST_MODEL_LAYER_LOCATION)));
    }

    public static LayerDefinition createSingleBodyLayer() {
        return ChestModel.createSingleBodyLayer().apply(meshDefinition -> {
            meshDefinition.getRoot().clearChild("lock");
            return meshDefinition;
        });
    }

    @Override
    protected Material getChestMaterial(EchoChestBlockEntity echoChestBlockEntity, boolean xmasTextures) {
        return xmasTextures ? Sheets.CHEST_XMAS_LOCATION : ECHO_CHEST_LOCATION;
    }
}
