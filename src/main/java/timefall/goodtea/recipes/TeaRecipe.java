/*
package timefall.goodtea.recipes;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
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
import timefall.goodtea.blocks.TeaKettleBlock;
import timefall.goodtea.blocks.entities.TeaKettleBlockEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeaRecipe implements Recipe<Inventory> {
    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItems;

    public TeaRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }


    @Override
    public boolean matches(Inventory inventory, World world) {
        if (world.isClient()) {
            return false;
        }

        List<ItemStack> inputList = new ArrayList<>();
        int slotCount = 0;

        for (int slotOffset = 0; slotOffset < TeaKettleBlockEntity.numberOfSlotsInTeaKettle - 1; slotOffset++) {
            ItemStack itemStack = inventory.getStack(slotOffset);
            if (!itemStack.isEmpty()) {
                slotCount++;
                inputList.add(itemStack);
            }
        }
        return slotCount == recipeItems.size() && TeaRecipeMatcher.findMatches(inputList, recipeItems) != null;
    }

    //@Override
    //public boolean matches(SimpleInventory inventory, World world) {
    //    if (world.isClient()) {
    //        return false;
    //    }
    //    return recipeItems.get(0).test(inventory.getStack(0));
    //}

    @Override
    public ItemStack craft(Inventory inventory) {
        return output.copy();
    }


    //@Override
    //public ItemStack craft(SimpleInventory inventory) {
    //    return output;
    //}

    @Override
    public boolean fits(int width, int height) {
        return width * height >= recipeItems.size();
    }

    @Override
    public ItemStack getOutput() {
        return output;
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
            var gson = new Gson();
            var teaRecipeJSON = gson.fromJson(json, TeaRecipeJSON.class);
            var ingredients = Arrays.stream(teaRecipeJSON.ingredients).map(ingredient -> {
               var ingredientId = new Identifier(ingredient.item);
                //return Registry.ITEM.get(ingredientId).getDefaultStack();
                return Ingredient.ofItems(Registry.ITEM.get(ingredientId));
            }).toList();


//            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));
//
//            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(TeaKettleBlockEntity.numberOfSlotsInTeaKettle - 1, Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size(); i++) {
                inputs.set(i, ingredients.get(i));
            }
            ItemStack output = Registry.ITEM.get(new Identifier(teaRecipeJSON.output.item)).getDefaultStack();
            return new TeaRecipe(id, output, inputs);
        }

        @Override
        public TeaRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new TeaRecipe(id, output, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, TeaRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
        }
    }
}
*/
