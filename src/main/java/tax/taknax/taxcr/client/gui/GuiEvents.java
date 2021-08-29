package tax.taknax.taxcr.client.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import micdoodle8.mods.galacticraft.api.client.tabs.AbstractTab;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tax.taknax.taxcr.ModItems;

@SideOnly(Side.CLIENT)
@EventBusSubscriber
public class GuiEvents
{
	private static Map<Integer, Integer> inventoryButtonsDefaultXPositions;
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event)
	{
		hideBookButton(event.getGui());
	}
	
	@SubscribeEvent
	public static void onPreGuiRender(GuiScreenEvent.DrawScreenEvent.Pre event)
	{
		hideBookButton(event.getGui());
		replaceRecipeBook(event.getGui());
		
		if (event.getGui() instanceof GuiInventory)
		{
			GuiCryptexRecipeBook recipeBook = getRecipeBookFromGui(event.getGui());
			GuiInventory inventoryGui = (GuiInventory) event.getGui();
			
			if (inventoryButtonsDefaultXPositions == null)
			{
				inventoryButtonsDefaultXPositions = new HashMap<>();
				
				for (int i = 0; i < inventoryGui.buttonList.size(); i++)
				{
					inventoryButtonsDefaultXPositions.put(i, inventoryGui.buttonList.get(i).x - inventoryGui.guiLeft);
				}
			}
			
			int openedRecipesGuiLeft = recipeBook.updateScreenPosition(inventoryGui.widthTooNarrow, inventoryGui.width, inventoryGui.xSize);
			
			for (int i = 0; i < inventoryGui.buttonList.size(); i++)
			{
				GuiButton button = inventoryGui.buttonList.get(i);
				
				if (button instanceof AbstractTab)
				{
					if (recipeBook.isVisible())
						button.x = openedRecipesGuiLeft + inventoryButtonsDefaultXPositions.get(i);
					else
						button.x = inventoryGui.guiLeft + inventoryButtonsDefaultXPositions.get(i);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onPreGuiButtonClicked(GuiScreenEvent.ActionPerformedEvent.Pre event)
	{
		replaceRecipeBook(event.getGui());
		GuiCryptexRecipeBook recipeBook = getRecipeBookFromGui(event.getGui());
		
		if (recipeBook != null && event.getButton().id == 10)
		{
			GuiContainer containerGui = (GuiContainer) event.getGui();
			recipeBook.actualGui.setWorldAndResolution(event.getGui().mc, event.getGui().width, event.getGui().height);
			recipeBook.actualGui.guiLeft = (event.getGui().width - (GuiGuideBook.bookImageWidth + containerGui.getXSize())) / 2 - 7;
			recipeBook.actualGui.guiTop = (event.getGui().height - GuiGuideBook.bookImageHeight) / 2 + 15;
		}
	}
	
	@SubscribeEvent
	public static void onGuiMouseInput(GuiScreenEvent.MouseInputEvent.Pre event)
	{
		replaceRecipeBook(event.getGui());
		GuiCryptexRecipeBook recipeBook = getRecipeBookFromGui(event.getGui());
		
		if (recipeBook != null && recipeBook.isVisible())
		{
			try
			{
				if (recipeBook.actualGui.mc != null)
					recipeBook.actualGui.handleMouseInput();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static GuiCryptexRecipeBook getRecipeBookFromGui(GuiScreen gui)
	{
		if (gui instanceof GuiCrafting)
		{
			return (GuiCryptexRecipeBook) ((GuiCrafting) gui).recipeBookGui;
		}
		
		if (gui instanceof GuiInventory)
		{
			return (GuiCryptexRecipeBook) ((GuiInventory) gui).recipeBookGui;
		}
		
		return null;
	}
	
	private static void replaceRecipeBook(GuiScreen gui)
	{
		if (gui instanceof GuiCrafting)
		{
			GuiRecipeBook recipeBook = ((GuiCrafting) gui).recipeBookGui;
			
			if (!(recipeBook instanceof GuiCryptexRecipeBook))
			{
				((GuiCrafting) gui).recipeBookGui = new GuiCryptexRecipeBook();
			}
		}
		
		if (gui instanceof GuiInventory)
		{
			GuiRecipeBook recipeBook = ((GuiInventory) gui).recipeBookGui;
			
			if (!(recipeBook instanceof GuiCryptexRecipeBook))
			{
				((GuiInventory) gui).recipeBookGui = new GuiCryptexRecipeBook();
			}
		}
	}
	
	private static void hideBookButton(GuiScreen gui)
	{
		EntityPlayer clientPlayer = FMLClientHandler.instance().getClientPlayerEntity();
		
		if (gui instanceof GuiCrafting || gui instanceof GuiInventory)
		{
			gui.buttonList.get(0).visible = clientPlayer.inventory.hasItemStack(new ItemStack(ModItems.guideBook));
		}
	}
}
