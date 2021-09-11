// 
// Decompiled by Procyon v0.5.36
// 

package micdoodle8.mods.galacticraft.api.client.tabs;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TabRegistry
{
	private static ArrayList<AbstractTab> tabList;
	private static Class<?> clazzJEIConfig;
	public static Class<?> clazzNEIConfig;
	private static Minecraft mc;
	private static boolean initWithPotion;
	
	public static void registerTab(final AbstractTab tab)
	{
		TabRegistry.tabList.add(tab);
	}
	
	public static ArrayList<AbstractTab> getTabList()
	{
		return TabRegistry.tabList;
	}
	
	public static void addTabsToInventory(final GuiContainer gui)
	{
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void guiPostInit(final GuiScreenEvent.InitGuiEvent.Post event)
	{
		if (event.getGui() instanceof GuiInventory)
		{
			int guiLeft = (event.getGui().width - 176) / 2;
			final int guiTop = (event.getGui().height - 166) / 2;
			guiLeft += getPotionOffset();
			updateTabValues(guiLeft, guiTop, InventoryTabVanilla.class);
			addTabsToList(event.getButtonList());
		}
	}
	
	public static void openInventoryGui()
	{
		TabRegistry.mc.player.connection.sendPacket(new CPacketCloseWindow(TabRegistry.mc.player.openContainer.windowId));
		final GuiInventory inventory = new GuiInventory(TabRegistry.mc.player);
		TabRegistry.mc.displayGuiScreen(inventory);
	}
	
	public static void updateTabValues(final int cornerX, final int cornerY, final Class<?> selectedButton)
	{
		int count = 2;
		for (int i = 0; i < TabRegistry.tabList.size(); ++i)
		{
			final AbstractTab t = TabRegistry.tabList.get(i);
			if (t.shouldAddToList())
			{
				t.id = count;
				t.x = cornerX + (count - 2) * 28;
				t.y = cornerY - 28;
				t.enabled = !t.getClass().equals(selectedButton);
				t.potionOffsetLast = getPotionOffsetNEI();
				++count;
			}
		}
	}
	
	public static void addTabsToList(final List<GuiButton> buttonList)
	{
		for (final AbstractTab tab : TabRegistry.tabList)
		{
			if (tab.shouldAddToList())
			{
				buttonList.add(tab);
			}
		}
	}
	
	public static int getPotionOffset()
	{
		if (!TabRegistry.mc.player.getActivePotionEffects().isEmpty())
		{
			TabRegistry.initWithPotion = true;
			return 60 + getPotionOffsetJEI() + getPotionOffsetNEI();
		}
		TabRegistry.initWithPotion = false;
		return 0;
	}
	
	public static int getPotionOffsetJEI()
	{
		if (TabRegistry.clazzJEIConfig != null)
		{
			try
			{
				final Object enabled = TabRegistry.clazzJEIConfig.getMethod("isOverlayEnabled", (Class<?>[]) new Class[0]).invoke(null, new Object[0]);
				if (enabled instanceof Boolean)
				{
					if (!(boolean) enabled)
					{
						return 0;
					}
					return -60;
				}
			}
			catch (Exception ex)
			{
			}
		}
		return 0;
	}
	
	public static int getPotionOffsetNEI()
	{
		if (TabRegistry.initWithPotion && TabRegistry.clazzNEIConfig != null)
		{
			try
			{
				final Object hidden = TabRegistry.clazzNEIConfig.getMethod("isHidden", (Class<?>[]) new Class[0]).invoke(null, new Object[0]);
				final Object enabled = TabRegistry.clazzNEIConfig.getMethod("isEnabled", (Class<?>[]) new Class[0]).invoke(null, new Object[0]);
				if (hidden instanceof Boolean && enabled instanceof Boolean)
				{
					if ((boolean) hidden || !(boolean) enabled)
					{
						return 0;
					}
					return -60;
				}
			}
			catch (Exception ex)
			{
			}
		}
		return 0;
	}
	
	static
	{
		TabRegistry.tabList = new ArrayList<AbstractTab>();
		TabRegistry.clazzJEIConfig = null;
		TabRegistry.clazzNEIConfig = null;
		try
		{
			TabRegistry.clazzJEIConfig = Class.forName("mezz.jei.config.Config");
		}
		catch (Exception ex)
		{
		}
		if (TabRegistry.clazzJEIConfig == null)
		{
			try
			{
				TabRegistry.clazzNEIConfig = Class.forName("codechicken.nei.NEIClientConfig");
			}
			catch (Exception ex2)
			{
			}
		}
		TabRegistry.mc = FMLClientHandler.instance().getClient();
	}
}
