package me.felipe.wizly.rankup;

import me.felipe.wizly.rankup.Main;
import me.felipe.wizly.rankup.Player;
import me.felipe.wizly.rankup.Rank;

public class WizlyRanksAPI {
	
	public static Rank getRank(Player p){
		if (Main.players.containsKey(p.getName())){
			return Main.players.get(p.getName()).getRank();
		}
		return null;
	}
	public static void updatePlayer(Player p){
		if (Main.players.containsKey(p.getName())){
			Main.players.get(p.getName()).update();
		}
	}

}

