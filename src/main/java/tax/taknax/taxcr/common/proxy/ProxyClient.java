package tax.taknax.taxcr.common.proxy;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import tax.taknax.taxcr.client.ClientPlayerHandler;

/**
 * Created by Creysys on 21 Mar 16.
 */
public class ProxyClient extends ProxyServer
{	
	public static KeyBinding recipeKey;
	public static KeyBinding usageKey;
	
	@Override
	public void registerKeyBinds()
	{
		recipeKey = new KeyBinding("key.guidebook.recipe", Keyboard.KEY_R, "key.categories.guidebook");
		usageKey = new KeyBinding("key.guidebook.usage", Keyboard.KEY_U, "key.categories.guidebook");
		
		ClientRegistry.registerKeyBinding(recipeKey);
		ClientRegistry.registerKeyBinding(usageKey);
	}
	
	@Override
	public void registerHandlers()
	{
		super.registerHandlers();
		
		MinecraftForge.EVENT_BUS.register(new ClientPlayerHandler());
	}	
}
