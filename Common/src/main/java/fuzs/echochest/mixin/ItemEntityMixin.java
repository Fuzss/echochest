package fuzs.echochest.mixin;

import fuzs.echochest.init.ModRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
abstract class ItemEntityMixin extends Entity {
    // set this initially so newly spawned entities aren't immediately picked up
    @Unique
    private int echochest$tickCooldown = 30 + this.random.nextInt(20);

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo callback) {
        if (!this.isRemoved() && !this.level().isClientSide) {
            if (--this.echochest$tickCooldown == 0) {
                this.gameEvent(ModRegistry.ITEM_TICK_GAME_EVENT.get());
                // add randomness, since only a single game event seems to be processed per listener per tick
                this.echochest$tickCooldown = 30 + this.random.nextInt(20);
            }
        }
    }
}
