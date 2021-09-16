package tax.taknax.taxcr;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import tax.taknax.taxcr.api.RecipeManager;
import tax.taknax.taxcr.client.gui.GuiGuideBook;
import tax.taknax.taxcr.common.GuiBookContainer;
import tax.taknax.taxcr.common.proxy.ProxyServer;
import tax.taknax.taxcr.network.message.MessagePutItemsInWorkbench;
import tax.taknax.taxcr.plugin.vanilla.PluginVanilla;

@Mod(modid = GuideBookMod.MODID, name = GuideBookMod.NAME, version = GuideBookMod.VERSION, acceptedMinecraftVersions = "[1.12.2]", dependencies = "required-after:taxlc@[1.0.0,);")
@EventBusSubscriber(modid = GuideBookMod.MODID)
public class GuideBookMod
{
    public static class GuiId
    {
        public static final int GuideBook = 0;
    }
    
    public class GuiHandler implements IGuiHandler
    {
        @Override
        public Object getServerGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3)
        {
            switch (i)
            {
                case GuiId.GuideBook:
                    return new GuiBookContainer();
            }
            
            return null;
        }
        
        @Override
        public Object getClientGuiElement(int i, EntityPlayer entityPlayer, World world, int i1, int i2, int i3)
        {
            switch (i)
            {
                case GuiId.GuideBook:
                    return new GuiGuideBook(Minecraft.getMinecraft().currentScreen);
            }
            
            return null;
        }
    }
    
    public static final String MODID = "taxcr";
    public static final String NAME = "Tax' Cryptex Reader";
    public static final String VERSION = "2.0.0";
    
    @Mod.Instance
    public static GuideBookMod instance;
    
    public static SimpleNetworkWrapper network;
    public final GuiHandler guiHandler = new GuiHandler();
    
    @SidedProxy(serverSide = "tax.taknax.taxcr.common.proxy.ProxyServer", clientSide = "tax.taknax.taxcr.common.proxy.ProxyClient")
    public static ProxyServer proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModItems.init();
        
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        network.registerMessage(MessagePutItemsInWorkbench.Handler.class, MessagePutItemsInWorkbench.class, 0, Side.SERVER);
        
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, guiHandler);
        
        if (event.getSide() == Side.CLIENT)
        {
            PluginVanilla.preInit();
        }
        
        proxy.registerHandlers();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerKeyBinds();
        proxy.registerModels();
    }
    
    @EventHandler
    public void postIinit(FMLPostInitializationEvent event)
    {
        if (event.getSide() == Side.CLIENT)
        {
            PluginVanilla.postInit();
            RecipeManager.load();
        }
    }
}