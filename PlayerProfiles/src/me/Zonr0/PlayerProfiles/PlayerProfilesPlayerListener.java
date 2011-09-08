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

}
