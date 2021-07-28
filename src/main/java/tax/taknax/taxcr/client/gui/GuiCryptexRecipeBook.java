package tax.taknax.taxcr.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;

public class GuiCryptexRecipeBook extends GuiRecipeBook
{
	public GuiGuideBook actualGui = new GuiGuideBook();
	private boolean visible;
	
	public GuiCryptexRecipeBook()
	{
		actualGui.insideAnotherGui = true;
		actualGui.mc = Minecraft.getMinecraft();
	}
	
	@Override
	public void initVisuals(boolean p_193014_1_, InventoryCrafting p_193014_2_)
	{
		if (visible)
			actualGui.initGui();
	}
	
	@Override
	public void tick()
	{
		if (visible)
			actualGui.updateScreen();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		if (visible)
			actualGui.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton)
	{
		if (visible)
		{
			try
			{
				actualGui.mouseClicked(mouseX, mouseY, mouseButton);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	@Override
	public boolean keyPressed(char typedChar, int keycode)
	{
		if (visible)
			actualGui.keyTyped(typedChar, keycode);
		
		return false;
	}
	
	@Override
	public boolean isVisible()
	{
		return visible;
	}
	
	@Override
	public void toggleVisibility()
	{
		visible ^= true;
	}
	
	@Override
	public boolean hasClickedOutside(int mouseX, int mouseY, int guiLeft, int guiTop, int guiWidth, int guiHeight)
	{
		if (!visible)
		{
			return true;
		}
		else
		{
			return mouseX < actualGui.guiLeft || mouseX > actualGui.guiLeft + GuiGuideBook.bookImageWidth + 10 || mouseY < actualGui.guiTop
					|| mouseY > actualGui.guiTop + GuiGuideBook.bookImageHeight;
		}
	}
	
	@Override
	public void renderTooltip(int p_191876_1_, int p_191876_2_, int mouseX, int mouseY)
	{
		if (visible)
			actualGui.drawForeground(mouseX, mouseY);
	}
	
	@Override
	public void slotClicked(Slot slotIn)
	{
	}
}
