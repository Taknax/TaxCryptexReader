package tax.taknax.taxcr.item;

import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import tax.taknax.taxcr.TaxCR;

public class CryptexItem extends Item
{
	public CryptexItem()
	{
	super(new Properties().tab(CreativeModeTab.TAB_MISC));
	}
	
	public Action<ItemStack> onItemRightClick(World world, Entity player, EnumHand hand)
	{
		if (world.isRemote)
			player.openGui(TaxCR.MOD_ID, TaxCR.MOD_ID.Cryptex, world, 0, 0, 0);
		return new Action<ItemStack>(InteractionResult.SUCCESS, player.getHeldItem(hand));
	}
}
