package me.Zonr0.PlayerProfiles;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;


public class PlayerProfiles extends JavaPlugin {

	
	Logger log = Logger.getLogger("Minecraft");
	
	public void onEnable() {
		log.info("PlayerProfiles has been enabled!");
		//EnableDebug();
	}
	
	public void onDisable() {
		log.info("PlayerProfiles has been disabled.");
	}
	
}