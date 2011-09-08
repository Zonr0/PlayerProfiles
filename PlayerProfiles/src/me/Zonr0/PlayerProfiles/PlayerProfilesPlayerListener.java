package me.Zonr0.PlayerProfiles;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerListener;

import lib.PatPeter.SQLibrary.*;

public class PlayerProfilesPlayerListener extends PlayerListener {
	
	public static PlayerProfiles plugin;
	private Logger log = Logger.getLogger("Minecraft");
	
	public PlayerProfilesPlayerListener(PlayerProfiles instance) {
		plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(cmd.getName().equalsIgnoreCase("debug")){ // If the player typed /basic then do the following...
			log.info("Debug information goes here.");
			return true;
		} //If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
		return false; 
	}

}
