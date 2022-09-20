package timefall.goodtea.screens.screenhandlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import timefall.goodtea.blocks.entities.TeaKettleBlockEntity;
import timefall.goodtea.registries.ScreenHandlersRegistry;

public class TeaKettleScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    public TeaKettleScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(TeaKettleBlockEntity.numberOfSlotsInTeaKettle), new ArrayPropertyDelegate(2));
    }

    public TeaKettleScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ScreenHandlersRegistry.TEA_KETTLE_SCREEN_HANDLER, syncId);
        checkSize(inventory, TeaKettleBlockEntity.numberOfSlotsInTeaKettle);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = propertyDelegate;

        this.addSlot(new Slot(inventory, 0, 20 + 1, 17 + 1));
        this.addSlot(new Slot(inventory, 1, 38 + 1, 17 + 1));
        this.addSlot(new Slot(inventory, 2, 56 + 1, 17 + 1));
        this.addSlot(new Slot(inventory, 3, 20 + 1, 35 + 1));
        this.addSlot(new Slot(inventory, 4, 38 + 1, 35 + 1));
        this.addSlot(new Slot(inventory, 5, 56 + 1, 35 + 1));
        this.addSlot(new Slot(inventory, 6, 20 + 1, 53 + 1));
        this.addSlot(new Slot(inventory, 7, 38 + 1, 53 + 1));
        this.addSlot(new Slot(inventory, 8, 56 + 1, 53 + 1));
        this.addSlot(new Slot(inventory, 9, 111 + 5, 30 + 5));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(propertyDelegate);
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int progressArrowSize = 26;

        return maxProgress !=0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 9; l++) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}

