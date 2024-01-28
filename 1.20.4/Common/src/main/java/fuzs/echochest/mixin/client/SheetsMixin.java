package fuzs.echochest.mixin.client;

import fuzs.echochest.client.init.ClientModRegistry;
import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

@Mixin(Sheets.class)
abstract class SheetsMixin {

    @Inject(method = "getAllMaterials", at = @At("TAIL"))
    private static void getAllMaterials(Consumer<Material> materialConsumer, CallbackInfo callback) {
        materialConsumer.accept(ClientModRegistry.ECHO_CHEST_LOCATION);
    }

    @Inject(method = "chooseMaterial", at = @At("HEAD"), cancellable = true)
    private static void chooseMaterial(BlockEntity blockEntity, ChestType chestType, boolean holiday, CallbackInfoReturnable<Material> callback) {
        if (blockEntity instanceof EchoChestBlockEntity) callback.setReturnValue(ClientModRegistry.ECHO_CHEST_LOCATION);
    }
}
