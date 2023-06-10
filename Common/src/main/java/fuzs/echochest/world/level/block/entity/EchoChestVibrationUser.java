package fuzs.echochest.world.level.block.entity;

import fuzs.echochest.init.ModRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.jetbrains.annotations.Nullable;

record EchoChestVibrationUser(EchoChestBlockEntity blockEntity,
                                     PositionSource positionSource) implements VibrationSystem.User {

    public EchoChestVibrationUser(EchoChestBlockEntity blockEntity) {
        this(blockEntity, new BlockPositionSource(blockEntity.getBlockPos()));
    }

    @Override
    public int getListenerRadius() {
        return 8;
    }

    @Override
    public PositionSource getPositionSource() {
        return this.positionSource;
    }

    @Override
    public boolean canReceiveVibration(ServerLevel level, BlockPos pos, GameEvent gameEvent, GameEvent.Context context) {
        return !this.blockEntity.isRemoved() && context.sourceEntity() != null && !context.sourceEntity().isRemoved() && (gameEvent != GameEvent.ENTITY_DIE || context.sourceEntity() instanceof LivingEntity && this.blockEntity.canAcceptExperience());
    }

    @Override
    public void onReceiveVibration(ServerLevel level, BlockPos sourcePos, GameEvent gameEvent, @Nullable Entity sourceEntity, @Nullable Entity projectileOwner, float distance) {

    }

    @Override
    public TagKey<GameEvent> getListenableEvents() {
        return ModRegistry.ECHO_CHEST_CAN_LISTEN;
    }

    @Override
    public boolean isValidVibration(GameEvent gameEvent, GameEvent.Context context) {
        if (!gameEvent.is(this.getListenableEvents())) {
            return false;
        } else {
            Entity entity = context.sourceEntity();
            if (entity != null) {
                if (entity.isSpectator()) {
                    return false;
                }

                if (entity.isSteppingCarefully() && gameEvent.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) {
                    if (this.canTriggerAvoidVibration() && entity instanceof ServerPlayer serverPlayer) {
                        CriteriaTriggers.AVOID_VIBRATION.trigger(serverPlayer);
                    }

                    return false;
                }

                // make sure we can pick up wool block items, wool blocks placed in the world will still prevent vibrations
                if (!(entity instanceof ItemEntity) && entity.dampensVibrations()) {
                    return false;
                }
            } else {
                return false;
            }

            if (context.affectedState() != null) {
                return !context.affectedState().is(BlockTags.DAMPENS_VIBRATIONS);
            } else {
                return true;
            }
        }
    }
}
