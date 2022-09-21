package timefall.goodtea.blocks.entities;


import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import timefall.goodtea.recipes.NewTeaRecipe;
import timefall.goodtea.registries.BlockEntitiesRegistry;
import timefall.goodtea.screens.screenhandlers.TeaKettleScreenHandler;

import java.util.Optional;

public class TeaKettleBlockEntity extends LockableContainerBlockEntity implements SidedInventory {
    public static int numberOfSlotsInTeaKettle = 10;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(numberOfSlotsInTeaKettle, ItemStack.EMPTY);
    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;
    private static final int[] BOTTOM_SLOTS = new int[]{9};
    private static final int[] TOP_AND_SIDE_SLOTS = new int[]{0,1,2,3,4,5,6,7,8};
    //private static final ScreenHandler handler = new TeaKettleScreenHandler(0, (PlayerInventory) TeaKettleScreenHandler.inventory);
    public TeaKettleBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.TEA_KETTLE_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> TeaKettleBlockEntity.this.progress;
                    case 1 -> TeaKettleBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> TeaKettleBlockEntity.this.progress = value;
                    case 1 -> TeaKettleBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public boolean isOnLitSource() {
        if (world != null) {
            BlockState checkState = world.getBlockState(pos.down());
            if (checkState.contains(Properties.LIT)) {
                return checkState.get(Properties.LIT);
            }
            return true;
        }
        return false;
    }

    public static void tick(World world, BlockPos pos, BlockState state, TeaKettleBlockEntity blockEntity) {
        if (blockEntity.isOnLitSource()) {
            blockEntity.progress++;
            markDirty(world, pos, state);
            if (blockEntity.progress >= blockEntity.maxProgress) {
                craftItem(blockEntity);
            }
        } else {
            blockEntity.resetProgress();
            markDirty(world, pos, state);
        }

    }

    //private static boolean canCraft(DefaultedList<ItemStack> slots) {
    //    ItemStack itemStack = slots.get(TeaKettleBlockEntity.numberOfSlotsInTeaKettle - 1);
    //    if (itemStack.isEmpty()) {
    //        return false;
    //    } else {
    //        for(int i = 0; i < 9; ++i) {
    //            ItemStack itemStack2 = slots.get(i);
    //            if (!itemStack2.isEmpty() && hasRecipe(teaKettleBlockEntity)) {
    //                return true;
    //            }
    //        }
    //
    //        return false;
    //    }
    //}

    private static void craftItem(TeaKettleBlockEntity entity) {
        ScreenHandler handler = new ScreenHandler(null, 0) {
            @Override
            public ItemStack transferSlot(PlayerEntity player, int index) {
                return ItemStack.EMPTY;
            }

            @Override
            public boolean canUse(PlayerEntity player) {
                return true;
            }
        };
        CraftingInventory inventory = new CraftingInventory(handler, 3, 3);
        for (int i = 0; i < inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<NewTeaRecipe> recipe = entity.getWorld().getRecipeManager()
                .getFirstMatch(NewTeaRecipe.Type.INSTANCE, inventory, entity.getWorld());

        if (hasRecipe(entity)) {
            entity.removeStack(0,1);
            entity.setStack(numberOfSlotsInTeaKettle - 1, new ItemStack(recipe.get().getOutput().getItem(),
                    entity.getStack(numberOfSlotsInTeaKettle - 1).getCount() + 1));
            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(TeaKettleBlockEntity entity) {
        ScreenHandler handler = new ScreenHandler(null, 0) {
            @Override
            public ItemStack transferSlot(PlayerEntity player, int index) {
                return ItemStack.EMPTY;
            }

            @Override
            public boolean canUse(PlayerEntity player) {
                return true;
            }
        };
        CraftingInventory inventory = new CraftingInventory(handler, 3, 3);
        for (int i = 0; i < inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<NewTeaRecipe> match = entity.getWorld().getRecipeManager()
                .getFirstMatch(NewTeaRecipe.Type.INSTANCE, inventory, entity.getWorld());

        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput().getItem());
    }

    private static boolean canInsertItemIntoOutputSlot(CraftingInventory inventory, Item output) {
        return inventory.getStack(numberOfSlotsInTeaKettle - 1).getItem() == output || inventory.getStack(numberOfSlotsInTeaKettle - 1).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(CraftingInventory inventory) {
        return inventory.getStack(numberOfSlotsInTeaKettle - 1).getMaxCount() > inventory.getStack(numberOfSlotsInTeaKettle - 1).getCount();
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.good_tea.tea_kettle");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new TeaKettleScreenHandler(syncId, playerInventory);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return side == Direction.DOWN ? BOTTOM_SLOTS : TOP_AND_SIDE_SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.inventory) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return slot >= 0 && slot < inventory.size() ? this.inventory.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.inventory.size()) {
            this.inventory.set(slot, stack);
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.squaredDistanceTo(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }
}
