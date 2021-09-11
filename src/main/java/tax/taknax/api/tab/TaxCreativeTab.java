package tax.taknax.api.tab;

import java.util.function.Supplier;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TaxCreativeTab extends CreativeTabs
{
	private final Supplier<ItemStack> tabIconItemSupplier;
	
	public TaxCreativeTab(Supplier<ItemStack> tabIconItemSupplier)
	{
		super("");
		this.tabIconItemSupplier = tabIconItemSupplier;
	}
	
	@Override
	public String getTranslatedTabLabel()
	{
		return "Taknax Mods";
	}
	
	@Override
	public ItemStack getTabIconItem()
	{
		return tabIconItemSupplier.get();
	}
}
