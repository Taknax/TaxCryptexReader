package tax.taknax.taxcr.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tax.taknax.taxcr.TaxCR;
@Mod.EventBusSubscriber(modid = TaxCR.MOD_ID)
public class ItemInit
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TaxCR.MOD_ID);
	
	public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("cryptex", () -> new Item(new Properties().tab(CreativeModeTab.TAB_MISC)));
}





//package tax.taknax.taxcr;

//import net.minecraft.client.renderer.block.model.ModelResourceLocation;
//import net.minecraft.creativetab.CreativeTabs;
//import net.minecraft.item.Item;
//import net.minecraftforge.client.event.ModelRegistryEvent;
//import net.minecraftforge.client.model.ModelLoader;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import tax.taknax.taxcr.common.items.ItemGuideBook;

//@Mod.EventBusSubscriber(modid = TaxCR.MODID)
//public class ModItems
//{
//	public static Item guideBook;
//	
//	public static void init()
//	{
//		guideBook = new ItemGuideBook("guidebook").setCreativeTab(CreativeTabs.MISC);
//	}
//	
//	@SubscribeEvent
//	public static void registerItems(RegistryEvent.Register<Item> event)
//	{
//		event.getRegistry().registerAll(guideBook);
//	}
//	
//	@SubscribeEvent
//	public static void registerRenders(ModelRegistryEvent event)
//	{
//		registerRender(guideBook);
//	}
//	
//	private static void registerRender(Item item)
//	{
//		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
//	}
//}
