package timefall.goodtea.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import timefall.goodtea.GoodTea;
import timefall.goodtea.screens.renderers.FluidStackRenderer;
import timefall.goodtea.screens.screenhandlers.TeaKettleScreenHandler;
import timefall.goodtea.util.FluidStack;
import timefall.goodtea.util.MouseUtil;

import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
@Environment(EnvType.CLIENT)
public class TeaKettleScreen extends HandledScreen<TeaKettleScreenHandler> {
    private static final Identifier TEXTURE =
            new Identifier(GoodTea.MOD_ID, "textures/gui/container/tea_kettle.png");
    private FluidStackRenderer fluidStackRenderer;
    public TeaKettleScreen(TeaKettleScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        assignFluidStackRenderer();
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        assignFluidStackRenderer();
    }

    private void assignFluidStackRenderer() {
        fluidStackRenderer = new FluidStackRenderer(FluidStack.convertDropletsToMb(FluidConstants.BUCKET),
                true, 15, 34);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        renderFluidTooltip(matrices, mouseX, mouseY, x, y, handler.fluidStack, 55, 15, fluidStackRenderer);
    }

    private void renderFluidTooltip(MatrixStack matrices, int mouseX, int mouseY, int x, int y,
                                    FluidStack fluidStack, int offsetX, int offsetY, FluidStackRenderer renderer) {
        if(isMouseAboveArea(mouseX, mouseY, x, y, offsetX, offsetY, renderer)) {
            renderTooltip(matrices, renderer.getTooltip(fluidStack, TooltipContext.Default.NORMAL),
                    Optional.empty(), mouseX - x, mouseY - y);
        }
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f,1.0f,1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderProgressArrow(matrices, x, y);

        fluidStackRenderer.drawFluid(matrices, handler.fluidStack, x + 83, y + 18, 16, 34,
                FluidStack.convertDropletsToMb(FluidConstants.BUCKET));
    }

    private void renderProgressArrow(MatrixStack matrices, int x, int y) {
        if (handler.isCrafting()) {
            drawTexture(matrices, x + 104 , y + 36 , 176, 0, handler.getScaledProgress(), 16);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, FluidStackRenderer renderer) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }
}
