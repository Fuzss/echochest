package fuzs.echochest.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

record EchoChestListener(EchoChestBlockEntity blockEntity) implements GameEventListener {

    @Override
    public PositionSource getListenerSource() {
        return this.blockEntity.getVibrationUser().getPositionSource();
    }

    @Override
    public int getListenerRadius() {
        return this.blockEntity.getVibrationUser().getListenerRadius();
    }

    @Override
    public boolean handleGameEvent(ServerLevel level, GameEvent gameEvent, GameEvent.Context context, Vec3 pos) {
        if (this.blockEntity.getVibrationUser().isValidVibration(gameEvent, context)) {
            Optional<Vec3> optional = this.getListenerSource().getPosition(level);
            if (optional.isPresent()) {
                Vec3 destination = optional.get();
                if (!this.blockEntity.getVibrationUser().canReceiveVibration(level, BlockPos.containing(pos), gameEvent, context)) {
                    return false;
                } else if (EchoChestBlockEntity.isOccluded(level, pos, destination)) {
                    return false;
                } else if (this.onSignalReceive(level, gameEvent, context, pos, destination)) {
                    EchoChestBlockEntity.animate(level, this.blockEntity.getBlockPos(), this.blockEntity.getBlockState(), this.blockEntity.openersCounter);
                    int travelTimeInTicks = Mth.floor(pos.distanceTo(destination));
                    level.sendParticles(new VibrationParticleOption(this.getListenerSource(), travelTimeInTicks), pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean onSignalReceive(ServerLevel level, GameEvent event, GameEvent.Context context, Vec3 origin, Vec3 destination) {
        if (event == GameEvent.ENTITY_DIE) {
            if (context.sourceEntity() instanceof LivingEntity livingEntity) {
                if (this.blockEntity.canAcceptExperience() && !livingEntity.wasExperienceConsumed()) {
                    int experienceReward = livingEntity.getExperienceReward();
                    if (livingEntity.shouldDropExperience() && experienceReward > 0) {
                        this.blockEntity.acceptExperience(experienceReward);
                    }

                    livingEntity.skipDropExperience();
                    return true;
                }
            }
        } else if (context.sourceEntity() instanceof ItemEntity item && !item.isRemoved()) {
            int lastCount = item.getItem().getCount();
            return HopperBlockEntity.addItem(this.blockEntity, item) || lastCount != item.getItem().getCount();
        }
        return false;
    }
}
