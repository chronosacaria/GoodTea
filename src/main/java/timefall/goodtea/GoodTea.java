package timefall.goodtea;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import timefall.goodtea.registries.*;

public class GoodTea implements ModInitializer {

    public static final String MOD_ID = "goodtea";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static Identifier ID(String path) {
        return new Identifier(MOD_ID, path);
    }

    public static final ItemGroup GOOD_TEA = FabricItemGroupBuilder.build(
            GoodTea.ID("good_tea"),
            () -> new ItemStack(ItemsRegistry.SPEARMINT_SEEDS));


    @Override
    public void onInitialize() {
        ItemsRegistry.registerItems();
        BlocksRegistry.registerBlocks();
        BlockEntitiesRegistry.registerBlockEntities();
        ScreenHandlersRegistry.registerScreenHandlers();
        TeaRecipeRegistry.registerRecipes();
        PacketsRegistry.registerS2CPackets();
    }
}
