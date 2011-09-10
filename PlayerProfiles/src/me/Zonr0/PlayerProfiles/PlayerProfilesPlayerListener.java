package me.Zonr0.PlayerProfiles;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerProfilesPlayerListener extends PlayerListener {
	
	private static PlayerProfiles plugin;
	
	public PlayerProfilesPlayerListener(PlayerProfiles instance) {
		plugin = instance;
	}

	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player joiningPlayer = event.getPlayer();
		try
		{
			if (!plugin.pHandler.isRegistered(joiningPlayer))
			{
				String nagMessage = ChatColor.RED + "Welcome!" + ChatColor.WHITE + " This server is running PlayerProfiles";
				nagMessage = nagMessage + " When you get the chance, please take the time to fill out some of the profile fields.";
				nagMessage = nagMessage + "Please type " + ChatColor.RED + "/profile help" + ChatColor.WHITE + " for more details.";
				joiningPlayer.sendMessage(nagMessage);
				plugin.pHandler.registerPlayer(joiningPlayer);
				return;
			}
			plugin.pHandler.updateLastSeen(joiningPlayer);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player leavingPlayer = event.getPlayer();
		try
		{
			if (plugin.pHandler.isRegistered(leavingPlayer))
			{
				plugin.pHandler.updateLastSeen(leavingPlayer);
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
