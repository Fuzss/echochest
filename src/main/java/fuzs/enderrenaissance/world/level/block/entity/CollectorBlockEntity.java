package fuzs.enderrenaissance.world.level.block.entity;

import fuzs.enderrenaissance.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CollectorBlockEntity extends BlockEntity {

    private int horizontalRange;
    private int verticalRange;

    public CollectorBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(ModRegistry.ENDER_COLLECTOR_BLOCK_ENTITY_TYPE.get(), p_155229_, p_155230_);
    }

    public boolean canAcceptStack(ItemStack stack) {

    }

    @Nullable
    public Container getAttachedContainer() {
        HopperBlockEntity.getContainerAt()
    }

    public static boolean isPositionInRange(BlockPos center, BlockPos pos, int horizontalRange, int verticalRange) {
        return isPositionInRange(center.getX(), center.getY(), center.getZ(), pos.getX(), pos.getY(), pos.getZ(), horizontalRange, verticalRange);
    }

    public static boolean isPositionInRange(double centerX, double centerY, double centerZ, double posX, double posY, double posZ, int horizontalRange, int verticalRange) {
        double dimX = Math.abs(centerX - posX);
        double dimY = Math.abs(centerY - posY);
        double dimZ = Math.abs(centerZ - posZ);
        return dimX <= horizontalRange && dimZ <= horizontalRange && dimY <= verticalRange;
    }
}
