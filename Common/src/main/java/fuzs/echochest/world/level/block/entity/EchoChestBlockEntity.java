package fuzs.echochest.world.level.block.entity;

import fuzs.echochest.init.ModRegistry;
import fuzs.echochest.world.inventory.EchoChestMenu;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.IntStream;

public class EchoChestBlockEntity extends ChestBlockEntity implements WorldlyContainer, GameEventListener, VibrationListener.VibrationListenerConfig {
    public static final String TAG_EXPERIENCE = "Experience";
    public static final MutableComponent CONTAINER_TITLE = Component.translatable("container.echo_chest");
    public static final int CONTAINER_SIZE = 25;
    public static final int MAX_EXPERIENCE = 3000;
    public static final int EXPERIENCE_PER_BOTTLE = 7;

    private final EchoChestOpenersCounter openersCounter;
    private final int[] allSlots;
    private final int[] inventorySlots;
    private final BlockPositionSource blockPosSource;
    private int experience;
    private final ContainerData dataAccess = new ContainerData() {

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

    public EchoChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModRegistry.ECHO_CHEST_BLOCK_ENTITY_TYPE.get(), blockPos, blockState);
        this.setItems(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
        this.blockPosSource = new BlockPositionSource(this.worldPosition);
        this.allSlots = IntStream.range(0, this.getContainerSize()).toArray();
        this.inventorySlots = IntStream.range(1, this.getContainerSize()).toArray();
        this.openersCounter = new EchoChestOpenersCounter(this);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EchoChestBlockEntity blockEntity) {
        if (blockEntity.experience >= EXPERIENCE_PER_BOTTLE) {
            if (EchoChestMenu.validBottleItem(blockEntity.getItem(0)) && HopperBlockEntity.addItem(null, blockEntity, new ItemStack(Items.EXPERIENCE_BOTTLE), null).isEmpty()) {
                blockEntity.extractExperienceBottle();
                ItemStack stack = blockEntity.getItem(0).copy();
                stack.shrink(1);
                blockEntity.setItem(0, stack);
            }
        }
    }

    private static void animate(Level level, BlockPos pos, BlockState state, EchoChestOpenersCounter openersCounter) {
        openersCounter.incrementOpeners(null, level, pos, state);
        if (!state.getValue(EnderChestBlock.WATERLOGGED)) {
            level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.DEEPSLATE_BRICKS_BREAK, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.2F + 0.8F);
        }
    }

    private static boolean isOccluded(Level level, Vec3 from, Vec3 to) {
        from = new Vec3((double) Mth.floor(from.x) + 0.5, (double) Mth.floor(from.y) + 0.5, (double) Mth.floor(from.z) + 0.5);
        to = new Vec3((double) Mth.floor(to.x) + 0.5, (double) Mth.floor(to.y) + 0.5, (double) Mth.floor(to.z) + 0.5);

        for (Direction direction : Direction.values()) {
            Vec3 vec3 = from.relative(direction, 9.999999747378752E-6);
            if (level.isBlockInLine(new ClipBlockStateContext(vec3, to, (blockState) -> {
                return blockState.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS);
            })).getType() != HitResult.Type.BLOCK) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    protected Component getDefaultName() {
        return CONTAINER_TITLE;
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
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.remove("Items");
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.getItems(), true);
        }
        tag.putInt(TAG_EXPERIENCE, this.experience);
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

    @Override
    public boolean shouldListen(ServerLevel level, GameEventListener listener, BlockPos pos, GameEvent gameEvent, GameEvent.Context context) {
        return !this.isRemoved() && context.sourceEntity() != null && !context.sourceEntity().isRemoved() && (gameEvent != GameEvent.ENTITY_DIE || context.sourceEntity() instanceof LivingEntity && this.canAcceptExperience());
    }

    @Override
    public void onSignalReceive(ServerLevel level, GameEventListener listener, BlockPos sourcePos, GameEvent gameEvent, @Nullable Entity sourceEntity, @Nullable Entity projectileOwner, float distance) {

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
    public PositionSource getListenerSource() {
        return this.blockPosSource;
    }

    @Override
    public int getListenerRadius() {
        return 8;
    }

    @Override
    public boolean handleGameEvent(ServerLevel level, GameEvent.Message eventMessage) {
        GameEvent gameEvent = eventMessage.gameEvent();
        GameEvent.Context context = eventMessage.context();
        if (!this.isValidVibration(gameEvent, context)) {
            return false;
        } else {
            Optional<Vec3> optional = this.getListenerSource().getPosition(level);
            if (optional.isEmpty()) {
                return false;
            } else {
                Vec3 origin = eventMessage.source();
                Vec3 destination = optional.get();
                if (!this.shouldListen(level, this, new BlockPos(origin), gameEvent, context)) {
                    return false;
                } else if (isOccluded(level, origin, destination)) {
                    return false;
                } else if (this.onSignalReceive(level, gameEvent, context, origin, destination)) {
                    animate(this.level, this.getBlockPos(), this.getBlockState(), this.openersCounter);
                    int travelTimeInTicks = Mth.floor(origin.distanceTo(destination));
                    level.sendParticles(new VibrationParticleOption(this.getListenerSource(), travelTimeInTicks), origin.x, origin.y, origin.z, 1, 0.0, 0.0, 0.0, 0.0);
                    return true;
                }
                return false;
            }
        }
    }

    private boolean onSignalReceive(ServerLevel level, GameEvent event, GameEvent.Context context, Vec3 origin, Vec3 destination) {
        if (event == GameEvent.ENTITY_DIE) {
            if (context.sourceEntity() instanceof LivingEntity livingEntity) {
                if (this.canAcceptExperience() && !livingEntity.wasExperienceConsumed()) {
                    int experienceReward = livingEntity.getExperienceReward();
                    if (livingEntity.shouldDropExperience() && experienceReward > 0) {
                        this.acceptExperience(experienceReward);
                    }

                    livingEntity.skipDropExperience();
                    return true;
                }
            }
        } else if (context.sourceEntity() instanceof ItemEntity item && !item.isRemoved()) {
            int lastCount = item.getItem().getCount();
            return HopperBlockEntity.addItem(this, item) || lastCount != item.getItem().getCount();
        }
        return false;
    }
}
