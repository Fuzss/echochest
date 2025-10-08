package fuzs.echochest.world.level.block.entity;

import fuzs.echochest.world.inventory.EchoChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

class EchoChestOpenersCounter {
    private final Container container;
    private int openCount;

    public EchoChestOpenersCounter(Container container) {
        this.container = container;
    }

    protected void onOpen(Level level, BlockPos pos, BlockState state) {
        level.playSound(null,
                (double) pos.getX() + 0.5,
                (double) pos.getY() + 0.5,
                (double) pos.getZ() + 0.5,
                SoundEvents.CHEST_OPEN,
                SoundSource.BLOCKS,
                0.5F,
                level.random.nextFloat() * 0.1F + 0.9F);
    }

    protected void onClose(Level level, BlockPos pos, BlockState state) {
        level.playSound(null,
                (double) pos.getX() + 0.5,
                (double) pos.getY() + 0.5,
                (double) pos.getZ() + 0.5,
                SoundEvents.CHEST_CLOSE,
                SoundSource.BLOCKS,
                0.5F,
                level.random.nextFloat() * 0.1F + 0.9F);
    }

    protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount) {
        level.blockEvent(pos, state.getBlock(), 1, openCount);
    }

    protected boolean isOwnContainer(Player player) {
        if (player.containerMenu instanceof EchoChestMenu menu) {
            Container container = menu.getContainer();
            return container == this.container;
        } else {
            return false;
        }
    }

    public void incrementOpeners(@Nullable LivingEntity livingEntity, Level level, BlockPos pos, BlockState state) {
        int i = this.openCount++;
        if (i == 0) {
            if (livingEntity != null) {
                this.onOpen(level, pos, state);
            }

            level.gameEvent(livingEntity, GameEvent.CONTAINER_OPEN, pos);
            scheduleRecheck(level, pos, state, livingEntity == null);
        }

        this.openerCountChanged(level, pos, state, i, this.openCount);
    }

    public void decrementOpeners(LivingEntity livingEntity, Level level, BlockPos pos, BlockState state) {
        int i = this.openCount--;
        if (this.openCount == 0) {
            this.onClose(level, pos, state);
            level.gameEvent(livingEntity, GameEvent.CONTAINER_CLOSE, pos);
        }

        this.openerCountChanged(level, pos, state, i, this.openCount);
    }

    private int getOpenCount(Level level, BlockPos pos) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        float f = 5.0F;
        AABB aABB = new AABB((float) i - 5.0F,
                (float) j - 5.0F,
                (float) k - 5.0F,
                (float) (i + 1) + 5.0F,
                (float) (j + 1) + 5.0F,
                (float) (k + 1) + 5.0F);
        return level.getEntities(EntityTypeTest.forClass(Player.class), aABB, this::isOwnContainer).size();
    }

    public void recheckOpeners(Level level, BlockPos pos, BlockState state) {
        int i = this.getOpenCount(level, pos);
        int j = this.openCount;
        if (j != i) {
            boolean bl = i != 0;
            boolean bl2 = j != 0;
            if (bl && !bl2) {
                // remove open sound for recheck
                level.gameEvent(null, GameEvent.CONTAINER_OPEN, pos);
            } else if (!bl) {
                // remove close sound for recheck
                level.gameEvent(null, GameEvent.CONTAINER_CLOSE, pos);
            }

            this.openCount = i;
        }

        this.openerCountChanged(level, pos, state, j, i);
        if (i > 0) {
            scheduleRecheck(level, pos, state, false);
        }

    }

    public int getOpenerCount() {
        return this.openCount;
    }

    private static void scheduleRecheck(Level level, BlockPos pos, BlockState state, boolean quick) {
        level.scheduleTick(pos, state.getBlock(), quick ? 3 : 5);
    }
}
