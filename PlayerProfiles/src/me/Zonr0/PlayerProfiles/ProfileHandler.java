package me.Zonr0.PlayerProfiles;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ProfileHandler {
	public PlayerProfiles plugin;
	
	public ProfileHandler(PlayerProfiles instance) {
		this.plugin = instance;
	}

	public void registerPlayer(Player sender) throws SQLException
	{
		String sizeQuery = "SELECT COUNT(*) as count FROM profiles WHERE username='" + sender.getName() + "'";
		String registerQuery = "INSERT INTO profiles (username, firstregistered, lastseen) VALUES (" + "'" + sender.getName() + "', datetime('now'), datetime('now'))";
		
		ResultSet result = plugin.dbManager.query(sizeQuery);
		
		if (result.getInt("count") > 0)
		{
			sender.sendMessage(ChatColor.RED + "Unable to register. Record already exists in the database.");
			return;
		}
		plugin.dbManager.query(registerQuery);
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
		String alias;
		
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
		from = result.getString("origin");
		alias = result.getString("aliases");
		bio = result.getString("bio");
		registerDate = convertDate(result.getString("firstregistered"));
		lastSeenDate = convertDate(result.getString("lastseen"));
		
		String nameLine = ChatColor.RED + "Name: " + ChatColor.WHITE + viewTarget;
		if (realName != null)
		{
			nameLine = nameLine + " aka " + realName;
		}
		requester.sendMessage(nameLine);
		if (alias != null)
		{
			requester.sendMessage(ChatColor.RED + "Other Aliases:" + ChatColor.WHITE + alias);
		}
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
		String updateQuery = "UPDATE profiles SET lastSeen=datetime('now') WHERE username='" + target.getName() + "'";
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
		String updateQuery = "UPDATE profiles SET origin='" + sanFrom + "' WHERE username='" + target.getName() + "'";
		plugin.dbManager.query(updateQuery);
	}
	public void updateBio(Player target, String bio) throws SQLException
	{
		String sanBio = sanitizeInput(bio);
		String updateQuery = "UPDATE profiles SET bio='" + sanBio + "' WHERE username='" + target.getName() + "'";
		plugin.dbManager.query(updateQuery);
	}
	public void updateAlias(Player target, String aliases) throws SQLException
	{
		String sanAlias = sanitizeInput(aliases);
		String updateQuery = "UPDATE profiles SET aliases='" + sanAlias + "' WHERE username='" + target.getName() + "'";
		plugin.dbManager.query(updateQuery);
	}
	public void getSmallPlayerList(Player target) throws SQLException
	{
		//TODO: Refactor the date field to use SQL dates instead of java dates.
		int LIST_SIZE = 10;
		int NAME_BUFFER = 25;
		
		StringBuilder profileLine = new StringBuilder();
		String output;
		
		String getQuery = "SELECT username,lastseen  FROM profiles ORDER BY lastseen DESC";
		ResultSet results = plugin.dbManager.query(getQuery);
		
		int readNames = 0;
		target.sendMessage(ChatColor.RED + "Last 10 registered players seen.");
		target.sendMessage(ChatColor.RED + "---------------------------------------------");
		while(results.next() && readNames < LIST_SIZE)
		{
			profileLine.append(results.getString("username"));
			while(profileLine.length() < NAME_BUFFER)
			{
				profileLine.append(' ');
			}
			profileLine.append(convertDate(results.getString("lastseen")));
			output = profileLine.toString();
			target.sendMessage(output);
			profileLine.delete(0, profileLine.length());
			//profileLine.append(workStamp.toString());
			readNames++;
		}

		
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
			}
			else
			{
				output = output + raw.charAt(i);
			}
		}
		
		return output;
	}
	private String convertDate(String dbDate)
	{
		String pattern = "yyyy-MM-dd H:mm:ss";
		Date prettyDate;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		try 
		{
			prettyDate = format.parse(dbDate);
			return prettyDate.toString();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "ERROR: Date Conversion Error";
	}
}
