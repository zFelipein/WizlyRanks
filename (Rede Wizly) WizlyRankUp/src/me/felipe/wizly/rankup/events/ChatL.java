package me.felipe.wizly.rankup.events;

import me.felipe.wizly.rankup.Main;
import me.felipe.wizly.rankup.RankPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;

public class ChatL implements Listener{
	
	@EventHandler
	public void onChat(ChatMessageEvent e){
		Player p = e.getSender();
		if (Main.players.containsKey(p.getName())){
			RankPlayer rp = Main.players.get(p.getName());
			String tag = "§7[Unranked]";
			if (rp.getRank() != null){
				tag = rp.getRank().getDisplayName();
			}
			if (e.getTags().contains("wizlyranks")){
				e.setTagValue("wizlyranks", tag);
			}
		}
	}

}
