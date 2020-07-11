package me.felipe.wizly.rankup.events;

import me.felipe.wizly.rankup.Main;
import me.felipe.wizly.rankup.RankPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Events implements Listener{
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		if (Main.players.containsKey(p.getName())){
			RankPlayer rp = Main.players.get(p.getName());
			rp.update();
		} else {
			RankPlayer rp = new RankPlayer(p.getName());
			Main.players.put(p.getName(), rp);
			rp.save();
			rp.update();
		}
	}
}