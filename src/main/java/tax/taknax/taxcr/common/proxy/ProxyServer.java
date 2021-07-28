package tax.taknax.taxcr.common.proxy;

import net.minecraftforge.common.MinecraftForge;
import tax.taknax.taxcr.common.PlayerEventHandler;

/**
 * Created by Creysys on 21 Mar 16.
 */
public class ProxyServer
{
	public void registerKeyBinds()
	{
	};
	
	public void registerHandlers()
	{
		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
	}
	
	public void registerModels()
	{
	}
}
