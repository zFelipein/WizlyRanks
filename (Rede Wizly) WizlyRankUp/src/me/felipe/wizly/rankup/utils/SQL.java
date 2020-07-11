package me.felipe.wizly.rankup.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import me.felipe.wizly.rankup.Main;

public class SQL {
	
	public SQL(){
		try {
			Connection c;
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:plugins/WizlyRanks/storage.db");
			Statement stmt = c.createStatement();
			stmt.execute("CREATE TABLE IF NOT EXISTS Ranks (Player TEXT, Rank TEXT)");
			Main.getInstance().debug("§aConexao ao SQLite estabelecida.");
			c.close();
			stmt.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public Connection getNewConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:plugins/WizlyRanks/storage.db");
	}
}