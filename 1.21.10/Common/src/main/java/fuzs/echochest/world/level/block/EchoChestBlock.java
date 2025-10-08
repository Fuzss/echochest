package fuzs.echochest.world.level.block;

import fuzs.echochest.init.ModRegistry;
import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import fuzs.puzzleslib.api.block.v1.entity.TickingEntityBlock;
import fuzs.puzzleslib.api.util.v1.InteractionResultHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class EchoChestBlock extends EnderChestBlock implements TickingEntityBlock<EchoChestBlockEntity> {
    public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;

    public EchoChestBlock(Properties properties) {
        super(properties);
        // add this as Fabric Api assumes instances of ChestBlockEntity have it, maybe other mods will, too
        this.registerDefaultState(this.defaultBlockState().setValue(TYPE, ChestType.SINGLE));
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, boolean isMoving) {
        Containers.updateNeighboursAfterDestroy(blockState, serverLevel, blockPos);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockPos above = pos.above();
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else if (!level.getBlockState(above).isRedstoneConductor(level, above)) {
            if (level.getBlockEntity(pos) instanceof EchoChestBlockEntity blockEntity) {
                player.openMenu(blockEntity);
                PiglinAi.angerNearbyPiglins((ServerLevel) level, player, true);
            }
            return InteractionResult.CONSUME;
        } else {
            return InteractionResultHelper.sidedSuccess(level.isClientSide());
        }
    }

    @Override
    public BlockEntityType<? extends EchoChestBlockEntity> getBlockEntityType() {
        return ModRegistry.ECHO_CHEST_BLOCK_ENTITY_TYPE.value();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return TickingEntityBlock.super.newBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return TickingEntityBlock.super.getTicker(level, state, blockEntityType);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos, Direction direction) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockPos));
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof ChestBlockEntity blockEntity) {
            blockEntity.recheckOpen();
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // NO-OP
    }

    public Component getDescriptionComponent() {
        return Component.translatable(this.getDescriptionId() + ".description").withStyle(ChatFormatting.GOLD);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }
}
