package tax.taknax.taxcr.common;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by Creysys on 21 Mar 16.
 */
public class PlayerEventHandler
{
	@SubscribeEvent
	public void firstJoin(PlayerEvent.PlayerLoggedInEvent event)
	{
		// Adding book on spawn
		//
		// if (event.player.world.isRemote)
		// return;
		// if (!event.player.getEntityData().getBoolean("joinedBefore"))
		// {
		// event.player.getEntityData().setBoolean("joinedBefore", true);
		// event.player.inventory.addItemStackToInventory(new
		// ItemStack(ModItems.guideBook));
		// }
	}
}
