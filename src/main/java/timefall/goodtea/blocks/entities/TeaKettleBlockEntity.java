package timefall.goodtea.blocks.entities;


import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import timefall.goodtea.enums.TeaKettleSlots;
import timefall.goodtea.recipes.TeaRecipe;
import timefall.goodtea.registries.BlockEntitiesRegistry;
import timefall.goodtea.registries.ItemsRegistry;
import timefall.goodtea.registries.PacketsRegistry;
import timefall.goodtea.screens.screenhandlers.TeaKettleScreenHandler;
import timefall.goodtea.util.FluidStack;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class TeaKettleBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, IImplementedInventory {
    public static int numberOfSlotsInTeaKettle = 11;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(numberOfSlotsInTeaKettle, ItemStack.EMPTY);
    private Ingredient latestIngredient;
    private ItemStack latestContainer;
    private Ingredient latestResult;
    private int progress = 0;
    private int maxProgress = 72;

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }


    protected final PropertyDelegate propertyDelegate = new PropertyDelegate() {
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

    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>()
    {

        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidConstants.BUCKET;
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
            if (!world.isClient()) {
                sendFluidPacket();
            }
        }
    };

    public void setInventory(DefaultedList<ItemStack> inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            this.inventory.set(i, inventory.get(i));
        }
    }

    public void setFluidLevel(FluidVariant fluidVariant, long fluidLevel) {
        this.fluidStorage.variant = fluidVariant;
        this.fluidStorage.amount = fluidLevel;
    }

    public TeaKettleBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntitiesRegistry.TEA_KETTLE_BLOCK_ENTITY, pos, state);
        this.latestIngredient = this.latestResult = Ingredient.EMPTY;
        this.latestContainer = ItemStack.EMPTY;
    }

    public DefaultedList<ItemStack> getIngredient() {
        DefaultedList<ItemStack> ingredients = DefaultedList.ofSize(9, ItemStack.EMPTY);
        for (int i = 0; i < 9; i++) {
            ingredients.set(i, this.inventory.get(i));
        }
        return ingredients;
    }

    public void removeIngredients() {
        int i;
        for (i = 0; i < 9; i++) {
            ItemStack stack = this.inventory.get(i);
            if (stack.isEmpty() || stack.getCount() <= 0) continue;
            ItemStack remainder = stack.getItem().hasRecipeRemainder() ? new ItemStack(stack.getItem().getRecipeRemainder()) : ItemStack.EMPTY;
            if (stack.getMaxCount() == 1) {
                this.inventory.set(i, remainder);
            } else {
                stack.decrement(1);
                boolean isSuccessful = false;
                for (int j = 0; j < 9 && !remainder.isEmpty(); j++) {
                    if (this.inventory.get(j).isEmpty()) {
                        this.inventory.set(j, remainder);
                        isSuccessful = true;
                        break;
                    }
                }
                if (!isSuccessful && this.world != null) {
                    ItemScatterer.spawn(this.world, this.pos.getX() + 1, this.pos.getY(), this.pos.getZ(), remainder);
                }
            }
        }
        this.inventory.get(TeaKettleSlots.CONTAINER.ordinal()).setCount(Math.max(0, this.inventory.get(TeaKettleSlots.CONTAINER.ordinal()).getCount() - 1));
    }

    public DefaultedList<ItemStack> getResults() {
        DefaultedList<ItemStack> ingredients = DefaultedList.ofSize(1, ItemStack.EMPTY);
        ingredients.set(0, this.inventory.get(TeaKettleSlots.RESULT.ordinal()));
        return ingredients;
    }

    public void insertResults(Ingredient result) {
        List<ItemStack> itemStacks = List.of(result.getMatchingStacks());
        for (int i = 0; i < 1 && i < itemStacks.size(); i++) {
            int j = i + TeaKettleSlots.RESULT.ordinal();
            this.inventory.set(j, itemStacks.get(i).copy());
        }
    }

    public ItemStack getContainer() {
        return this.inventory.get(TeaKettleSlots.CONTAINER.ordinal());
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.goodtea.tea_kettle");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        sendFluidPacket();
        return new TeaKettleScreenHandler(
                syncId,
                playerInventory,
                this,
                this.propertyDelegate);
    }

    private void sendFluidPacket() {
        PacketByteBuf data = PacketByteBufs.create();
        fluidStorage.variant.toPacket(data);
        data.writeLong(fluidStorage.amount);
        data.writeBlockPos(getPos());

        for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) world, getPos())) {
            ServerPlayNetworking.send(player, PacketsRegistry.FLUID_SYNC, data);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("tea_kettle.progress", progress);
        nbt.put("tea_kettle.variant", fluidStorage.variant.toNbt());
        nbt.putLong("tea_kettle.fluid", fluidStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("tea_kettle.progress");
        fluidStorage.variant = FluidVariant.fromNbt((NbtCompound) nbt.get("tea_kettle.variant"));
        fluidStorage.amount = nbt.getLong("tea_kettle.fluid");
    }

    private void resetProgress() {
        this.progress = 0;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, TeaKettleBlockEntity entity) {
        if (entity != null /*&& entity.isOnLitObject()*/) {
            if (entity.progress == 0) {
                matchRecipe(world, entity);
                entity.progress++;
                markDirty(world, blockPos, blockState);
            }
            if (entity.getContainer().isOf(ItemsRegistry.TEA_CUP)
                    && entity.getContainer().isOf(entity.latestContainer.getItem())
                    && entity.getResults().get(0).isOf(Items.AIR)
                    && entity.compareList(List.of(entity.latestIngredient.getMatchingStacks()), entity.getIngredient())
                    && entity.isOnLitObject()
                    && entity.hasEnoughWater()) {
                matchRecipe(world, entity);
                entity.progress++;
                if (entity.progress >= entity.maxProgress) {
                    entity.insertResults(entity.latestResult);
                    entity.removeIngredients();
                    extractFluid(entity);
                    markDirty(world, blockPos, blockState);
                }
            } else {
                entity.resetProgress();
                markDirty(world, blockPos, blockState);
            }
        }
    }

    private static void extractFluid(TeaKettleBlockEntity entity) {
        try(Transaction transaction = Transaction.openOuter()) {
            entity.fluidStorage.extract(FluidVariant.of(Fluids.WATER),
                    100, transaction);
            transaction.commit();
        }
    }

    public static void transferFluidToFluidStorage(TeaKettleBlockEntity entity) {
        if (entity.fluidStorage.amount != FluidStack.convertDropletsToMb(FluidConstants.BUCKET)) {
            try (Transaction transaction = Transaction.openOuter()) {
                entity.fluidStorage.insert(
                        FluidVariant.of(Fluids.WATER),
                        FluidStack.convertDropletsToMb(FluidConstants.BUCKET) - entity.fluidStorage.amount,
                        transaction);
                transaction.commit();
                entity.setStack(TeaKettleSlots.WATER_CONTAINER.ordinal(), new ItemStack(Items.BUCKET));
            }
        }
    }

    private static boolean hasFluidSourceInSlot(TeaKettleBlockEntity entity) {
        return entity.getStack(TeaKettleSlots.WATER_CONTAINER.ordinal()).isOf(Items.WATER_BUCKET);
    }

    public static void matchRecipe(World world, TeaKettleBlockEntity teaKettleBlockEntity) {
        List<TeaRecipe> recipes = world.getRecipeManager().listAllOfType(TeaRecipe.Type.INSTANCE);
        for (TeaRecipe recipe : recipes) {
            if (recipe.check(teaKettleBlockEntity.getIngredient(), teaKettleBlockEntity.getContainer())) {
                teaKettleBlockEntity.latestIngredient = recipe.ingredient;
                teaKettleBlockEntity.latestContainer = recipe.container.copy();
                teaKettleBlockEntity.latestResult = recipe.result;
                teaKettleBlockEntity.markDirty();
                break;
            }
        }
    }

    private static void craftItem(TeaKettleBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        ItemStack itemStack = ItemStack.EMPTY;

        Optional<TeaRecipe> match = entity.getWorld().getRecipeManager()
                .getFirstMatch(TeaRecipe.Type.INSTANCE, inventory, entity.getWorld());
        if (match.isPresent()) {
            TeaRecipe teaRecipe = match.get();
            itemStack = teaRecipe.craft(inventory);
        }

        if (!itemStack.isEmpty() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, itemStack.getItem())) {
            entity.removeStack(0,1);
            entity.setStack(TeaKettleSlots.RESULT.ordinal(), new ItemStack(itemStack.getItem(),
                    entity.getStack(TeaKettleSlots.RESULT.ordinal()).getCount() + 1));
            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(TeaKettleBlockEntity entity) {
        SimpleInventory inventory = new SimpleInventory(entity.size());
        for (int i = 0; i < entity.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        ItemStack itemStack = ItemStack.EMPTY;

        Optional<TeaRecipe> match = entity.getWorld().getRecipeManager()
                .getFirstMatch(TeaRecipe.Type.INSTANCE, inventory, entity.getWorld());
        if (match.isPresent()) {
            TeaRecipe teaRecipe = match.get();
            itemStack = teaRecipe.craft(inventory);
        }

        return !itemStack.isEmpty() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, itemStack.getItem());
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, Item output) {
        return inventory.getStack(TeaKettleSlots.RESULT.ordinal()).getItem()
                == output || inventory.getStack(TeaKettleSlots.RESULT.ordinal()).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(TeaKettleSlots.RESULT.ordinal()).getMaxCount()
                > inventory.getStack(TeaKettleSlots.RESULT.ordinal()).getCount();
    }

    public boolean isOnLitObject() {
        return this.world != null &&
                (this.world.getBlockState(this.pos.down()).isOf(Blocks.FURNACE)
                        && this.world.getBlockState(this.pos.down()).get(Properties.LIT));
    }

    public boolean hasEnoughWater() {
        return this.world != null && this.fluidStorage.amount >= 100;
    }

    public boolean compareList(List<ItemStack> itemStacks1, List<ItemStack> itemStacks2) {
        int actualSize = 0;
        for (ItemStack stack1 : itemStacks2) {
            actualSize += stack1.isOf(Items.AIR) ? 0 : 1;
        }
        if (actualSize != itemStacks1.size()) return false;
        for (ItemStack stack : itemStacks1) {
            boolean doesHave = false;
            for (ItemStack stack1 : itemStacks2) {
                if (stack.getItem() == stack1.getItem()) {
                    doesHave = true;
                    break;
                }
            }
            if (!doesHave) return false;
        }
        return true;
    }



    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.UP) {
            return TeaKettleSlots.UP_SLOTS;
        } else if (side == Direction.DOWN) {
            return TeaKettleSlots.DOWN_SLOTS;
        }
        return TeaKettleSlots.SIDE_SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }
}
