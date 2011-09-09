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
		
		String TableCreationQuery = "CREATE TABLE profiles ( 'id' INTEGER PRIMARY KEY, 'username' VARCHAR(30) NOT NULL, 'realname' VARCHAR(80), 'twitteraccount' VARCHAR(30), 'from' VARCHAR(255), 'bio' TEXT, 'firstregistered' TIMESTAMP NOT NULL, 'lastseen' TIMESTAMP);";
		
		dbManager = new SQLite(this.log, this.logPrefix, "profiles", pFolder.getPath());
		dbManager.open();
		
		if(dbManager.createTable(TableCreationQuery))
		{
			log.info("Table creation succesful");
		}
		else
		{
			log.info("Table failed to initialize");
		}
	}
	
	public void onDisable() {
		if (dbManager != null) {
			dbManager.close();
		}
		log.info("PlayerProfiles has been disabled.");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
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
			//********
			//Register
			//********
			if (args[0].equalsIgnoreCase("register"))
			{
				try 
				{
					pHandler.registerPlayer(user);
					
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
			//****
			//View
			//****
			else if (args[0].equalsIgnoreCase("view"))
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
				String fullName = "";
				for (int i = 1; i < args.length; i++)
				{
					fullName = fullName + args[i];
				}
				try
				{
				pHandler.updateName(user, fullName);
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
				String fullTwitter = "";
				for (int i = 1; i < args.length; i++)
				{
					fullTwitter = fullTwitter + args[i];
				}
				try
				{
				pHandler.updateTwitter(user, fullTwitter);
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
					String fullFrom = "";
					for (int i = 1; i < args.length; i++)
					{
						fullFrom = fullFrom + args[i];
					}
					try
					{
					pHandler.updateFrom(user, fullFrom);
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
					String fullBio = "";
					for (int i = 1; i < args.length; i++)
					{
						fullBio = fullBio + args[i];
					}
					try
					{
					pHandler.updateBio(user, fullBio);
					} catch (SQLException e)
					{
						e.printStackTrace();
					}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "/Profile register|view|name|twitter|from||bio|list|fulllist|help");
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
}