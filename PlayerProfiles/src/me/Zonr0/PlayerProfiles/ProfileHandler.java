package me.Zonr0.PlayerProfiles;

import java.util.List;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.Timestamp;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.parser.ParseException;

public class ProfileHandler {
	public PlayerProfiles plugin;
	
	public ProfileHandler(PlayerProfiles instance) {
		this.plugin = instance;
	}

	public void registerPlayer(Player sender) throws SQLException
	{
		Timestamp currentDate = new Timestamp(0);
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
	
	public void updateName(Player target, String name) throws SQLException
	{
		String sanName = sanitizeInput(name);
		String updateQuery = "UPDATE profiles SET realname='" + sanName + "' WHERE username='" + target.getName() + "'";
		plugin.dbManager.query(updateQuery);
	}
	public void updateTwitter(Player target, String twitter) throws SQLException
	{
		String santwit = sanitizeInput(twitter);
		String updateQuery = "UPDATE profiles SET twitteraccount='" + santwit + "' WHERE username='" + target.getName() + "'";
		plugin.dbManager.query(updateQuery);
	}
	public void updateFrom(Player target, String from) throws SQLException
	{
		String sanFrom = sanitizeInput(from);
		String updateQuery = "UPDATE profiles SET from='" + sanFrom + "' WHERE username='" + target.getName() + "'";
		plugin.dbManager.query(updateQuery);
	}
	public void updateBio(Player target, String bio) throws SQLException
	{
		String sanBio = sanitizeInput(bio);
		String updateQuery = "UPDATE profiles SET bio='" + sanBio + "' WHERE username='" + target.getName() + "'";
		plugin.dbManager.query(updateQuery);
	}
	public void getSmallPlayerList(Player target) throws SQLException
	{
		//TODO: Refactor the date field to use SQL dates instead of java dates.
		int LIST_SIZE = 10;
		DateFormat format = DateFormat.getDateTimeInstance();
		List<Date> dateList = new ArrayList<Date>();
		String dateString;
		
		String getQuery = "SELECT username,lastseen  FROM profiles";
		ResultSet results = plugin.dbManager.query(getQuery);
		
		while(results.next())
		{
			dateString = results.getString("lastseen");
			try {
				dateList.add(format.parse(dateString));
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Collections.
		Collections.sort(dateList);
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
	
	private String sanitizeInput(String input)
	{
		String output = "";
		String raw = input;
		for (int i = 0; i < raw.length(); i++)
		{
			if (raw.charAt(i) == '\'')
			{
				output = output + '\'';
				output = output + raw.charAt(i);
				output = output + '\'';
			}
			else
			{
				output = output + raw.charAt(i);
			}
		}
		
		return output;
	}
}
