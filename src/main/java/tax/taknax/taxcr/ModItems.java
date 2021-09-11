package tax.taknax.taxcr;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tax.taknax.api.tab.TaxCreativeTab;
import tax.taknax.taxcr.common.items.ItemGuideBook;

@Mod.EventBusSubscriber(modid = GuideBookMod.MODID)
public class ModItems
{
	public static CreativeTabs taxTab;
	public static Item guideBook;
	
	public static void init()
	{
		initCreativeTab();
		guideBook = new ItemGuideBook("cryptex").setCreativeTab(taxTab);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(guideBook);
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event)
	{
		registerRender(guideBook);
	}
	
	private static void initCreativeTab()
	{
		for (CreativeTabs tab : CreativeTabs.CREATIVE_TAB_ARRAY)
		{
			if (tab instanceof TaxCreativeTab)
			{
				return;
			}
		}
		
		taxTab = new TaxCreativeTab(() -> new ItemStack(guideBook));
	}
	
	private static void registerRender(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
