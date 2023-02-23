package fuzs.echochest.world.level.block.entity;

import com.mojang.serialization.Dynamic;
import fuzs.echochest.EchoChest;
import fuzs.echochest.init.ModRegistry;
import fuzs.echochest.world.inventory.EchoChestMenu;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import org.jetbrains.annotations.Nullable;

public class EchoChestBlockEntity extends ChestBlockEntity implements VibrationListener.VibrationListenerConfig {
    private static final MutableComponent CONTAINER_TITLE = Component.translatable("container.echo_chest");

    private VibrationListener listener;
    private final ContainerOpenersCounter openersCounter;

    public EchoChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModRegistry.ECHO_CHEST_BLOCK_ENTITY_TYPE.get(), blockPos, blockState);
        this.setItems(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
        this.listener = new VibrationListener(new BlockPositionSource(this.worldPosition), 8, this, null, 0.0F, 0);
        this.openersCounter = new ContainerOpenersCounter() {

            @Override
            protected void onOpen(Level level, BlockPos pos, BlockState state) {
                level.playSound(null, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
            }

            @Override
            protected void onClose(Level level, BlockPos pos, BlockState state) {
                level.playSound(null, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, SoundEvents.ENDER_CHEST_CLOSE, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
            }

            @Override
            protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount) {
                level.blockEvent(pos, state.getBlock(), 1, openCount);
            }

            @Override
            protected boolean isOwnContainer(Player player) {
                if (player.containerMenu instanceof EchoChestMenu menu) {
                    Container container = menu.getContainer();
                    return container == EchoChestBlockEntity.this;
                } else {
                    return false;
                }
            }
        };
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    protected Component getDefaultName() {
        return CONTAINER_TITLE;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.setItems(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.getItems());
        }
        if (tag.contains("listener", Tag.TAG_COMPOUND)) {
            VibrationListener.codec(this).parse(new Dynamic<>(NbtOps.INSTANCE, tag.getCompound("listener"))).resultOrPartial(EchoChest.LOGGER::error).ifPresent((vibrationListener) -> {
                this.listener = vibrationListener;
            });
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.remove("Items");
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.getItems(), true);
        }
        VibrationListener.codec(this).encodeStart(NbtOps.INSTANCE, this.listener).resultOrPartial(EchoChest.LOGGER::error).ifPresent((tagx) -> {
            tag.put("listener", tagx);
        });
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new EchoChestMenu(containerId, inventory, this);
    }

    @Override
    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public VibrationListener getListener() {
        return this.listener;
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
            if (!(context.sourceEntity() instanceof ItemEntity)) {
                return false;
            }

            if (context.affectedState() != null) {
                return !context.affectedState().is(BlockTags.DAMPENS_VIBRATIONS);
            } else {
                return true;
            }
        }
    }

    @Override
    public boolean shouldListen(ServerLevel level, GameEventListener listener, BlockPos pos, GameEvent gameEvent, GameEvent.Context context) {
        return true;
    }

    @Override
    public void onSignalReceive(ServerLevel level, GameEventListener listener, BlockPos sourcePos, GameEvent gameEvent, @Nullable Entity sourceEntity, @Nullable Entity projectileOwner, float distance) {
        if (sourceEntity instanceof ItemEntity item) HopperBlockEntity.addItem(this, item);
    }
}
