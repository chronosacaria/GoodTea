package timefall.goodtea.registries;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import timefall.goodtea.GoodTea;
import timefall.goodtea.recipes.NewTeaRecipe;

public class TeaRecipeRegistry {

    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(GoodTea.MOD_ID, NewTeaRecipe.Serializer.ID),
                NewTeaRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(GoodTea.MOD_ID, NewTeaRecipe.Type.ID),
                NewTeaRecipe.Type.INSTANCE);

    }
}
