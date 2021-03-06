package tax.taknax.taxcr.common.handler;

import tax.taknax.taxcr.GuideBookMod;
import tax.taknax.taxcr.common.util.EnumBookDesign;
import net.minecraftforge.common.config.Config;

@Config(modid = GuideBookMod.MODID)
public class ConfigBuildHandler {

	@Config.Name("Book Design")
	@Config.Comment("Choose a book design to be used for the animation.")
	public static EnumBookDesign bookDesign = EnumBookDesign.VANILLA;
	
}