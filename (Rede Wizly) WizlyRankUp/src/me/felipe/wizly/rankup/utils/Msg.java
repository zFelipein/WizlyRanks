package me.felipe.wizly.rankup.utils;

import org.bukkit.ChatColor;

import me.felipe.wizly.rankup.Main;

public class Msg {
	
	public static String get(String path){
		return ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("mensagens." + path)
				);
	}

}