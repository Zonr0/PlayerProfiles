package me.Zonr0.PlayerProfiles;

import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import lib.PatPeter.SQLibrary.*;

public class PlayerProfilesPlayerListener extends PlayerListener {
	
	public static PlayerProfiles plugin;
	
	public PlayerProfilesPlayerListener(PlayerProfiles instance) {
		plugin = instance;
	}

	public void OnPlayerJoin(PlayerJoinEvent event)
	{
		Player joiningPlayer = event.getPlayer();
		try
		{
			if (!plugin.pHandler.isRegistered(joiningPlayer))
			{
				joiningPlayer.sendMessage(ChatColor.RED + "Welcome!" + ChatColor.WHITE + " If you intend to stay and play on this server, please take the time to register your username");
				joiningPlayer.sendMessage("using " + ChatColor.RED + "/profile register." + ChatColor.WHITE + " When you get the chance, take the time to fill out any relevent fields as well.");
				joiningPlayer.sendMessage("Please type " + ChatColor.RED + "/profile help" + ChatColor.WHITE + " for more details.");
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
