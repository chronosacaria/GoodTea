package timefall.goodtea.registries;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import timefall.goodtea.GoodTea;
import timefall.goodtea.screens.screenhandlers.TeaKettleScreenHandler;

public class ScreenHandlersRegistry {
    public static ScreenHandlerType<TeaKettleScreenHandler> TEA_KETTLE_SCREEN_HANDLER;

    //public static final ScreenHandlerType<TeaKettleScreenHandler> TEA_KETTLE_SCREEN_HANDLER = registerScreenHandler(
    //        "tea_kettle_screen_handler", new)

    //private static ScreenHandlerType<?> registerScreenHandler(String name, ScreenHandlerType<?> screenHandler) {
    //    return Registry.register(Registry.SCREEN_HANDLER, new Identifier(GoodTea.MOD_ID, name), screenHandler);
    //}

    public static void registerScreenHandlers() {
        TEA_KETTLE_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(TeaKettleScreenHandler::new);
        Registry.register(Registry.SCREEN_HANDLER, new Identifier(GoodTea.MOD_ID, "tea_kettle"), TEA_KETTLE_SCREEN_HANDLER);
    }
}
