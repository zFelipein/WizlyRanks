package me.felipe.wizly.rankup;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.magma.vips.MagmaVipsAPI;

public class RankPlayer {
	
	private String player = "";
	private Rank rank = null;
	
	public RankPlayer(String playerName){
		this.player = playerName;
	}
	
	public void setRank(Rank r){
		this.rank = r;
	}
	public Rank getRank(){
		return this.rank;
	}
	
	public String getPlayer(){
		return this.player;
	}
	public void save(){
		try {
			Connection c = Main.sql.getNewConnection();
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Player FROM Ranks WHERE Player='" + player + "'");
			Statement exe = c.createStatement();
			if (rs.next()){
				exe.executeUpdate("UPDATE Ranks SET Rank='" + this.rank.getName() + ":" + rank.getPosition() + "' WHERE Player='" + player + "'");
			} else {
				if (rank != null)
					exe.execute("INSERT INTO Ranks (Player, Rank) VALUES ('" + player + "','" + rank.getName() + ":" + rank.getPosition() + "');");
				else {
					exe.execute("INSERT INTO Ranks (Player, Rank) VALUES ('" + player + "','" + "unrank:0" + "');");
				}
			}
			c.close();
			stmt.close();
			rs.close();
			exe.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	@SuppressWarnings("deprecation")
	public void update(){
		boolean hasVip = MagmaVipsAPI.hasVip(Bukkit.getPlayer(player));
		String default_group = Main.getInstance().getConfig().getString("default_group");
		if (hasVip){
			default_group = MagmaVipsAPI.getUsando(Bukkit.getPlayer(player));
		}
		String r = "null";
		if (getRank() != null)
			r = getRank().getGrupo();
		String[] unk = {default_group};
		String[] ranked = {default_group, r};
		if (r.equalsIgnoreCase("null")) {
			PermissionUser pex = PermissionsEx.getUser(player);
			pex.setGroups(unk);
		} else {
			
			PermissionUser pex = PermissionsEx.getUser(player);
			pex.setGroups(ranked);
			
		}
	}

}
