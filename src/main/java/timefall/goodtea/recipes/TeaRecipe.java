package timefall.goodtea.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public class TeaRecipe implements Recipe<Inventory> {
    private final Identifier id;
    public Ingredient result;
    public Ingredient ingredient;
    public ItemStack container;
    //private final DefaultedList<Ingredient> recipeItems;

    public TeaRecipe(Identifier id, Ingredient ingredient, ItemStack container, Ingredient result) {
        this.id = id;
        this.ingredient = ingredient;
        this.container = container;
        this.result = result;
        //this.recipeItems = recipeItems;
    }
    //@Override
    //public boolean matches(SimpleInventory inventory, World world) {
    //    if (world.isClient()) {
    //        return false;
    //    }
    //
    //    List<ItemStack> inputList = new ArrayList<>();
    //    int slotCount = 0;
    //
    //    for (int slotOffset = 0; slotOffset < TeaKettleBlockEntity.numberOfSlotsInTeaKettle - 1; slotOffset++) {
    //        ItemStack itemStack = inventory.getStack(slotOffset);
    //        if (!itemStack.isEmpty()) {
    //            slotCount++;
    //            inputList.add(itemStack);
    //        }
    //    }
    //    return slotCount == recipeItems.size() &&
    //}

    //@Override
    //public boolean matches(SimpleInventory inventory, World world) {
    //    RecipeMatcher recipeMatcher = new RecipeMatcher();
    //    if (world.isClient()) {
    //        return false;
    //    }
    //    int i = 0;
    //    for (int j = 0; j < inventory.size(); j++) {
    //        ItemStack itemStack = inventory.getStack(j);
    //        if (!itemStack.isEmpty()) {
    //            i++;
    //            recipeMatcher.addInput(itemStack, 1);
    //        }
    //    }
    //    System.out.println(recipeItems.size());
    //    return i == recipeItems.size() && recipeMatcher.match(this, null);
    //}

    //@Override
    //public ItemStack craft(SimpleInventory inventory) {
    //    return output;
    //}
    @Override
    public boolean matches(Inventory inventory, World world) {
        return this.ingredient.test(inventory.getStack(0));
    }
    public boolean check(List<ItemStack> itemStacks, ItemStack container) {
        List<ItemStack> itemStack = List.of(this.ingredient.getMatchingStacks());
        int actualSize = 0;
        for (ItemStack itemStack1 : itemStacks) {
            actualSize += itemStack1.isOf(Items.AIR) ? 0 : 1;
        }
        if (actualSize != itemStack.size()) return false;
        for (ItemStack stack : itemStack) {
            boolean doesHave = false;
            for (ItemStack stack1 : itemStacks) {
                if (stack.getItem() == stack1.getItem()) {
                    doesHave = true;
                    break;
                }
            }
            if (!doesHave) return false;
        }
        return container.getItem() == this.container.getItem();
    }
    @Override
    public ItemStack craft(Inventory inventory) {
        return ItemStack.EMPTY;
    }
    @Override
    public boolean fits(int width, int height) {
        return true;
    }
    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        defaultedList.add(this.ingredient);
        return defaultedList;
    }
    @Override
    public ItemStack getOutput() {
        return ItemStack.EMPTY;
    }
    @Override
    public Identifier getId() {
        return id;
    }
    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }
    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    public static class Type implements RecipeType<TeaRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "tea_brewing";
    }
    public static class Serializer implements RecipeSerializer<TeaRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "tea_brewing";

        public static class TeaRecipeJSON {
            public String type;
            public Ingredient[] ingredients;
            public static class Ingredient {
                public String item;
            }
            public Output output;
            public static class Output {
                public String item;
            }
        }

        @Override
        public TeaRecipe read(Identifier id, JsonObject json) {
            JsonElement ingredientJsonElement = JsonHelper.hasArray(json, "ingredient") ?
                    JsonHelper.getArray(json, "ingredient") : JsonHelper.getObject(json, "ingredient");
            Ingredient ingredient = Ingredient.fromJson(ingredientJsonElement);
            String containerString = JsonHelper.getString(json, "container");
            ItemStack containerItemStack = new ItemStack((Registry.ITEM.getOrEmpty(new Identifier(containerString)).orElseThrow(
                    () -> new IllegalStateException("Item: " + containerString + " does not exist!"))));
            JsonElement resultJsonElement = JsonHelper.hasArray(json, "result") ?
                    JsonHelper.getArray(json, "result") : JsonHelper.getObject(json, "result");
            Ingredient results = Ingredient.fromJson(resultJsonElement);
            return new TeaRecipe(id, ingredient, containerItemStack, results);
        }

        @Override
        public TeaRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient ingredient = Ingredient.fromPacket(buf);
            ItemStack itemStack = buf.readItemStack();
            Ingredient results = Ingredient.fromPacket(buf);
            return new TeaRecipe(id, ingredient, itemStack, results);
        }

        @Override
        public void write(PacketByteBuf buf, TeaRecipe recipe) {
            recipe.ingredient.write(buf);
            buf.writeItemStack(recipe.container);
            recipe.result.write(buf);
        }
    }
}
