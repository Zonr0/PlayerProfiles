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
				String nagMessage = ChatColor.RED + "Welcome!" + ChatColor.WHITE + " If you intend to stay and play on this server, please take the time to register your username";
				nagMessage = nagMessage + "using " + ChatColor.RED + "/profile register." + ChatColor.WHITE + " When you get the chance, take the time to fill out any relevant fields as well.";
				nagMessage = nagMessage + "Please type " + ChatColor.RED + "/profile help" + ChatColor.WHITE + " for more details.";
				joiningPlayer.sendMessage(nagMessage);
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
