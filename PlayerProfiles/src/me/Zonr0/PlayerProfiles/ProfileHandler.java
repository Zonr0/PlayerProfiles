package me.Zonr0.PlayerProfiles;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ProfileHandler {
	public PlayerProfiles plugin;
	
	public ProfileHandler(PlayerProfiles instance) {
		this.plugin = instance;
	}

	public void registerPlayer(Player sender) throws SQLException
	{
		Date currentDate = new Date();
		String sizeQuery = "SELECT COUNT(*) as count FROM profiles WHERE username='" + sender.getName() + "'";
		String registerQuery = "INSERT INTO profiles (username, firstregistered, lastseen) VALUES (" + "'" + sender.getName() + "','" + currentDate.toString() + "','" + currentDate.toString() + "')";
		
		ResultSet result = plugin.dbManager.query(sizeQuery);
		
		if (result.getInt("count") > 0)
		{
			sender.sendMessage(ChatColor.RED + "Unable to register. Record already exists in the database.");
			return;
		}
		result = plugin.dbManager.query(registerQuery);
		sender.sendMessage(ChatColor.RED + "Registered Succesfully!");
	}
	
	public void viewPlayer(Player requester, String viewTarget) throws SQLException
	{
		
		String realName;
		String twitterAccount;
		String from;
		String bio;
		String registerDate;
		String lastSeenDate;
		
		String viewQuery = "SELECT * FROM profiles WHERE username='" + viewTarget + "'";
		String countQuery = "SELECT COUNT(*) as count FROM profiles WHERE username ='" + viewTarget + "'";
		
		ResultSet result = plugin.dbManager.query(countQuery);
		
		if (result.getInt("count") == 0)
		{
			requester.sendMessage(ChatColor.RED + "Empty database, no usernames to view.");
			return;
		}
		
		result = plugin.dbManager.query(viewQuery);
		
		if (result.getInt("id") == 0)
		{
			requester.sendMessage(ChatColor.RED + "Unregistered username");
			return;
		}
		realName = result.getString("realname");
		twitterAccount = result.getString("twitteraccount");
		from = result.getString("from");
		bio = result.getString("bio");
		registerDate = result.getString("firstregistered");
		lastSeenDate = result.getString("lastseen");
		
		String nameLine = ChatColor.RED + "Name: " + ChatColor.WHITE + viewTarget;
		if (realName != null)
		{
			nameLine = nameLine + " aka " + realName;
		}
		requester.sendMessage(nameLine);
		if (twitterAccount != null)
		{
		requester.sendMessage(ChatColor.RED + "Twitter: " + ChatColor.WHITE + twitterAccount);
		}
		if (from != null)
		{
		requester.sendMessage(ChatColor.RED + "From: " + ChatColor.WHITE + from);
		}
		if (bio != null)
		{
		requester.sendMessage(ChatColor.RED + "Bio: " + ChatColor.WHITE + bio);
		}
		requester.sendMessage(ChatColor.RED + "Registered on: " + ChatColor.WHITE + registerDate);
		requester.sendMessage(ChatColor.RED + "Last Seen on: " + ChatColor.WHITE + lastSeenDate);
		
	}
	
	public void updateLastSeen(Player target) throws SQLException
	{
		Date curTime = new Date();
		String updateQuery = "UPDATE profiles SET lastSeen='" + curTime.toString() + "' WHERE username='" + target.getName() + "'";
		plugin.dbManager.query(updateQuery);
	}
	
	public boolean isRegistered(Player target) throws SQLException
	{
		String viewQuery = "SELECT * FROM profiles WHERE username='" + target.getName() + "'";
		String countQuery = "SELECT COUNT(*) as count FROM profiles WHERE username ='" + target.getName() + "'";
		
		ResultSet result = plugin.dbManager.query(countQuery);
		
		if (result.getInt("count") == 0)
		{
			return false;
		}
		
		result = plugin.dbManager.query(viewQuery);
		
		if (result.getInt("id") == 0)
		{
			return false;
		}
		
		return true;
	}
	
	public boolean isRegistered(String target) throws SQLException
	{
		String viewQuery = "SELECT * FROM profiles WHERE username='" + target + "'";
		String countQuery = "SELECT COUNT(*) as count FROM profiles WHERE username ='" + target + "'";
		
		ResultSet result = plugin.dbManager.query(countQuery);
		
		if (result.getInt("count") == 0)
		{
			return false;
		}
		
		result = plugin.dbManager.query(viewQuery);
		
		if (result.getInt("id") == 0)
		{
			return false;
		}
		
		return true;
	}
}
