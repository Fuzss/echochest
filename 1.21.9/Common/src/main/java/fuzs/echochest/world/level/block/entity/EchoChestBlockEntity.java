package fuzs.echochest.world.level.block.entity;

import fuzs.echochest.EchoChest;
import fuzs.echochest.init.ModRegistry;
import fuzs.echochest.world.inventory.EchoChestMenu;
import fuzs.puzzleslib.api.block.v1.entity.TickingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class EchoChestBlockEntity extends ChestBlockEntity implements WorldlyContainer, GameEventListener.Provider<GameEventListener>, TickingBlockEntity {
    public static final String TAG_EXPERIENCE = EchoChest.id("experience").toString();
    public static final MutableComponent CONTAINER_ECHO_CHEST = Component.translatable("container.echo_chest");
    public static final int CONTAINER_SIZE = 25;
    public static final int MAX_EXPERIENCE = 3000;
    public static final int EXPERIENCE_PER_BOTTLE = 7;

    private final ContainerData dataAccess;
    private final EchoChestOpenersCounter openersCounter;
    private final int[] allSlots;
    private final int[] inventorySlots;
    private final GameEventListener listener;
    private int experience;

    public EchoChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModRegistry.ECHO_CHEST_BLOCK_ENTITY_TYPE.value(), blockPos, blockState);
        this.setItems(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
        this.allSlots = IntStream.range(0, this.getContainerSize()).toArray();
        this.inventorySlots = IntStream.range(1, this.getContainerSize()).toArray();
        this.openersCounter = new EchoChestOpenersCounter(this);
        this.listener = new EchoChestListener(this);
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
        if (this.experience >= EXPERIENCE_PER_BOTTLE) {
            if (EchoChestMenu.validBottleItem(this.getItem(0)) && HopperBlockEntity.addItem(null,
                    this,
                    new ItemStack(Items.EXPERIENCE_BOTTLE),
                    null).isEmpty()) {
                this.extractExperienceBottle();
                ItemStack stack = this.getItem(0).copy();
                stack.shrink(1);
                this.setItem(0, stack);
            }
        }
    }

    void animate() {
        this.openersCounter.incrementOpeners(null, this.getLevel(), this.getBlockPos(), this.getBlockState());
        if (!this.getBlockState().getValue(EnderChestBlock.WATERLOGGED)) {
            this.getLevel()
                    .playSound(null,
                            this.getBlockPos().getX() + 0.5,
                            this.getBlockPos().getY() + 0.5,
                            this.getBlockPos().getZ() + 0.5,
                            SoundEvents.DEEPSLATE_BRICKS_BREAK,
                            SoundSource.BLOCKS,
                            1.0F,
                            this.getLevel().random.nextFloat() * 0.2F + 0.8F);
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
        if (this.experience != lastExperience) {
            this.setChanged();
        }
    }

    public void extractExperienceBottle() {
        this.acceptExperience(-EXPERIENCE_PER_BOTTLE);
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        this.setItems(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
        if (!this.tryLoadLootTable(valueInput)) {
            ContainerHelper.loadAllItems(valueInput, this.getItems());
        }
        this.experience = valueInput.getIntOr(TAG_EXPERIENCE, 0);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        if (!this.trySaveLootTable(valueOutput)) {
            ContainerHelper.saveAllItems(valueOutput, this.getItems(), true);
        }
        valueOutput.putInt(TAG_EXPERIENCE, this.experience);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new EchoChestMenu(containerId, inventory, this, this.dataAccess);
    }

    @Override
    public void startOpen(ContainerUser containerUser) {
        if (!this.remove && !containerUser.getLivingEntity().isSpectator()) {
            this.openersCounter.incrementOpeners(containerUser.getLivingEntity(),
                    this.getLevel(),
                    this.getBlockPos(),
                    this.getBlockState());
        }
    }

    @Override
    public void stopOpen(ContainerUser player) {
        if (!this.remove && !player.getLivingEntity().isSpectator()) {
            this.openersCounter.decrementOpeners(player.getLivingEntity(),
                    this.getLevel(),
                    this.getBlockPos(),
                    this.getBlockState());
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
}
