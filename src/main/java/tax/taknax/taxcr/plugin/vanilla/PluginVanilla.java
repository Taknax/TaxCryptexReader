package tax.taknax.taxcr.plugin.vanilla;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import tax.taknax.taxcr.api.ItemInfoManager;
import tax.taknax.taxcr.api.RecipeManager;
import tax.taknax.taxcr.plugin.vanilla.recipe.handler.RecipeHandlerBrewing;
import tax.taknax.taxcr.plugin.vanilla.recipe.handler.RecipeHandlerCrafting;
import tax.taknax.taxcr.plugin.vanilla.recipe.handler.RecipeHandlerInfo;
import tax.taknax.taxcr.plugin.vanilla.recipe.handler.RecipeHandlerSmelting;

/**
 * Created by Creysys on 06 Apr 16.
 */
public final class PluginVanilla
{
	public static void preInit()
	{
		RecipeManager.registerHandler(new RecipeHandlerCrafting());
		RecipeManager.registerHandler(new RecipeHandlerSmelting());
		RecipeManager.registerHandler(new RecipeHandlerBrewing());
		
		ItemInfoManager.setBlockInfo(Blocks.DISPENSER, 0, "guidebook.info.dispenser");
		ItemInfoManager.setBlockInfo(Blocks.NOTEBLOCK, 0, "guidebook.info.noteBlock");
		ItemInfoManager.setBlockInfo(Blocks.GOLDEN_RAIL, 0, "guidebook.info.poweredRail");
		ItemInfoManager.setBlockInfo(Blocks.DETECTOR_RAIL, 0, "guidebook.info.detectorRail");
		ItemInfoManager.setBlockInfo(Blocks.ACTIVATOR_RAIL, 0, "guidebook.info.activatorRail");
		ItemInfoManager.setBlockInfo(Blocks.PISTON, 0, "guidebook.info.piston");
		ItemInfoManager.setBlockInfo(Blocks.STICKY_PISTON, 0, "guidebook.info.stickyPiston");
		ItemInfoManager.setBlockInfo(Blocks.TNT, 0, "guidebook.info.tnt");
		ItemInfoManager.setBlockInfo(Blocks.MOSSY_COBBLESTONE, 0, "guidebook.info.mossyCobblestone");
		ItemInfoManager.setBlockInfo(Blocks.STONE_PRESSURE_PLATE, 0, "guidebook.info.stonePressurePlate");
		ItemInfoManager.setBlockInfo(Blocks.WOODEN_PRESSURE_PLATE, 0, "guidebook.info.woodPressurePlate");
		ItemInfoManager.setBlockInfo(Blocks.STONE_BUTTON, 0, "guidebook.info.stoneButton");
		ItemInfoManager.setBlockInfo(Blocks.WOODEN_BUTTON, 0, "guidebook.info.woodButton");
		ItemInfoManager.setBlockInfo(Blocks.VINE, 0, "guidebook.info.vines");
		
		ItemInfoManager.setItemInfo(Items.LINGERING_POTION, 0, "guidebook.info.lingeringPotion");
		ItemInfoManager.setItemInfo(Items.SPECTRAL_ARROW, 0, "guidebook.info.spectralArrow");
		ItemInfoManager.setItemInfo(Items.GLASS_BOTTLE, 0, "guidebook.info.glassBottle");
		ItemInfoManager.setItemInfo(Items.DRAGON_BREATH, 0, "guidebook.info.dragonBreath");
	}
	
	public static void postInit()
	{
		RecipeManager.registerHandler(new RecipeHandlerInfo());
	}
}
