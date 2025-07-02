package fuzs.echochest.world.level.block.entity;

import fuzs.echochest.init.ModRegistry;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

final class EchoChestListener implements GameEventListener {
    private final EchoChestBlockEntity blockEntity;
    private final PositionSource positionSource;

    EchoChestListener(EchoChestBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
        this.positionSource = new BlockPositionSource(blockEntity.getBlockPos());
    }

    @Override
    public PositionSource getListenerSource() {
        return this.positionSource;
    }

    @Override
    public int getListenerRadius() {
        return 8;
    }

    @Override
    public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos) {
        if (this.isValidVibration(gameEvent, context)) {
            Optional<Vec3> optional = this.getListenerSource().getPosition(level);
            if (optional.isPresent()) {
                Vec3 destination = optional.get();
                if (VibrationSystem.Listener.isOccluded(level, pos, destination)) {
                    return false;
                } else if (!this.canReceiveVibration(level, BlockPos.containing(pos), gameEvent, context)) {
                    return false;
                } else {
                    this.blockEntity.animate();
                    int travelTimeInTicks = Mth.floor(pos.distanceTo(destination));
                    level.sendParticles(new VibrationParticleOption(this.getListenerSource(), travelTimeInTicks), pos.x(),
                            pos.y(), pos.z(), 1, 0.0, 0.0, 0.0, 0.0
                    );

                    return true;
                }
            }
        }
        return false;
    }

    public boolean isValidVibration(Holder<GameEvent> gameEvent, GameEvent.Context context) {
        if (!gameEvent.is(ModRegistry.ECHO_CHEST_CAN_LISTEN_GAME_EVENT_TAG)) {
            return false;
        } else {
            Entity entity = context.sourceEntity();
            if (entity != null) {
                if (entity.isSpectator()) {
                    return false;
                }

                if (entity.isSteppingCarefully() && gameEvent.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) {
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

    public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, GameEvent.Context context) {
        if (gameEvent.is(GameEvent.ENTITY_DIE)) {
            if (context.sourceEntity() instanceof LivingEntity livingEntity) {
                if (this.blockEntity.canAcceptExperience() && !livingEntity.wasExperienceConsumed()) {
                    DamageSource damageSource = livingEntity.getLastDamageSource();
                    int experienceReward = livingEntity.getExperienceReward(level, Optionull.map(damageSource, DamageSource::getEntity));
                    if (livingEntity.shouldDropExperience() && experienceReward > 0) {
                        this.blockEntity.acceptExperience(experienceReward);
                    }

                    livingEntity.skipDropExperience();
                    return true;
                }
            }
        } else if (context.sourceEntity() instanceof ItemEntity item && !item.isRemoved()) {
            int oldCount = item.getItem().getCount();
            return HopperBlockEntity.addItem(this.blockEntity, item) || oldCount != item.getItem().getCount();
        }

        return false;
    }

    @Override
    public DeliveryMode getDeliveryMode() {
        return DeliveryMode.BY_DISTANCE;
    }
}
