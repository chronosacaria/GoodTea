package timefall.goodtea.registries;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import timefall.goodtea.GoodTea;
import timefall.goodtea.screens.screenhandlers.TeaKettleScreenHandler;

public class ScreenHandlersRegistry {
    public static ScreenHandlerType<TeaKettleScreenHandler> TEA_KETTLE_SCREEN_HANDLER =
            ScreenHandlerRegistry.registerSimple(new Identifier(GoodTea.MOD_ID, "tea_kettle"),
                    TeaKettleScreenHandler::new);

    public static void registerScreenHandlers() {
        GoodTea.LOGGER.info("Steeping screen handlers to make some " + GoodTea.MOD_ID + "!");
    }
}
