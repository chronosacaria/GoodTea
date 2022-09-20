package timefall.goodtea.blocks.entities;


import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import timefall.goodtea.registries.BlockEntitiesRegistry;
import timefall.goodtea.registries.ItemsRegistry;
import timefall.goodtea.registries.TeaRecipeRegistry;
import timefall.goodtea.screens.screenhandlers.TeaKettleScreenHandler;

public class TeaKettleBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, IImplementedInventory {
    public static int numberOfSlotsInTeaKettle = 10;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(numberOfSlotsInTeaKettle, ItemStack.EMPTY);
    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;

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

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.goodtea.tea_kettle");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new TeaKettleScreenHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("tea_kettle.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("tea_kettle.progress");
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, TeaKettleBlockEntity entity) {
        if (world.isClient()) return;
        if (hasRecipe(entity)) {
            entity.progress++;
            markDirty(world, blockPos, blockState);
            if (entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            markDirty(world, blockPos, blockState);
        }
    }

    private static void craftItem(TeaKettleBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        if (hasRecipe(entity)) {
            entity.removeStack(0,1);
            entity.setStack(numberOfSlotsInTeaKettle - 1, new ItemStack(ItemsRegistry.SPEARMINT_TEA,
                    entity.getStack(numberOfSlotsInTeaKettle - 1).getCount() + 1));
            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(TeaKettleBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        boolean hasTeaIngredientsInCraftingSlots = entity.getStack(0).getItem() == ItemsRegistry.SPEARMINT_LEAF;

        return hasTeaIngredientsInCraftingSlots && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, ItemsRegistry.SPEARMINT_TEA);
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, Item output) {
        return inventory.getStack(numberOfSlotsInTeaKettle - 1).getItem() == output || inventory.getStack(numberOfSlotsInTeaKettle - 1).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(numberOfSlotsInTeaKettle - 1).getMaxCount() > inventory.getStack(numberOfSlotsInTeaKettle - 1).getCount();
    }

    private static boolean hasTeaIngredientsInCraftingSlots(DefaultedList<ItemStack> slots) {
        ItemStack itemStack = slots.get(9);
        if (itemStack.isEmpty())
            return false;
        else if (!TeaRecipeRegistry.isValidIngredient(itemStack))
            return false;
        else {
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack2 = slots.get(i);
                if (!itemStack2.isEmpty() && TeaRecipeRegistry.hasRecipe(itemStack2, itemStack)) {
                    return true;
                }
            }
        }
        return false;
    }
}
