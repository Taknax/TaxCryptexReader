package tax.taknax.taxcr.helper;

import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import tax.taknax.taxcr.GuideBookMod;

public class ReflectionHelper {

    private static final String GUIINVENTORY_RECIPEBUTTON = "field_192048_z";
    private static final String GUICRAFTING_RECIPEBUTTON = "field_192049_w";

    public static void setRecipeButton(GuiContainer instance, GuiButtonImage button) {

        try {

            if (instance instanceof GuiCrafting) {

                ObfuscationReflectionHelper.setPrivateValue(GuiCrafting.class, (GuiCrafting) instance, button, GUICRAFTING_RECIPEBUTTON);

            } else if (instance instanceof GuiInventory) {

                ObfuscationReflectionHelper.setPrivateValue(GuiInventory.class, (GuiInventory) instance, button, GUIINVENTORY_RECIPEBUTTON);

            }

        } catch (Exception e) {

            GuideBookMod.LOGGER.error("setRecipeButton() failed", e);

        }

    }

}