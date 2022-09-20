package timefall.goodtea.registries;

import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class TeaRecipeRegistry {
    private static final List<Recipe<Potion>> TEA_RECIPES = Lists.newArrayList();
    private static final List<Recipe<Item>> ITEM_RECIPES = Lists.newArrayList();
    private static final List<Ingredient> TEA_TYPES = Lists.newArrayList();
    private static final Predicate<ItemStack> TEA_TYPE_PREDICATE = (itemStack) -> {
        Iterator<Ingredient> var1 = TEA_TYPES.iterator();
        Ingredient ingredient;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            ingredient = (Ingredient)var1.next();
        } while (!ingredient.test(itemStack));

        return true;
    };

    public TeaRecipeRegistry() {
    }

    public static boolean isValidIngredient(ItemStack itemStack) {
        return isItemRecipeIngredient(itemStack) || isTeaRecipeIngredient(itemStack);
    }

    private static boolean isItemRecipeIngredient(ItemStack itemStack) {
        int i = 0;

        for (int j = ITEM_RECIPES.size(); i < j; ++i) {
            if ((ITEM_RECIPES.get(i)).ingredient.test(itemStack)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isTeaRecipeIngredient(ItemStack itemStack) {
        int i = 0;

        for (int j = TEA_RECIPES.size(); i < j; ++i) {
            if (TEA_RECIPES.get(i).ingredient.test(itemStack)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isBrewable(Potion tea) {
        int i = 0;

        for(int j = TEA_RECIPES.size(); i < j; ++i) {
            if (TEA_RECIPES.get(i).output == tea) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasRecipe(ItemStack input, ItemStack ingredient) {
        if (!TEA_TYPE_PREDICATE.test(input)) {
            return false;
        } else {
            return hasItemRecipe(input, ingredient) || hasTeaRecipe(input, ingredient);
        }
    }

    protected static boolean hasItemRecipe(ItemStack input, ItemStack ingredient) {
        Item item = input.getItem();
        int i = 0;

        for(int j = ITEM_RECIPES.size(); i < j; ++i) {
            Recipe<Item> recipe = ITEM_RECIPES.get(i);
            if (recipe.input == item && recipe.ingredient.test(ingredient)) {
                return true;
            }
        }

        return false;
    }

    protected static boolean hasTeaRecipe(ItemStack input, ItemStack ingredient) {
        Potion potion = PotionUtil.getPotion(input);
        int i = 0;

        for(int j = TEA_RECIPES.size(); i < j; ++i) {
            Recipe<Potion> recipe = TEA_RECIPES.get(i);
            if (recipe.input == potion && recipe.ingredient.test(ingredient)) {
                return true;
            }
        }

        return false;
    }

    public static ItemStack craft(ItemStack ingredient, ItemStack input) {
        if (!input.isEmpty()) {
            Potion tea = PotionUtil.getPotion(input);
            Item item = input.getItem();
            int i = 0;

            int j;
            Recipe recipe;
            for(j = ITEM_RECIPES.size(); i < j; ++i) {
                recipe = ITEM_RECIPES.get(i);
                if (recipe.input == item && recipe.ingredient.test(ingredient)) {
                    return PotionUtil.setPotion(new ItemStack((ItemConvertible)recipe.output), tea);
                }
            }

            i = 0;

            for(j = TEA_RECIPES.size(); i < j; ++i) {
                recipe = TEA_RECIPES.get(i);
                if (recipe.input == tea && recipe.ingredient.test(ingredient)) {
                    return PotionUtil.setPotion(new ItemStack(item), (Potion)recipe.output);
                }
            }
        }

        return input;
    }

    public static void registerItemRecipe(Item input, Item ingredient, Item output) {
        if (!(input instanceof PotionItem)) {
            throw new IllegalArgumentException("Expected a potion, got: " + Registry.ITEM.getId(input));
        } else if (!(output instanceof PotionItem)) {
            throw new IllegalArgumentException("Expected a potion, got: " + Registry.ITEM.getId(output));
        } else {
            ITEM_RECIPES.add(new Recipe<>(input, Ingredient.ofItems(ingredient), output));
        }
    }

    public static void registerPotionType(Item item) {
        if (!(item instanceof PotionItem)) {
            throw new IllegalArgumentException("Expected a potion, got: " + Registry.ITEM.getId(item));
        } else {
            TEA_TYPES.add(Ingredient.ofItems(item));
        }
    }

    public static void registerPotionRecipe(Potion input, Item item, Potion output) {
        TEA_RECIPES.add(new Recipe<>(input, Ingredient.ofItems(item), output));
    }

    static class Recipe<T> {
        final T input;
        final Ingredient ingredient;
        final T output;

        public Recipe(T input, Ingredient ingredient, T output) {
            this.input = input;
            this.ingredient = ingredient;
            this.output = output;
        }
    }
}
