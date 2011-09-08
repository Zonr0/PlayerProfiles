package me.Zonr0.PlayerProfiles;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Date;
import lib.PatPeter.SQLibrary.*;

public class ProfileHandler {
	public PlayerProfiles plugin;
	
	public ProfileHandler(PlayerProfiles instance) {
		this.plugin = instance;
	}

	public void registerPlayer(String playerName) throws SQLException
	{
		Date currentDate = new Date();
		String registerQuery = "INSERT INTO profiles (username, firstregistered, lastseen) VALUES (" + playerName + "," + currentDate.toString() + "," + currentDate.toString();
		plugin.dbManager.query(registerQuery);
	}
}
