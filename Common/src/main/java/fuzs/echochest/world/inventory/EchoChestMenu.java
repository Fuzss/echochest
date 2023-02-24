package fuzs.echochest.world.inventory;

import fuzs.echochest.init.ModRegistry;
import fuzs.echochest.world.level.block.entity.EchoChestBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class EchoChestMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData containerData;

    public EchoChestMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(EchoChestBlockEntity.CONTAINER_SIZE), new SimpleContainerData(1));
    }

    public EchoChestMenu(int containerId, Inventory inventory, Container container, ContainerData containerData) {
        super(ModRegistry.ECHO_CHEST_MENU_TYPE.get(), containerId);
        checkContainerSize(container, EchoChestBlockEntity.CONTAINER_SIZE);
        this.container = container;
        this.containerData = containerData;
        container.startOpen(inventory.player);
        this.addDataSlots(containerData);

        this.addSlot(new Slot(container, 0, 19, 73) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return validBottleItem(stack);
            }
        });

        for (int l = 0; l < 4; ++l) {
            for (int m = 0; m < 6; ++m) {
                this.addSlot(new Slot(container, m + l * 6 + 1, 51 + m * 18, 24 + l * 18));
            }
        }

        for (int l = 0; l < 3; ++l) {
            for (int m = 0; m < 9; ++m) {
                this.addSlot(new Slot(inventory, m + l * 9 + 9, 8 + m * 18, 118 + l * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(inventory, l, 8 + l * 18, 176));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index < EchoChestBlockEntity.CONTAINER_SIZE) {
                if (!this.moveItemStackTo(itemStack2, EchoChestBlockEntity.CONTAINER_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!validBottleItem(itemStack2) || !this.moveItemStackTo(itemStack2, 0, 1, false)) {
                if (!this.moveItemStackTo(itemStack2, 1, EchoChestBlockEntity.CONTAINER_SIZE, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    public Container getContainer() {
        return this.container;
    }

    public float getExperiencePercentage() {
        return this.containerData.get(0) / (float) EchoChestBlockEntity.MAX_EXPERIENCE;
    }

    public static boolean validBottleItem(ItemStack stack) {
        return stack.is(Items.GLASS_BOTTLE);
    }
}