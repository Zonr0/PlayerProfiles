package me.Zonr0.PlayerProfiles;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;
import lib.PatPeter.SQLibrary.*;

//comment

public class PlayerProfiles extends JavaPlugin {

	public SQLite dbManager;
	public String logPrefix = "[PlayerProfiles]";
	public File pFolder = new File("plugins/PlayerProfiles");
	
	Logger log = Logger.getLogger("Minecraft");
	
	private final PlayerProfilesPlayerListener playerListener = new PlayerProfilesPlayerListener(this);
	
	public void onEnable() {
		log.info("PlayerProfiles has been enabled!");
		
		createPluginFolder();
		
		String TableCreationQuery = "CREATE TABLE profiles ( 'id' INTEGER PRIMARY KEY, 'username' VARCHAR(30) NOT NULL, 'realname' VARCHAR(80), twitteraccount' VARCHAR(30), 'from' VARCHAR(255), 'firstregistered' VARCHAR(255) NOT NULL, 'lastSeen' VARCHAR(255));";
		
		dbManager = new SQLite(this.log, this.logPrefix, "profiles", pFolder.getPath());
		dbManager.open();
		
		if(dbManager.createTable(TableCreationQuery))
		{
			log.info("Table creation succesful");
		}
		else
		{
			log.info("Table failed to initialize");
		}
		
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Event.Priority.Normal, this);
	}
	
	public void onDisable() {
		if (dbManager != null) {
			dbManager.close();
		}
		log.info("PlayerProfiles has been disabled.");
	}
	
	public void createPluginFolder() {
		if (!this.pFolder.exists()) {
			pFolder.mkdir();
		}
	}
}