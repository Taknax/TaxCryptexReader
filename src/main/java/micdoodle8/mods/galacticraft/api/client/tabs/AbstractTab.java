// 
// Decompiled by Procyon v0.5.36
// 

package micdoodle8.mods.galacticraft.api.client.tabs;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiButton;

public abstract class AbstractTab extends GuiButton
{
    ResourceLocation texture;
    ItemStack renderStack;
    public int potionOffsetLast;
    protected RenderItem itemRender;
    
    public AbstractTab(final int id, final int posX, final int posY, final ItemStack renderStack) {
        super(id, posX, posY, 28, 32, "");
        this.texture = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
        this.renderStack = renderStack;
        this.itemRender = FMLClientHandler.instance().getClient().getRenderItem();
    }
    
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
        final int newPotionOffset = TabRegistry.getPotionOffsetNEI();
        if (newPotionOffset != this.potionOffsetLast) {
            this.x += newPotionOffset - this.potionOffsetLast;
            this.potionOffsetLast = newPotionOffset;
        }
        if (this.visible) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final int yTexPos = this.enabled ? 3 : 32;
            final int ySize = this.enabled ? 25 : 32;
            final int xOffset = (this.id != 2) ? 1 : 0;
            final int yPos = this.y + (this.enabled ? 3 : 0);
            mc.renderEngine.bindTexture(this.texture);
            this.drawTexturedModalRect(this.x, yPos, xOffset * 28, yTexPos, 28, ySize);
            RenderHelper.enableGUIStandardItemLighting();
            this.zLevel = 100.0f;
            this.itemRender.zLevel = 100.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableRescaleNormal();
            this.itemRender.renderItemAndEffectIntoGUI(this.renderStack, this.x + 6, this.y + 8);
            this.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, this.renderStack, this.x + 6, this.y + 8, (String)null);
            GlStateManager.disableLighting();
            this.itemRender.zLevel = 0.0f;
            this.zLevel = 0.0f;
            RenderHelper.disableStandardItemLighting();
        }
    }
    
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        final boolean inWindow = this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        if (inWindow) {
            this.onTabClicked();
        }
        return inWindow;
    }
    
    public abstract void onTabClicked();
    
    public abstract boolean shouldAddToList();
}
