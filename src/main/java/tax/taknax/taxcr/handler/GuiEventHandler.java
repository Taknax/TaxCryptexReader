package tax.taknax.taxcr.handler;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tax.taknax.taxcr.client.gui.GuiButtonBook;
import tax.taknax.taxcr.helper.ReflectionHelper;

public class GuiEventHandler {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void guiInit(GuiScreenEvent.InitGuiEvent.Post evt) {

        GuiScreen gui = evt.getGui();
        GuiButtonBook animatedBook = null;

        if (gui instanceof GuiCrafting) {
            animatedBook = this.replaceButton((GuiCrafting) gui, 6, 49);
        } else if (gui instanceof GuiInventory) {
            animatedBook = this.replaceButton((GuiInventory) gui, 105, 22);
        }

        if (animatedBook == null) {
            return;
        }

        // from https://stackoverflow.com/questions/1196586/calling-remove-in-foreach-loop-in-java
        List<GuiButton> buttonList = evt.getButtonList();
        Iterator<GuiButton> iterator = buttonList.iterator();
        boolean flag = false;
        while (iterator.hasNext()) {
            GuiButton button = iterator.next();
            if (button.id == 10 && button.visible) {
                iterator.remove();
                flag = true;
            }
        }

        if (flag) {
            buttonList.add(animatedBook);
        }

    }

    private GuiButtonBook replaceButton(GuiContainer gui, int x, int y) {

        GuiButtonBook animatedBook = new GuiButtonBook(10, gui.getGuiLeft() + x, gui.height / 2 - y);
        ReflectionHelper.setRecipeButton(gui, animatedBook);
        return animatedBook;

    }

}
