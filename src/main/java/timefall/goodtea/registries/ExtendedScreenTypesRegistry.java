/* The credit for this code goes to ianm1647 of farmersdelight and the original can be found below:
 * https://github.com/ianm1647/farmersdelight/blob/master/src/main/java/com/nhoryzon/mc/farmersdelight/registry/ExtendedScreenTypesRegistry.java
 * This piece of code is under the MIT License:
 * MIT License
 *
 * Copyright (c) 2020 vectorwing, Zifiv
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package timefall.goodtea.registries;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import timefall.goodtea.GoodTea;
import timefall.goodtea.screens.screenhandlers.TeaKettleScreenHandler;

public enum ExtendedScreenTypesRegistry {
    TEA_KETTLE("tea_kettle", TeaKettleScreenHandler.class, TeaKettleScreenHandler::new);

    private final String pathName;
    private final Class<? extends ScreenHandler> screenHandlerClass;
    @SuppressWarnings("deprecation")
    private final ScreenHandlerRegistry.ExtendedClientHandlerFactory<? extends ScreenHandler> screenHandlerFactory;
    private ScreenHandlerType<? extends ScreenHandler> screenHandlerType;

    @SuppressWarnings("deprecation")
    ExtendedScreenTypesRegistry(String pathName, Class<? extends ScreenHandler> screenHandlerClass,
                                ScreenHandlerRegistry.ExtendedClientHandlerFactory<? extends ScreenHandler> screenHandlerFactory) {
        this.pathName = pathName;
        this.screenHandlerClass = screenHandlerClass;
        this.screenHandlerFactory = screenHandlerFactory;
    }

    public static void registerAll() {
        for (ExtendedScreenTypesRegistry value : values()) {
            Registry.register(Registry.SCREEN_HANDLER, new Identifier(GoodTea.MOD_ID, value.pathName), value.get());
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends ScreenHandler> ScreenHandlerType<T> get() {
        return (ScreenHandlerType<T>) get(screenHandlerClass);
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    private <T extends ScreenHandler> ScreenHandlerType<T> get(Class<T> clazz) {
        if (screenHandlerType == null) {
            screenHandlerType = new ExtendedScreenHandlerType<>((ScreenHandlerRegistry.ExtendedClientHandlerFactory<T>) screenHandlerFactory);
        }

        return (ScreenHandlerType<T>) screenHandlerType;
    }
}
