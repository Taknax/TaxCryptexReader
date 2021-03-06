package tax.taknax.taxcr.plugin.vanilla.recipe.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.AbstractBrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import tax.taknax.taxcr.api.DrawableRecipe;
import tax.taknax.taxcr.api.RecipeHandler;
import tax.taknax.taxcr.api.RecipeManager;
import tax.taknax.taxcr.plugin.vanilla.recipe.DrawableRecipeBrewing;

/**
 * Created by Creysys on 26 Mar 16.
 */
@SuppressWarnings("deprecation")
public class RecipeHandlerBrewing extends RecipeHandler
{
	@Override
	public String getName()
	{
		return "guidebook.brewing";
	}
	
	@Override
	public Object getTabIcon()
	{
		return Items.BREWING_STAND;
	}
	
	@Override
	public int recipesPerPage()
	{
		return 2;
	}
	
	public boolean containsIngredient(ItemStack ingredient, ArrayList<ItemStack> ingredients)
	{
		for (ItemStack i : ingredients)
			if (i.isItemEqual(ingredient))
				return true;
		return false;
	}
	
	public void addIngredients(ArrayList<ItemStack> ingredients, ArrayList<Object> list)
	{
		try
		{
			// Class c1 =
			// Class.forName("net.minecraft.potion.PotionHelper$MixPredicate");
			// Class c2 =
			// Class.forName("net.minecraft.potion.PotionHelper$ItemPredicateInstance");
			// Field fieldPredicate = c1.getDeclaredFields()[1];
			// Field fieldItem = c2.getDeclaredFields()[0];
			// Field fieldMeta = c2.getDeclaredFields()[1];
			
			// fieldPredicate.setAccessible(true);
			// fieldItem.setAccessible(true);
			// fieldMeta.setAccessible(true);
			
			// for (Object o : list) {
			// if(!o.getClass().getName().equals("net.minecraft.potion.PotionHelper$MixPredicate"))
			// continue;
			
			// Object predicate = fieldPredicate.get(o);
			// if(!predicate.getClass().getName().equals("net.minecraft.potion.PotionHelper$ItemPredicateInstance"))
			// continue;
			
			// Item item = (Item) fieldItem.get(predicate);
			// int meta = (Integer) fieldMeta.get(predicate);
			// if(meta == -1) meta = 0;
			
			// ItemStack ingredient = new ItemStack(item, 1, meta);
			// if(!containsIngredient(ingredient, ingredients))
			// ingredients.add(ingredient);
			// }
		}
		catch (Exception e)
		{
			FMLCommonHandler.instance().raiseException(e, "addIngredients", true);
		}
	}
	
	public boolean containsRecipe(DrawableRecipeBrewing recipe, ArrayList<DrawableRecipe> recipes)
	{
		for (DrawableRecipe dr : recipes)
		{
			DrawableRecipeBrewing r = (DrawableRecipeBrewing) dr;
			if (RecipeManager.equalItems(r.input, recipe.input) && RecipeManager.equalItems(r.ingredient, recipe.ingredient))
				return true;
		}
		return false;
	}
	
	public void addVanillaBrewingRecipes(ArrayList<DrawableRecipe> recipes)
	{
		ArrayList<ItemStack> ingredients = new ArrayList<ItemStack>();
		
		ArrayList<Object> typeConversions = ReflectionHelper.getPrivateValue(PotionHelper.class, null, 0);
		ArrayList<Object> itemConversions = ReflectionHelper.getPrivateValue(PotionHelper.class, null, 1);
		
		addIngredients(ingredients, typeConversions);
		addIngredients(ingredients, itemConversions);
		
		ArrayList<ItemStack> knownPotions = new ArrayList<ItemStack>();
		ItemStack waterBottle = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
		knownPotions.add(waterBottle);
		
		int brewingStep = 1;
		boolean foundNewPotions;
		do
		{
			List<ItemStack> newPotions = getNewPotions(knownPotions, ingredients, recipes);
			foundNewPotions = !newPotions.isEmpty();
			knownPotions.addAll(newPotions);
			
			brewingStep++;
			if (brewingStep > 100)
			{
				FMLCommonHandler.instance().raiseException(null, "Calculation of vanilla brewing recipes is broken, aborting after 100 brewing steps.", false);
				return;
			}
		}
		while (foundNewPotions);
	}
	
	private List<ItemStack> getNewPotions(ArrayList<ItemStack> knownPotions, ArrayList<ItemStack> potionIngredients, ArrayList<DrawableRecipe> recipes)
	{
		List<ItemStack> newPotions = new ArrayList<ItemStack>();
		for (ItemStack potionInput : knownPotions)
		{
			for (ItemStack potionIngredient : potionIngredients)
			{
				ItemStack potionOutput = PotionHelper.doReaction(potionIngredient, potionInput.copy());
				if (potionOutput == null)
				{
					continue;
				}
				
				if (potionInput.getItem() == potionOutput.getItem())
				{
					PotionType potionOutputType = PotionUtils.getPotionFromItem(potionOutput);
					if (potionOutputType == PotionTypes.WATER)
					{
						continue;
					}
					
					PotionType potionInputType = PotionUtils.getPotionFromItem(potionInput);
					int inputId = PotionType.REGISTRY.getIDForObject(potionInputType);
					int outputId = PotionType.REGISTRY.getIDForObject(potionOutputType);
					if (inputId == outputId)
					{
						continue;
					}
				}
				
				DrawableRecipeBrewing recipe = new DrawableRecipeBrewing(potionInput, potionIngredient, potionOutput);
				if (!containsRecipe(recipe, recipes))
				{
					recipes.add(recipe);
					newPotions.add(potionOutput);
				}
			}
		}
		return newPotions;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<DrawableRecipe> getRecipes()
	{
		ArrayList<DrawableRecipe> ret = new ArrayList<DrawableRecipe>();
		
		addVanillaBrewingRecipes(ret);
		
		for (IBrewingRecipe iBrewingRecipe : BrewingRecipeRegistry.getRecipes())
			if (iBrewingRecipe instanceof AbstractBrewingRecipe)
			{
				AbstractBrewingRecipe<?> recipe = (AbstractBrewingRecipe<?>) iBrewingRecipe;
				ItemStack ingredient;
				if (recipe.getIngredient() instanceof ItemStack)
					ingredient = (ItemStack) recipe.getIngredient();
				else if (recipe.getIngredient() instanceof List)
				{
					List<ItemStack> ores = (List<ItemStack>) recipe.getIngredient();
					if (ores.size() == 0)
						continue;
					ingredient = ores.get(0);
				}
				else if (recipe.getIngredient() instanceof String)
				{
					List<ItemStack> ores = OreDictionary.getOres((String) recipe.getIngredient());
					if (ores.size() == 0)
						continue;
					ingredient = ores.get(0);
				}
				else
					continue;
				ret.add(new DrawableRecipeBrewing(recipe.getInput(), ingredient, recipe.getOutput()));
			}
		return ret;
	}
}
