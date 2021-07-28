package tax.taknax.taxcr.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.oredict.OreDictionary;
import tax.taknax.taxcr.api.DrawableRecipe;
import tax.taknax.taxcr.api.IGuiAccessor;
import tax.taknax.taxcr.api.RecipeHandler;
import tax.taknax.taxcr.api.RecipeManager;
import tax.taknax.taxcr.common.proxy.ProxyClient;

/**
 * Created by Creysys on 20 Mar 16.
 */
public class GuiGuideBook extends GuiScreen implements IGuiAccessor
{
	public abstract class State
	{
		public class GuiButton
		{
			private int id;
			private int x;
			private int y;
			private int textureX;
			private int textureY;
			private int width;
			private int height;
			private String text;
			
			public GuiButton(int id, int x, int y, int textureX, int textureY, int width, int height, String text)
			{
				this.id = id;
				this.x = x;
				this.y = y;
				this.textureX = textureX;
				this.textureY = textureY;
				this.width = width;
				this.height = height;
				this.text = text;
			}
			
			public void mouseClicked(int mouseX, int mouseY)
			{
				if (x <= mouseX && mouseX <= x + width && y <= mouseY && mouseY <= y + height)
				{
					buttonClicked(id);
				}
			}
			
			public void draw(int mouseX, int mouseY)
			{
				mc.getTextureManager().bindTexture(bookGuiTextures);
				RenderHelper.disableStandardItemLighting();
				
				if (x <= mouseX && mouseX <= x + width && y <= mouseY && mouseY <= y + height)
				{
					drawTexturedModalRect(x, y, textureX + 23, textureY, width, height);
					drawHoveringText(Arrays.asList(text), mouseX, mouseY);
				}
				else
					drawTexturedModalRect(x, y, textureX, textureY, width, height);
			}
		}
		
		public State lastState;
		
		public State(State lastState)
		{
			this.lastState = lastState;
		}
		
		public abstract void draw(int mouseX, int mouseY);
		
		public abstract void drawForeground(int mouseX, int mouseY);
		
		public abstract void update();
		
		public abstract boolean keyTyped(char typedChar, int keyCode);
		
		public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);
		
		public abstract void buttonClicked(int id);
		
		public abstract void mouseScrolled(int amount);
	}
	
	public class StateHome extends State
	{
		private final int itemSize = 18;
		private final int itemsPerRow = 6;
		
		private GuiTextField searchBar;
		private ArrayList<ItemStack> searchResult;
		private int page;
		private int mouseX;
		private int mouseY;
		
		private GuiButton previous;
		private GuiButton next;
		
		public StateHome()
		{
			super(null);
			searchBar = new GuiTextField(0, fontRenderer, guiLeft + 38, guiTop + 22, 110, 16);
			updateSearchResult();
			page = 0;
			
			previous = new GuiButton(0, guiLeft + 28, guiTop + 150, 3, 207, 18, 10, I18n.format("guidebook.previousPage"));
			next = new GuiButton(1, guiLeft + 134, guiTop + 150, 3, 194, 18, 10, I18n.format("guidebook.nextPage"));
		}
		
		private String getModName(String modId)
		{
			for (ModContainer container : Loader.instance().getModList())
			{
				if (container.getModId().toLowerCase().equals(modId.toLowerCase()))
					return container.getName();
			}
			return "";
		}
		
		private void updateSearchResult()
		{
			String pattern = searchBar.getText().toLowerCase();
			searchResult = new ArrayList<ItemStack>();
			for (ItemStack stack : RecipeManager.craftableItems)
			{
				if (stack == null)
					continue;
				
				if (pattern.startsWith("@"))
				{
					String modPattern = pattern.substring(1);
					Item item = stack.getItem();
					if (item == null)
						continue;
					String itemMod = getModName(item.delegate.name().getResourceDomain());
					
					if (itemMod.toLowerCase().contains(modPattern) && !RecipeManager.containsItemStack(searchResult, stack))
						searchResult.add(stack);
				}
				else
				{
					String displayName = stack.getDisplayName();
					if (displayName == null)
						continue;
					
					if (displayName.toLowerCase().contains(pattern) && !RecipeManager.containsItemStack(searchResult, stack))
						searchResult.add(stack);
				}
			}
			
			Collections.sort(searchResult, new Comparator<ItemStack>()
			{
				@Override
				public int compare(ItemStack o1, ItemStack o2)
				{
					int x = Item.getIdFromItem(o1.getItem());
					int y = Item.getIdFromItem(o2.getItem());
					
					return (x < y) ? -1 : ((x == y) ? 0 : 1);
				}
			});
			
			page = 0;
		}
		
		@Override
		public void draw(int mouseX, int mouseY)
		{
			this.mouseX = mouseX;
			this.mouseY = mouseY;
			
			if (insideAnotherGui)
			{
				searchBar.x = guiLeft + 38;
				searchBar.y = guiTop + 22;
				previous.x = guiLeft + 28;
				previous.y = guiTop + 150;
				next.x = guiLeft + 134;
				next.y = guiTop + 150;
			}
			
			fontRenderer.drawString(I18n.format("guidebook.search"), guiLeft + 38, guiTop + 10, 0x000000);
			
			RenderHelper.disableStandardItemLighting();
			drawRect(guiLeft + 38, guiTop + 36, guiLeft + 38 + 110, guiTop + 42 + 110, 0x99555555);
			
			searchBar.drawTextBox();
			
			RenderHelper.enableGUIStandardItemLighting();
			if (searchResult != null)
			{
				for (int i = page * 36; i < searchResult.size() && i < page * 36 + 36; i++)
				{
					int x = guiLeft + 40 + (i % itemsPerRow) * itemSize;
					int y = guiTop + 42 + i / itemsPerRow * itemSize - page * itemSize * 6;
					itemRender.renderItemAndEffectIntoGUI(searchResult.get(i), x, y);
				}
			}
			
			if (page > 0)
			{
				previous.draw(mouseX, mouseY);
			}
			if (searchResult != null && searchResult.size() > page * 36 + 36)
			{
				next.draw(mouseX, mouseY);
			}
		}
		
		@Override
		public void drawForeground(int mouseX, int mouseY)
		{
			if (searchResult != null)
			{
				for (int i = page * 36; i < searchResult.size() && i < page * 36 + 36; i++)
				{
					int x = guiLeft + 40 + (i % itemsPerRow) * itemSize;
					int y = guiTop + 42 + i / itemsPerRow * itemSize - page * itemSize * 6;
					
					List<String> lines = searchResult.get(i).getTooltip(Minecraft.getMinecraft().player, TooltipFlags.NORMAL);
					
					if (x < mouseX && mouseX < x + itemSize && y < mouseY && mouseY < y + itemSize)
					{
						drawHoveringText(lines, mouseX, mouseY);
					}
				}
			}
		}
		
		@Override
		public void update()
		{
			searchBar.updateCursorCounter();
		}
		
		@Override
		public boolean keyTyped(char typedChar, int keyCode)
		{
			if (searchBar.textboxKeyTyped(typedChar, keyCode))
			{
				updateSearchResult();
				return true;
			}
			
			if (searchResult != null)
			{
				for (int i = page * 36; i < searchResult.size() && i < page * 36 + 36; i++)
				{
					int x = guiLeft + 40 + (i % itemsPerRow) * itemSize;
					int y = guiTop + 42 + i / itemsPerRow * itemSize - page * itemSize * 6;
					
					if (x < mouseX && mouseX < x + itemSize && y < mouseY && mouseY < y + itemSize)
					{
						if (keyCode == ProxyClient.recipeKey.getKeyCode())
						{
							openRecipeState(searchResult.get(i));
							break;
						}
						else if (keyCode == ProxyClient.usageKey.getKeyCode())
						{
							openUsageState(searchResult.get(i));
							break;
						}
					}
				}
			}
			
			return false;
		}
		
		@Override
		public void mouseClicked(int mouseX, int mouseY, int mouseButton)
		{
			searchBar.mouseClicked(mouseX, mouseY, mouseButton);
			
			if (mouseButton == 0)
			{
				previous.mouseClicked(mouseX, mouseY);
				next.mouseClicked(mouseX, mouseY);
			}
			else if (mouseButton == 1 && searchBar.x < mouseX && mouseX < searchBar.x + searchBar.width && searchBar.y < mouseY
					&& mouseY < searchBar.y + searchBar.height)
			{
				searchBar.setText("");
				updateSearchResult();
			}
			
			if (searchResult != null)
			{
				for (int i = page * 36; i < searchResult.size() && i < page * 36 + 36; i++)
				{
					int x = guiLeft + 40 + (i % itemsPerRow) * itemSize;
					int y = guiTop + 42 + i / itemsPerRow * itemSize - page * itemSize * 6;
					
					if (x < mouseX && mouseX < x + itemSize && y < mouseY && mouseY < y + itemSize)
					{
						if (mouseButton == 0)
						{
							openRecipeState(searchResult.get(i));
							break;
						}
						else if (mouseButton == 1)
						{
							openUsageState(searchResult.get(i));
							break;
						}
					}
				}
			}
		}
		
		@Override
		public void buttonClicked(int id)
		{
			if (id == 0 && page > 0)
			{
				playPageSound();
				page--;
			}
			if (id == 1 && (searchResult != null && searchResult.size() > page * 36 + 36))
			{
				playPageSound();
				page++;
			}
		}
		
		@Override
		public void mouseScrolled(int amount)
		{
			buttonClicked(amount < 0 ? 1 : 0);
		}
	}
	
	public class StateRecipe extends State
	{
		private LinkedHashMap<RecipeHandler, ArrayList<DrawableRecipe>> handlers;
		private int tab;
		private int page;
		public String cmd;
		public Object arg;
		
		private GuiButton previous;
		private GuiButton next;
		
		public StateRecipe(State lastState, LinkedHashMap<RecipeHandler, ArrayList<DrawableRecipe>> handlers, String cmd, Object arg)
		{
			super(lastState);
			this.handlers = handlers;
			this.tab = 0;
			this.page = 0;
			this.cmd = cmd;
			this.arg = arg;
			
			previous = new GuiButton(0, guiLeft + 28, guiTop + 150, 3, 207, 18, 10, I18n.format("guidebook.previousPage"));
			next = new GuiButton(1, guiLeft + 134, guiTop + 150, 3, 194, 18, 10, I18n.format("guidebook.nextPage"));
		}
		
		public Map.Entry<RecipeHandler, ArrayList<DrawableRecipe>> getTab()
		{
			int j = tab;
			for (Map.Entry<RecipeHandler, ArrayList<DrawableRecipe>> e : handlers.entrySet())
			{
				if (j == 0)
				{
					return e;
				}
				j--;
			}
			return null;
		}
		
		public void drawTabIcon(RecipeHandler h, int x, int y)
		{
			Object o = h.getTabIcon();
			if (o instanceof Item)
				o = new ItemStack((Item) o);
			else if (o instanceof Block)
				o = new ItemStack((Block) o);
			
			if (o instanceof ItemStack)
				itemRender.renderItemAndEffectIntoGUI((ItemStack) o, x, y);
			else if (o instanceof ResourceLocation)
			{
				mc.getTextureManager().bindTexture((ResourceLocation) o);
				drawScaledCustomSizeModalRect(x, y, 0, 0, 16, 16, 16, 16, 16, 16);
			}
		}
		
		@Override
		public void draw(int mouseX, int mouseY)
		{
			if (insideAnotherGui)
			{
				previous.x = guiLeft + 28;
				previous.y = guiTop + 150;
				next.x = guiLeft + 134;
				next.y = guiTop + 150;
			}
			
			Map.Entry<RecipeHandler, ArrayList<DrawableRecipe>> tab = getTab();
			RecipeHandler handler = tab.getKey();
			ArrayList<DrawableRecipe> recipes = tab.getValue();
			
			for (int i = 0; i < handler.recipesPerPage(); i++)
			{
				if (recipes.size() > page * handler.recipesPerPage() + i)
					recipes.get(page * handler.recipesPerPage() + i).draw(GuiGuideBook.this, i, mouseX, mouseY);
			}
			
			int i = 0;
			for (RecipeHandler h : handlers.keySet())
			{
				int x = guiLeft + 155;
				int y = guiTop + 8 + i * 22;
				
				if (h == handler)
				{
					mc.getTextureManager().bindTexture(bookGuiTextures);
					RenderHelper.disableStandardItemLighting();
					drawTexturedModalRect(x, y, 3, 233, 20, 22);
					RenderHelper.enableGUIStandardItemLighting();
					drawTabIcon(h, x + 1, y + 3);
				}
				else
				{
					mc.getTextureManager().bindTexture(bookGuiTextures);
					RenderHelper.disableStandardItemLighting();
					drawTexturedModalRect(x, y, 26, 233, 22, 22);
					RenderHelper.enableGUIStandardItemLighting();
					drawTabIcon(h, x + 3, y + 3);
				}
				
				i++;
			}
			
			if (page > 0)
				previous.draw(mouseX, mouseY);
			if (recipes.size() > page * handler.recipesPerPage() + handler.recipesPerPage())
				next.draw(mouseX, mouseY);
		}
		
		@Override
		public void drawForeground(int mouseX, int mouseY)
		{
			Map.Entry<RecipeHandler, ArrayList<DrawableRecipe>> tab = getTab();
			RecipeHandler handler = tab.getKey();
			ArrayList<DrawableRecipe> recipes = tab.getValue();
			
			GlStateManager.disableRescaleNormal();
			RenderHelper.disableStandardItemLighting();
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			
			for (int i = 0; i < handler.recipesPerPage(); i++)
			{
				if (recipes.size() > page * handler.recipesPerPage() + i)
				{
					DrawableRecipe recipe = recipes.get(page * handler.recipesPerPage() + i);
					recipe.drawForeground(GuiGuideBook.this, i, mouseX, mouseY);
				}
			}
			
			int i = 0;
			for (RecipeHandler h : handlers.keySet())
			{
				int x = guiLeft + 155;
				int y = guiTop + 8 + i * 22;
				
				mc.getTextureManager().bindTexture(bookGuiTextures);
				if (x < mouseX && mouseX < x + 20 && y < mouseY && mouseY < y + 22)
				{
					drawHoveringString(I18n.format(h.getName()), mouseX, mouseY);
				}
				
				i++;
			}
		}
		
		@Override
		public void update()
		{
			Map.Entry<RecipeHandler, ArrayList<DrawableRecipe>> tab = getTab();
			RecipeHandler handler = tab.getKey();
			ArrayList<DrawableRecipe> recipes = tab.getValue();
			
			for (int i = 0; i < handler.recipesPerPage(); i++)
			{
				if (recipes.size() > page * handler.recipesPerPage() + i)
					recipes.get(page * handler.recipesPerPage() + i).update();
			}
		}
		
		@Override
		public boolean keyTyped(char typedChar, int keyCode)
		{
			return false;
		}
		
		@Override
		public void mouseClicked(int mouseX, int mouseY, int mouseButton)
		{
			Map.Entry<RecipeHandler, ArrayList<DrawableRecipe>> tab = getTab();
			RecipeHandler handler = tab.getKey();
			ArrayList<DrawableRecipe> recipes = tab.getValue();
			
			for (int i = 0; i < handler.recipesPerPage(); i++)
			{
				if (recipes.size() > page * handler.recipesPerPage() + i)
					recipes.get(page * handler.recipesPerPage() + i).mouseClick(GuiGuideBook.this, i, mouseX, mouseY, mouseButton);
			}
			
			if (mouseButton == 0)
			{
				previous.mouseClicked(mouseX, mouseY);
				next.mouseClicked(mouseX, mouseY);
			}
			
			int i = 0;
			for (RecipeHandler h : handlers.keySet())
			{
				int x = guiLeft + 155;
				int y = guiTop + 8 + i * 22;
				
				mc.getTextureManager().bindTexture(bookGuiTextures);
				if (x < mouseX && mouseX < x + 20 && y < mouseY && mouseY < y + 22)
				{
					if (mouseButton == 0 && h != handler)
					{
						playButtonSound();
						this.tab = i;
						this.page = 0;
						break;
					}
					else if (mouseButton == 1)
					{
						openHandlerUsageState(h);
						break;
					}
				}
				
				i++;
			}
		}
		
		@Override
		public void buttonClicked(int id)
		{
			Map.Entry<RecipeHandler, ArrayList<DrawableRecipe>> tab = getTab();
			RecipeHandler handler = tab.getKey();
			ArrayList<DrawableRecipe> recipes = tab.getValue();
			
			if (id == 0 && page > 0)
			{
				playPageSound();
				page--;
			}
			if (id == 1 && recipes.size() > page * handler.recipesPerPage() + handler.recipesPerPage())
			{
				playPageSound();
				page++;
			}
		}
		
		@Override
		public void mouseScrolled(int amount)
		{
			buttonClicked(amount < 0 ? 1 : 0);
		}
	}
	
	public static final ResourceLocation bookGuiTextures = new ResourceLocation("taxcr", "textures/gui/guidebook.png");
	public static final ResourceLocation openBookSound = new ResourceLocation("taxcr", "openbook");
	public static final int bookImageWidth = 192;
	public static final int bookImageHeight = 192;
	
	public static String onOpenCmd = null;
	public static Object onOpenArg = null;
	
	public State state;
	public int guiTop;
	public int guiLeft;
	public boolean insideAnotherGui;
	
	private final GuiScreen previousGui;
	
	public GuiGuideBook()
	{
		this.previousGui = null;
	}
	
	public GuiGuideBook(GuiScreen previousGui)
	{
		this.previousGui = previousGui;
	}
	
	public ArrayList<DrawableRecipe> getRecipesFor(ArrayList<DrawableRecipe> recipes, ItemStack stack)
	{
		ArrayList<DrawableRecipe> ret = new ArrayList<DrawableRecipe>();
		for (DrawableRecipe recipe : recipes)
		{
			ItemStack output = recipe.getOutput();
			if (output == null || output.getItem() == null)
				continue;
			if (RecipeManager.equalItems(stack, output) || (output.getItemDamage() == OreDictionary.WILDCARD_VALUE && stack.getItem() == output.getItem()))
				ret.add(recipe);
		}
		return ret;
	}
	
	public ArrayList<DrawableRecipe> getRecipesWith(ArrayList<DrawableRecipe> recipes, ItemStack stack)
	{
		ArrayList<DrawableRecipe> ret = new ArrayList<DrawableRecipe>();
		for (DrawableRecipe recipe : recipes)
			for (ItemStack input : recipe.getInput())
				if (input != null && input.getItem() != null)
					if (RecipeManager.equalItems(input, stack) || (input.getItemDamage() == OreDictionary.WILDCARD_VALUE && input.getItem() == stack.getItem()))
					{
						ret.add(recipe);
						break;
					}
		return ret;
	}
	
	public void openRecipeState(ItemStack stack)
	{
		if (state != null && state instanceof StateRecipe)
		{
			StateRecipe r = (StateRecipe) state;
			if (r.cmd.equals("recipe") && r.arg instanceof ItemStack)
				if (RecipeManager.equalItems((ItemStack) r.arg, stack))
					return;
		}
		if (state != null && state.lastState instanceof StateRecipe)
		{
			StateRecipe r = (StateRecipe) state.lastState;
			if (r.cmd.equals("recipe") && r.arg instanceof ItemStack)
				if (RecipeManager.equalItems((ItemStack) r.arg, stack))
				{
					playButtonSound();
					state = state.lastState;
					return;
				}
		}
		
		LinkedHashMap<RecipeHandler, ArrayList<DrawableRecipe>> handlers = new LinkedHashMap<RecipeHandler, ArrayList<DrawableRecipe>>();
		for (Map.Entry<RecipeHandler, ArrayList<DrawableRecipe>> entry : RecipeManager.loadedRecipes.entrySet())
		{
			ArrayList<DrawableRecipe> recipes = getRecipesFor(entry.getValue(), stack);
			if (recipes.size() > 0)
				handlers.put(entry.getKey(), recipes);
		}
		
		if (handlers.size() > 0)
		{
			playButtonSound();
			state = new StateRecipe(state, handlers, "recipe", stack);
		}
	}
	
	public void openUsageState(ItemStack stack)
	{
		if (state != null && state instanceof StateRecipe)
		{
			StateRecipe r = (StateRecipe) state;
			if (r.cmd.equals("usage") && r.arg instanceof ItemStack)
				if (RecipeManager.equalItems((ItemStack) r.arg, stack))
					return;
		}
		if (state != null && state.lastState instanceof StateRecipe)
		{
			StateRecipe r = (StateRecipe) state.lastState;
			if (r.cmd.equals("usage") && r.arg instanceof ItemStack)
				if (RecipeManager.equalItems((ItemStack) r.arg, stack))
				{
					playButtonSound();
					state = state.lastState;
					return;
				}
		}
		
		LinkedHashMap<RecipeHandler, ArrayList<DrawableRecipe>> handlers = new LinkedHashMap<RecipeHandler, ArrayList<DrawableRecipe>>();
		for (Map.Entry<RecipeHandler, ArrayList<DrawableRecipe>> entry : RecipeManager.loadedRecipes.entrySet())
		{
			ArrayList<DrawableRecipe> recipes = getRecipesWith(entry.getValue(), stack);
			if (recipes.size() > 0)
				handlers.put(entry.getKey(), recipes);
		}
		
		if (handlers.size() > 0)
		{
			playButtonSound();
			state = new StateRecipe(state, handlers, "usage", stack);
		}
	}
	
	public void openHandlerUsageState(RecipeHandler handler)
	{
		if (state != null && state instanceof StateRecipe)
		{
			StateRecipe r = (StateRecipe) state;
			if (r.cmd.equals("recipe") && r.arg == handler)
				return;
		}
		if (state != null && state.lastState instanceof StateRecipe)
		{
			StateRecipe r = (StateRecipe) state.lastState;
			if (r.cmd.equals("recipe") && r.arg == handler)
			{
				playButtonSound();
				state = state.lastState;
				return;
			}
		}
		
		LinkedHashMap<RecipeHandler, ArrayList<DrawableRecipe>> handlers = new LinkedHashMap<RecipeHandler, ArrayList<DrawableRecipe>>();
		handlers.put(handler, RecipeManager.loadedRecipes.get(handler));
		
		playButtonSound();
		state = new StateRecipe(state, handlers, "usage", handler);
	}
	
	public RenderItem getRenderItem()
	{
		return itemRender;
	}
	
	public FontRenderer getFontRenderer()
	{
		return fontRenderer;
	}
	
	public Minecraft getMc()
	{
		return mc;
	}
	
	public int getLeft()
	{
		return guiLeft;
	}
	
	public int getTop()
	{
		return guiTop;
	}
	
	public void drawHoveringStrings(List<String> lines, int x, int y)
	{
		super.drawHoveringText(lines, x, y);
	}
	
	public void drawHoveringString(String s, int x, int y)
	{
		drawHoveringText(Arrays.asList(s), x, y);
	}
	
	public void playButtonSound()
	{
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
	
	public void playPageSound()
	{
		float pitch = mc.world.rand.nextFloat() % 0.4f + 0.6f;
		mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(new SoundEvent(openBookSound), pitch));
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		guiLeft = (width - bookImageWidth) / 2;
		guiTop = (height - bookImageHeight) / 2;
		
		if (onOpenCmd != null && onOpenArg != null)
		{
			if (onOpenCmd.equals("recipe"))
			{
				if (onOpenArg instanceof ItemStack)
					openRecipeState((ItemStack) onOpenArg);
			}
			else if (onOpenCmd.equals("usage"))
			{
				if (onOpenArg instanceof ItemStack)
					openUsageState((ItemStack) onOpenArg);
				else if (onOpenArg instanceof RecipeHandler)
					openHandlerUsageState((RecipeHandler) onOpenArg);
			}
			
			onOpenCmd = null;
			onOpenArg = null;
		}
		else
		{
			state = new StateHome();
			playPageSound();
		}
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(bookGuiTextures);
		RenderHelper.disableStandardItemLighting();
		drawTexturedModalRect(guiLeft, guiTop - 10, 0, 0, bookImageWidth, bookImageHeight);
		
		state.draw(mouseX, mouseY);
		
		if (state.lastState != null)
		{
			mc.getTextureManager().bindTexture(bookGuiTextures);
			RenderHelper.disableStandardItemLighting();
			int x = guiLeft + 28;
			int y = guiTop - 6;
			
			if (x < mouseX && mouseX < x + 18 && y < mouseY && mouseY < y + 10)
			{
				drawTexturedModalRect(x, y, 26, 220, 18, 10);
				drawHoveringText(Arrays.asList(I18n.format("guidebook.back")), mouseX, mouseY);
			}
			else
				drawTexturedModalRect(x, y, 3, 220, 18, 10);
		}
		RenderHelper.enableStandardItemLighting();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		if (!insideAnotherGui)
			drawDefaultBackground();
		
		drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		
		if (!insideAnotherGui)
			drawForeground(mouseX, mouseY);
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		state.update();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode)
	{
		if (state.keyTyped(typedChar, keyCode))
			return;
		
		if (keyCode == Keyboard.KEY_ESCAPE)
		{
			if (state instanceof StateRecipe)
			{
				if(this.previousGui == null)
				{
					state = new StateHome();
				}
				else
				{
					Minecraft.getMinecraft().displayGuiScreen(this.previousGui);
				}
			}
			else
			{
				this.mc.player.closeScreen();
			}
		}
		else if (keyCode == 14)
		{
			if (state.lastState != null)
			{
				playButtonSound();
				state = state.lastState;
			}
		}
		else if (keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode())
		{
			this.mc.player.closeScreen();
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		state.mouseClicked(mouseX, mouseY, mouseButton);
		
		if (state.lastState != null)
		{
			int x = guiLeft + 28;
			int y = guiTop - 6;
			
			if (x < mouseX && mouseX < x + 18 && y < mouseY && mouseY < y + 10)
			{
				playButtonSound();
				state = state.lastState;
			}
		}
	}
	
	@Override
	public void handleMouseInput() throws IOException
	{
		int mouseScroll = Mouse.getEventDWheel();
		
		if (mouseScroll != 0)
		{
			state.mouseScrolled(mouseScroll);
		}
		
		super.handleMouseInput();
	}
	
	public void drawForeground(int mouseX, int mouseY)
	{
		if (state != null)
		{
			state.drawForeground(mouseX, mouseY);
		}
	}
}
