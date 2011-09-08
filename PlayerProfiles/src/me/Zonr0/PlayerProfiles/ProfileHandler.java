package me.Zonr0.PlayerProfiles;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import lib.PatPeter.SQLibrary.*;

public class ProfileHandler {
	public PlayerProfiles plugin;
	
	public ProfileHandler(PlayerProfiles instance) {
		this.plugin = instance;
	}

	public void registerPlayer(CommandSender sender) throws SQLException
	{
		Date currentDate = new Date();
		String checkQuery = "SELECT username FROM profiles where username='" + sender.getName() + "'";
		String registerQuery = "INSERT INTO profiles (username, firstregistered, lastseen) VALUES (" + sender.getName() + "," + currentDate.toString() + "," + currentDate.toString();
		
		ResultSet result = plugin.dbManager.query(checkQuery);
		
		if (result.getString("username") != null)
		{
			sender.sendMessage(ChatColor.RED + "Unable to register. Record already exists in the database.");
			return;
		}
		plugin.dbManager.query(registerQuery);
		sender.sendMessage(ChatColor.RED + "Registered Succesfully!");
	}
	
	public void viewPlayer(CommandSender requester, String viewTarget) throws SQLException
	{
		
		String realName;
		String twitterAccount;
		String from;
		String bio;
		String registerDate;
		String lastSeenDate;
		
		String viewQuery = "SELECT as viewData FROM profiles WHERE username='" + viewTarget + "'";
		ResultSet result = plugin.dbManager.query(viewQuery);
		
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
}
