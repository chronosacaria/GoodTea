package timefall.goodtea.screens.screenhandlers;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import timefall.goodtea.blocks.entities.TeaKettleBlockEntity;
import timefall.goodtea.enums.TeaKettleSlots;
import timefall.goodtea.registries.ScreenHandlersRegistry;
import timefall.goodtea.util.FluidStack;

@SuppressWarnings("UnstableApiUsage")
public class TeaKettleScreenHandler extends ScreenHandler {
    public final Inventory inventory;
    public PropertyDelegate propertyDelegate;
    public FluidStack fluidStack;

    public TeaKettleScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity entity, PropertyDelegate propertyDelegate) {
        super(ScreenHandlersRegistry.TEA_KETTLE_SCREEN_HANDLER, syncId);
        checkSize((Inventory) entity, TeaKettleBlockEntity.numberOfSlotsInTeaKettle);
        checkDataCount(propertyDelegate, 2);
        this.propertyDelegate = propertyDelegate;
        this.addProperties(propertyDelegate);
        this.inventory = (Inventory) entity;
        inventory.onOpen(playerInventory.player);
        if (entity instanceof TeaKettleBlockEntity teaKettleBlockEntity) {
            this.fluidStack = new FluidStack(teaKettleBlockEntity.fluidStorage.variant, teaKettleBlockEntity.fluidStorage.amount);
        }

        int i, j;
        for(i = 0; i < 3; ++i) {
            for(j = 0; j < 3; ++j) {
                this.addSlot(new Slot(inventory, j + i * 3, 21 + j * 18, 18 + i * 18));
            }
        }

        this.addSlot(new Slot(inventory, TeaKettleSlots.CONTAINER.ordinal(), 83, 54));

        this.addSlot(new Slot(inventory, TeaKettleSlots.RESULT.ordinal(), 130  + 5, 30 + 5));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(propertyDelegate);
    }

    public TeaKettleScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(
                syncId,
                playerInventory,
                resolveBlockEntity(playerInventory, buf),
                new ArrayPropertyDelegate(2));

    }

    private static TeaKettleBlockEntity resolveBlockEntity(PlayerInventory playerInventory, PacketByteBuf buf) {
        var getPos = buf.readBlockPos();
        var blockEntity = playerInventory.player.getWorld().getBlockEntity(getPos);
        return (TeaKettleBlockEntity) blockEntity;
    }

    public void setFluid(FluidStack stack) {
        fluidStack = stack;
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        int progressArrowSize = 23;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
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

