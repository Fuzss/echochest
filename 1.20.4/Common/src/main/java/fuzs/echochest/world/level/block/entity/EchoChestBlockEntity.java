package fuzs.echochest.world.level.block.entity;

import com.mojang.serialization.Dynamic;
import fuzs.echochest.EchoChest;
import fuzs.echochest.init.ModRegistry;
import fuzs.echochest.world.inventory.EchoChestMenu;
import fuzs.puzzleslib.api.block.v1.entity.TickingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class EchoChestBlockEntity extends ChestBlockEntity implements WorldlyContainer, GameEventListener.Holder<GameEventListener>, VibrationSystem, TickingBlockEntity {
    public static final String TAG_EXPERIENCE = EchoChest.id("experience").toString();
    public static final MutableComponent CONTAINER_ECHO_CHEST = Component.translatable("container.echo_chest");
    public static final int CONTAINER_SIZE = 25;
    public static final int MAX_EXPERIENCE = 3000;
    public static final int EXPERIENCE_PER_BOTTLE = 7;

    private final int[] allSlots;
    private final int[] inventorySlots;
    private final EchoChestOpenersCounter openersCounter;
    private final GameEventListener listener;
    private final VibrationSystem.User user;
    private final ContainerData dataAccess;
    private VibrationSystem.Data vibrationData;
    private int experience;

    public EchoChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModRegistry.ECHO_CHEST_BLOCK_ENTITY_TYPE.value(), blockPos, blockState);
        this.setItems(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
        this.allSlots = IntStream.range(0, this.getContainerSize()).toArray();
        this.inventorySlots = IntStream.range(1, this.getContainerSize()).toArray();
        this.openersCounter = new EchoChestOpenersCounter(this);
        this.listener = new VibrationSystem.Listener(this);
        this.user = new EchoChestVibrationUser(this);
        this.vibrationData = new VibrationSystem.Data();
        this.dataAccess = new ContainerData() {

            @Override
            public int get(int index) {
                return index == 0 ? EchoChestBlockEntity.this.experience : 0;
            }

            @Override
            public void set(int index, int value) {
                if (index == 0) {
                    EchoChestBlockEntity.this.experience = value;
                }
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public void clientTick() {
        lidAnimateTick(this.getLevel(), this.getBlockPos(), this.getBlockState(), this);
    }

    @Override
    public void serverTick() {
        Ticker.tick(this.getLevel(), this.vibrationData, this.user);
        if (this.experience >= EXPERIENCE_PER_BOTTLE) {
            if (EchoChestMenu.validBottleItem(this.getItem(0)) && HopperBlockEntity.addItem(null, this, new ItemStack(Items.EXPERIENCE_BOTTLE), null).isEmpty()) {
                this.extractExperienceBottle();
                ItemStack stack = this.getItem(0).copy();
                stack.shrink(1);
                this.setItem(0, stack);
            }
        }
    }

    void animate() {
        BlockPos blockPos = this.getBlockPos();
        this.openersCounter.incrementOpeners(null, this.getLevel(), blockPos, this.getBlockState());
        if (!this.getBlockState().getValue(EnderChestBlock.WATERLOGGED)) {
            this.getLevel().playSound(null, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, SoundEvents.DEEPSLATE_BRICKS_BREAK, SoundSource.BLOCKS, 1.0F, this.getLevel().random.nextFloat() * 0.2F + 0.8F);
        }
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    protected Component getDefaultName() {
        return CONTAINER_ECHO_CHEST;
    }

    public boolean canAcceptExperience() {
        return this.experience < MAX_EXPERIENCE;
    }

    public void acceptExperience(int amount) {
        int lastExperience = this.experience;
        this.experience = Mth.clamp(this.experience + amount, 0, MAX_EXPERIENCE);
        if (this.experience != lastExperience) this.setChanged();
    }

    public void extractExperienceBottle() {
        this.acceptExperience(-EXPERIENCE_PER_BOTTLE);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.setItems(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.getItems());
        }
        this.experience = tag.getInt(TAG_EXPERIENCE);
        if (tag.contains("listener", 10)) {
            VibrationSystem.Data.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, tag.getCompound("listener"))).resultOrPartial(EchoChest.LOGGER::error).ifPresent((data) -> {
                this.vibrationData = data;
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
        tag.putInt(TAG_EXPERIENCE, this.experience);
        VibrationSystem.Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData).resultOrPartial(EchoChest.LOGGER::error).ifPresent((data) -> {
            tag.put("listener", data);
        });
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new EchoChestMenu(containerId, inventory, this, this.dataAccess);
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

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index != 0 || EchoChestMenu.validBottleItem(stack);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side.getAxis().getPlane() == Direction.Plane.HORIZONTAL ? this.allSlots : this.inventorySlots;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        // don't allow picked up glass bottles to go into the bottles slot, would mess with witch farms where the player wants to farm xp (=wants to leave the xp tank filled)
        return (index != 0 || direction != null) && this.canPlaceItem(index, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index != 0;
    }

    @Override
    public GameEventListener getListener() {
        return this.listener;
    }

    @Override
    public User getVibrationUser() {
        return this.user;
    }

    @Override
    public Data getVibrationData() {
        return this.vibrationData;
    }
}
