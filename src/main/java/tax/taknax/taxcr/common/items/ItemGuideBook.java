package tax.taknax.taxcr.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import tax.taknax.taxcr.GuideBookMod;

/**
 * Created by Creysys on 20 Mar 16.
 */
public class ItemGuideBook extends Item
{	
	public ItemGuideBook(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(GuideBookMod.MODID, name);
	}
	
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (world.isRemote)
			player.openGui(GuideBookMod.instance, GuideBookMod.GuiId.GuideBook, world, 0, 0, 0);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
