/* The credit for this code goes to ianm1647 of farmersdelight and the original can be found below:
 * https://github.com/ianm1647/farmersdelight/blob/master/src/main/java/com/nhoryzon/mc/farmersdelight/item/inventory/SlotItemHandler.java
 * This piece of code is under the MIT License:
 * MIT License
 *
 * Copyright (c) 2020 vectorwing, Zifiv
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package timefall.goodtea.handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import timefall.goodtea.interfaces.ISidedInventoryHandler;

public class SlotItemHandler extends Slot {
    private static final Inventory EMPTY_INVENTORY = new SimpleInventory(0);
    private final ISidedInventoryHandler itemHandler;
    private final int index;

    public SlotItemHandler(ISidedInventoryHandler itemHandler, int index, int xPosition, int yPosition) {
        super(EMPTY_INVENTORY, index, xPosition, yPosition);
        this.itemHandler = itemHandler;
        this.index = index;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        return itemHandler.isValid(index, stack);
    }

    @Override
    public ItemStack getStack() {
        return getItemHandler().getStack(index);
    }

    @Override
    public void setStack(ItemStack stack) {
        getItemHandler().setStack(index, stack);
        markDirty();
    }

    @Override
    public int getMaxItemCount() {
        return itemHandler.getMaxCountForSlot(this.index);
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        ItemStack maxAdd = stack.copy();
        int maxInput = stack.getMaxCount();
        maxAdd.setCount(maxInput);

        ISidedInventoryHandler handler = this.getItemHandler();
        ItemStack currentStack = handler.getStack(index);

        handler.setStack(index, ItemStack.EMPTY);
        ItemStack remainder = handler.insertItemStack(index, maxAdd, true);
        handler.setStack(index, currentStack);

        return maxInput - remainder.getCount();
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerIn) {
        return !getItemHandler().extractItemStack(index, 1, true).isEmpty();
    }

    @Override
    public ItemStack takeStack(int amount) {
        return this.getItemHandler().extractItemStack(index, amount, false);
    }

    public ISidedInventoryHandler getItemHandler() {
        return itemHandler;
    }
}