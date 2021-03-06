package tax.taknax.taxcr.plugin.vanilla.recipe;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import tax.taknax.taxcr.api.DrawableRecipe;
import tax.taknax.taxcr.api.IGuiAccessor;

/**
 * Created by Creysys on 26 Mar 16.
 */
public class DrawableRecipeBrewing extends DrawableRecipe
{
	
	public static final ResourceLocation brewingGridTexture = new ResourceLocation("taxcr", "textures/gui/brewingGrid.png");
	public static final ItemStack blazeRod = new ItemStack(Items.BLAZE_POWDER);
	
	public ItemStack input;
	public ItemStack ingredient;
	public ItemStack output;
	
	public DrawableRecipeBrewing(ItemStack input, ItemStack ingredient, ItemStack output)
	{
		this.input = input.copy();
		this.ingredient = ingredient.copy();
		this.output = output.copy();
	}
	
	@Override
	public ItemStack[] getInput()
	{
		return new ItemStack[] { input, ingredient, blazeRod };
	}
	
	@Override
	public ItemStack getOutput()
	{
		return output;
	}
	
	@Override
	public void draw(IGuiAccessor gui, int pageRecipeIndex, int mouseX, int mouseY)
	{
		if (pageRecipeIndex == 0)
			drawRecipe(gui, gui.getLeft() + 41, gui.getTop() + 14);
		else if (pageRecipeIndex == 1)
			drawRecipe(gui, gui.getLeft() + 41, gui.getTop() + 94);
	}
	
	@Override
	public void drawForeground(IGuiAccessor gui, int pageRecipeIndex, int mouseX, int mouseY)
	{
		if (pageRecipeIndex == 0)
			drawRecipeTooltip(gui, gui.getLeft() + 41, gui.getTop() + 14, mouseX, mouseY);
		else if (pageRecipeIndex == 1)
			drawRecipeTooltip(gui, gui.getLeft() + 41, gui.getTop() + 94, mouseX, mouseY);
	}
	
	@Override
	public void mouseClick(IGuiAccessor gui, int pageRecipeIndex, int mouseX, int mouseY, int mouseButton)
	{
		if (pageRecipeIndex == 0)
			clickRecipe(gui, gui.getLeft() + 41, gui.getTop() + 14, mouseX, mouseY, mouseButton);
		else if (pageRecipeIndex == 1)
			clickRecipe(gui, gui.getLeft() + 41, gui.getTop() + 94, mouseX, mouseY, mouseButton);
	}
	
	public void drawRecipe(IGuiAccessor gui, int left, int top)
	{
		gui.getMc().getTextureManager().bindTexture(brewingGridTexture);
		RenderHelper.disableStandardItemLighting();
		Gui.drawModalRectWithCustomSizedTexture(left, top, 0, 0, 107, 60, 130, 60);
		
		int j = ticks / 4 % 7;
		if (j == 0)
			Gui.drawModalRectWithCustomSizedTexture(left + 19, top - 6 + 27, 108, 27 - 5 + 27, 11, 6, 130, 60);
		else if (j == 1)
			Gui.drawModalRectWithCustomSizedTexture(left + 19, top - 11 + 27, 108, 27 - 10 + 27, 11, 11, 130, 60);
		else if (j == 2)
			Gui.drawModalRectWithCustomSizedTexture(left + 19, top - 16 + 27, 108, 27 - 15 + 27, 11, 16, 130, 60);
		else if (j == 3)
			Gui.drawModalRectWithCustomSizedTexture(left + 19, top - 20 + 27, 108, 27 - 19 + 27, 11, 20, 130, 60);
		else if (j == 4)
			Gui.drawModalRectWithCustomSizedTexture(left + 19, top - 24 + 27, 108, 27 - 23 + 27, 11, 24, 130, 60);
		else if (j == 5)
			Gui.drawModalRectWithCustomSizedTexture(left + 19, top - 28 + 27, 108, 27 - 27 + 27, 11, 28, 130, 60);
		
		j = ticks / 6 % 23;
		Gui.drawModalRectWithCustomSizedTexture(left + 55, top + 8, 108, 6, j, 16, 130, 60);
		
		drawItemStack(gui, input, left + 12, top + 36, false);
		drawItemStack(gui, input, left + 35, top + 43, false);
		drawItemStack(gui, input, left + 58, top + 36, false);
		
		drawItemStack(gui, ingredient, left + 35, top + 2, false);
		
		drawItemStack(gui, output, left + 86, top + 8, true);
		
		drawItemStack(gui, blazeRod, left + 1, top + 1, false);
	}
	
	public void drawRecipeTooltip(IGuiAccessor gui, int left, int top, int mouseX, int mouseY)
	{
		drawItemStackTooltip(gui, input, left + 12, top + 36, mouseX, mouseY);
		drawItemStackTooltip(gui, input, left + 35, top + 43, mouseX, mouseY);
		drawItemStackTooltip(gui, input, left + 58, top + 36, mouseX, mouseY);
		
		drawItemStackTooltip(gui, ingredient, left + 35, top + 2, mouseX, mouseY);
		
		drawItemStackTooltip(gui, output, left + 86, top + 8, mouseX, mouseY);
		
		drawItemStackTooltip(gui, blazeRod, left + 1, top + 1, mouseX, mouseY);
	}
	
	public void clickRecipe(IGuiAccessor gui, int left, int top, int mouseX, int mouseY, int mouseButton)
	{
		clickItemStack(gui, input, left + 12, top + 36, mouseX, mouseY, mouseButton);
		clickItemStack(gui, input, left + 35, top + 43, mouseX, mouseY, mouseButton);
		clickItemStack(gui, input, left + 58, top + 36, mouseX, mouseY, mouseButton);
		
		clickItemStack(gui, ingredient, left + 35, top + 2, mouseX, mouseY, mouseButton);
		
		clickItemStack(gui, output, left + 86, top + 8, mouseX, mouseY, mouseButton);
		
		clickItemStack(gui, blazeRod, left + 1, top + 1, mouseX, mouseY, mouseButton);
	}
}
