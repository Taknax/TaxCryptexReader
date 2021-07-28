package tax.taknax.taxcr.plugin.vanilla.recipe.handler;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import tax.taknax.taxcr.api.DrawableRecipe;
import tax.taknax.taxcr.api.ItemInfoManager;
import tax.taknax.taxcr.api.RecipeHandler;
import tax.taknax.taxcr.plugin.vanilla.recipe.DrawableRecipeInfo;

/**
 * Created by Creysys on 27 Mar 16.
 */
public class RecipeHandlerInfo extends RecipeHandler
{
	public static final ResourceLocation tabIcon = new ResourceLocation("taxcr", "textures/gui/infotabicon.png");
	
	@Override
	public String getName()
	{
		return "guidebook.info";
	}
	
	@Override
	public Object getTabIcon()
	{
		return tabIcon;
	}
	
	@Override
	public int recipesPerPage()
	{
		return 1;
	}
	
	@Override
	public ArrayList<DrawableRecipe> getRecipes()
	{
		ArrayList<DrawableRecipe> ret = new ArrayList<DrawableRecipe>();
		for (Map.Entry<ItemStack, String> entry : ItemInfoManager.infos.entrySet())
			ret.add(new DrawableRecipeInfo(entry.getKey(), entry.getValue()));
		
		return ret;
	}
}
