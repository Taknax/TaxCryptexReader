package tax.taknax.taxcr.client.gui;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import tax.taknax.taxcr.GuideBookMod;
import tax.taknax.taxcr.common.handler.ConfigBuildHandler;
import tax.taknax.taxcr.common.util.EnumBookDesign;

public class GuiButtonBook extends GuiButtonImage {

    private static final ResourceLocation BOOK_BUTTON = new ResourceLocation(GuideBookMod.MODID, "textures/gui/recipe_button.png");

    private float animationTicks;
    private boolean bookVisible;

    public GuiButtonBook(int buttonId, int posX, int posY) {

        super(buttonId, posX, posY, 20, 18, 0, 0, 18, BOOK_BUTTON);

    }

    /**
     * Move button when recipe book is opened or closed, called by current gui container
     */
    @Override
    public void setPosition(int posX, int posY) {

        this.x = posX + 1;
        this.y = posY;

        if (!this.bookVisible) {
            this.animationTicks = 0.0F;
        }

    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {

        if (this.visible) {

            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            this.bookVisible = mc.player.getRecipeBook().isGuiOpen();

            EnumBookDesign design = ConfigBuildHandler.bookDesign;
            mc.getTextureManager().bindTexture(BOOK_BUTTON);
            GlStateManager.disableDepth();

            int posX = 0;
            int posY = design.getId() * 2 * this.height;

            if (this.hovered) {
                this.animationTicks = Math.min(design.getFrames() - 1.0F, this.animationTicks + partialTicks * design.getSpeed());
            } else {
                this.animationTicks = Math.max(0.0F, this.animationTicks - partialTicks * design.getSpeed());
            }

            if (this.bookVisible) {
                posY += this.height;
            }

            posX += Math.round(this.animationTicks) * this.width;
            this.drawTexturedModalRect(this.x, this.y, posX, posY, this.width, this.height);
            GlStateManager.enableDepth();

        }

    }

}