package me.Zonr0.PlayerProfiles;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.logging.Logger;
import lib.PatPeter.SQLibrary.*;

//comment

public class PlayerProfiles extends JavaPlugin {

	public SQLite dbManager;
	public ProfileHandler pHandler = new ProfileHandler(this);
	public String logPrefix = "[PlayerProfiles]";
	public File pFolder = new File("plugins/PlayerProfiles");
	
	Logger log = Logger.getLogger("Minecraft");
	
	private final PlayerProfilesPlayerListener playerListener = new PlayerProfilesPlayerListener(this);
	
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
		log.info("PlayerProfiles has been enabled!");
		
		createPluginFolder();
		
		String TableCreationQuery = "CREATE TABLE profiles ( 'id' INTEGER PRIMARY KEY, 'username' VARCHAR(30) NOT NULL, 'realname' VARCHAR(80), 'aliases' VARCHAR(120), 'twitteraccount' VARCHAR(30), 'origin' VARCHAR(255), 'bio' TEXT, 'firstregistered' TIMESTAMP NOT NULL, 'lastseen' TIMESTAMP);";
		
		dbManager = new SQLite(this.log, this.logPrefix, "profiles", pFolder.getPath());
		dbManager.open();
		
		if (dbManager.checkTable("profiles"))
		{
			log.info("PlayerProfiles: Profile table found, skipping creation.");
		}
		else
		{
			log.info("PlayerProfiles: Profiles table not found, creating.");
			
			if(dbManager.createTable(TableCreationQuery))
			{
				log.info("PlayerProfiles: Table creation succesful");
			}
			else
			{
				log.info("PlayerProfiles: Table failed to initialize");
			}
		}
	}
	
	public void onDisable() {
		if (dbManager != null) {
			dbManager.close();
		}
		log.info("PlayerProfiles has been disabled.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		String viewTarget;
		Player user = (Player)sender;
		
		if(cmd.getName().equalsIgnoreCase("debug"))
		{ // If the player typed /debug then do the following...
			log.info("Debug information goes here.");
			return true;
		}
		//Main PlayerProfiles Commands
		else if (cmd.getName().equalsIgnoreCase("profile")) 
		{
			String fullText = grabText(args);
			//****
			//View
			//****
			if (args[0].equalsIgnoreCase("view"))
			{
				viewTarget = args[1];
				try
				{
				pHandler.viewPlayer(user, viewTarget);
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
			//****
			//Name
			//****
			else if (args[0].equalsIgnoreCase("name"))
			{
				try
				{
				pHandler.updateName(user, fullText);
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
			//****
			//alias
			//****
			else if (args[0].equalsIgnoreCase("alias"))
			{
				try
				{
				pHandler.updateAlias(user, fullText);
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
			//*******
			//Twitter
			//*******
			else if (args[0].equalsIgnoreCase("twitter"))
			{
				try
				{
				pHandler.updateTwitter(user, fullText);
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
			//****
			//From
			//****
				else if (args[0].equalsIgnoreCase("from"))
				{
					try
					{
					pHandler.updateFrom(user, fullText);
					} catch (SQLException e)
					{
						e.printStackTrace();
					}
				}
			//***
			//Bio
			//***
				else if (args[0].equalsIgnoreCase("bio"))
				{
					try
					{
					pHandler.updateBio(user, fullText);
					} catch (SQLException e)
					{
						e.printStackTrace();
					}
				}
			//****
			//List
			//****
				else if (args[0].equalsIgnoreCase("list"))
				{
					try
					{
					pHandler.getSmallPlayerList(user);
					} catch (SQLException e)
					{
						e.printStackTrace();
					}
				}
			//Help
				else if (args[0].equalsIgnoreCase("help"))
				{
					user.sendMessage("PlayerProfiles is a system for keeping track of various pieces of personal information. " +
				                     "It is designed for simple use, and you will be automatically registered upon first login. " +
									 "Available commands are listed below. Please remember that this plugin is a work in progress," +
				                      "so if you find any bugs, please feel free to message me.");
					
					user.sendMessage("/profile name - Your real name.");
					user.sendMessage("/profile alias - Other usernames/aliases you are known by.");
					user.sendMessage("/profile twitter - Your twitter account");
					user.sendMessage("/profile from - Where you are coming from, how you know about this server. ie: personal friend, etc.");
					user.sendMessage("/profile bio - A short description of who you are.");
					user.sendMessage("There are other commands available to you as well:");
					user.sendMessage("/profile view <username> - View a user's profile.");
					user.sendMessage("/profile list - List the last ten registered users seen.");
					user.sendMessage("/profile help - Display this message.");
					user.sendMessage("/profile about - Display plugin information.");
				}
				else if (args[0].equalsIgnoreCase("about"))
				{
					user.sendMessage("PlayerProfiles by Zonr_0, uses PatPeter's SQLLite/MySQL bukkit wrapper.");
				}
			else
			{
				sender.sendMessage(ChatColor.RED + "/Profile register|view|name|twitter|from|bio|list|help|about");
			}
			return true;
		}
		 //If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
		return false; 
	}
	
	public void createPluginFolder() {
		if (!this.pFolder.exists()) {
			pFolder.mkdir();
		}
	}
	private String grabText(String[] args)
	{
		String output = "";
		for (int i = 1; i < args.length; i++)
		{
			output = output + args[i] + " ";
		}
		return output;
	}
}