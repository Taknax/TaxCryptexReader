package tax.taknax.taxcr.plugin.vanilla.recipe.handler;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import tax.taknax.taxcr.api.DrawableRecipe;
import tax.taknax.taxcr.api.RecipeHandler;
import tax.taknax.taxcr.plugin.vanilla.recipe.DrawableRecipeSmelting;

/**
 * Created by Creysys on 21 Mar 16.
 */
public class RecipeHandlerSmelting extends RecipeHandler
{
	@Override
	public String getName()
	{
		return "guidebook.smelting";
	}
	
	@Override
	public Object getTabIcon()
	{
		return Blocks.FURNACE;
	}
	
	@Override
	public int recipesPerPage()
	{
		return 2;
	}
	
	@Override
	public ArrayList<DrawableRecipe> getRecipes()
	{
		ArrayList<DrawableRecipe> ret = new ArrayList<DrawableRecipe>();
		for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.instance().getSmeltingList().entrySet())
		{
			if (entry.getKey() != null && entry.getValue() != null)
				ret.add(new DrawableRecipeSmelting(entry.getKey(), entry.getValue()));
		}
		return ret;
	}
}
