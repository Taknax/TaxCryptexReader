package tax.taknax.taxcr.client;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tax.taknax.taxcr.TaxCR;
import tax.taknax.taxcr.api.RecipeManager;
import tax.taknax.taxcr.client.gui.GuiCryptexRecipeBook;
import tax.taknax.taxcr.client.gui.GuiEvents;
import tax.taknax.taxcr.client.gui.GuiGuideBook;
import tax.taknax.taxcr.common.proxy.ProxyClient;
import tax.taknax.taxcr.init.ItemInit;

/**
 * Created by Creysys on 25 Mar 16.
 */
public class ClientPlayerHandler
{
	
	@SuppressWarnings("unlikely-arg-type")
	public boolean playerHasBook()
	{
		InventoryPlayer inventory = Minecraft.getMinecraft().player.inventory;
		if (inventory.player.isCreative() || inventory.player.getEntityData().getBoolean("doesntNeedGuideBook"))
			return true;
		
		if (inventory.offHandInventory.contains(ItemInit.guideBook))
		{
			return true;
		}
		
		for (ItemStack stack : inventory.mainInventory)
		{
			if (stack != null && stack.getItem() == ItemInit.guideBook)
				return true;
		}
		
		return false;
	}
	
	@SubscribeEvent
	public void preKeyboardInput(GuiScreenEvent.KeyboardInputEvent.Pre event)
	{
		if (event.getGui() instanceof GuiContainer && playerHasBook())
		{
			GuiContainer gui = (GuiContainer) event.getGui();
			Slot slot = gui.getSlotUnderMouse();
			if (slot != null && slot.getHasStack())
			{
				if (Keyboard.getEventKey() == ProxyClient.recipeKey.getKeyCode() && RecipeManager.hasRecipes(slot.getStack()))
				{
					GuiGuideBook.onOpenCmd = "recipe";
					GuiGuideBook.onOpenArg = slot.getStack();
					openCryptexGui();
					event.setCanceled(true);
				}
				else if (Keyboard.getEventKey() == ProxyClient.usageKey.getKeyCode() && RecipeManager.hasUsages(slot.getStack()))
				{
					GuiGuideBook.onOpenCmd = "usage";
					GuiGuideBook.onOpenArg = slot.getStack();
					openCryptexGui();
					event.setCanceled(true);
				}
			}
		}
	}
	
	private void openCryptexGui()
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = mc.player;
		GuiCryptexRecipeBook recipeBook = GuiEvents.getRecipeBookFromGui(mc.currentScreen);
		
		if (recipeBook != null)
		{
			if (mc.currentScreen instanceof GuiCrafting)
			{
				GuiCrafting gui = (GuiCrafting) mc.currentScreen;
				
				if (!recipeBook.isVisible())
					recipeBook.toggleVisibility();

				gui.guiLeft = recipeBook.updateScreenPosition(gui.widthTooNarrow, gui.width, gui.xSize);
				recipeBook.actualGui.setWorldAndResolution(mc, gui.width, gui.height);
				recipeBook.actualGui.guiLeft = (gui.width - (GuiGuideBook.bookImageWidth + gui.getXSize())) / 2 - 7;
				recipeBook.actualGui.guiTop = (gui.height - GuiGuideBook.bookImageHeight) / 2 + 15;
				gui.recipeButton.setPosition(gui.guiLeft + 5, gui.height / 2 - 49);
			}
			
			if (mc.currentScreen instanceof GuiInventory)
			{
				GuiInventory gui = (GuiInventory) mc.currentScreen;
				
				if (!recipeBook.isVisible())
					recipeBook.toggleVisibility();

				gui.guiLeft = recipeBook.updateScreenPosition(gui.widthTooNarrow, gui.width, gui.xSize);
				gui.recipeButton.setPosition(gui.guiLeft + 104, gui.height / 2 - 22);
				gui.buttonClicked = true;
				recipeBook.actualGui.setWorldAndResolution(mc, gui.width, gui.height);
				recipeBook.actualGui.guiLeft = (gui.width - (GuiGuideBook.bookImageWidth + gui.getXSize())) / 2 - 7;
				recipeBook.actualGui.guiTop = (gui.height - GuiGuideBook.bookImageHeight) / 2 + 15;
			}
		}
		else
		{
			player.openGui(TaxCR.instance, 0, Minecraft.getMinecraft().world, 0, 0, 0);
		}
	}
}
