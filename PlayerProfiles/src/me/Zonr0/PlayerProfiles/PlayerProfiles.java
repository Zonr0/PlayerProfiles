package me.Zonr0.PlayerProfiles;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

//comment

public class PlayerProfiles extends JavaPlugin {

	
	Logger log = Logger.getLogger("Minecraft");
	
	private final PlayerProfilesPlayerListener playerListener = new PlayerProfilesPlayerListener(this);
	
	public void onEnable() {
		log.info("PlayerProfiles has been enabled!");
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Event.Priority.Normal, this);
	}
	
	public void onDisable() {
		log.info("PlayerProfiles has been disabled.");
	}
	
}