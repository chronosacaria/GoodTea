package timefall.goodtea.recipes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import timefall.goodtea.blocks.entities.TeaKettleBlockEntity;

import java.util.Arrays;

public class NewTeaRecipe extends ShapelessRecipe {
    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> input;

    public NewTeaRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input) {
        super(id, group, output, input);
        this.id = id;
        this.output = output;
        this.input = input;
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
        if (world.isClient()) {
            return false;
        }
        RecipeMatcher recipeMatcher = new RecipeMatcher();
        int i = 0;

        for(int j = 0; j < craftingInventory.size(); j++) {
            ItemStack itemStack = craftingInventory.getStack(j);
            if (!itemStack.isEmpty()) {
                i++;
                recipeMatcher.addInput(itemStack, itemStack.isOf(Items.WATER_BUCKET) ? 1 : 3);
            }
        }

        return i == this.input.size() && recipeMatcher.match(this, null);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<NewTeaRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "tea_brewing";
    }

    public static class Serializer implements RecipeSerializer<NewTeaRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "tea_brewing";

        public static class NewTeaRecipeJSON {
            public String type;
            public NewTeaRecipe.Serializer.NewTeaRecipeJSON.Ingredient[] ingredients;
            public static class Ingredient {
                public String item;
            }
            public NewTeaRecipe.Serializer.NewTeaRecipeJSON.Output output;
            public static class Output {
                public String item;
            }
        }

        @Override
        public NewTeaRecipe read(Identifier id, JsonObject json) {
            var gson = new Gson();
            var teaRecipeJSON = gson.fromJson(json, NewTeaRecipe.Serializer.NewTeaRecipeJSON.class);
            var ingredients = Arrays.stream(teaRecipeJSON.ingredients).map(ingredient -> {
                var ingredientId = new Identifier(ingredient.item);
                return Ingredient.ofItems(Registry.ITEM.get(ingredientId));
            }).toList();
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(TeaKettleBlockEntity.numberOfSlotsInTeaKettle - 1, Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size(); i++) {
                inputs.set(i, ingredients.get(i));
            }
            ItemStack output = Registry.ITEM.get(new Identifier(teaRecipeJSON.output.item)).getDefaultStack();
            return new NewTeaRecipe(id, "tea", output, inputs);
        }

        @Override
        public NewTeaRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            String string = buf.readString();
            return new NewTeaRecipe(id, string, output, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, NewTeaRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
            buf.writeString(recipe.getGroup());
        }
    }
}
