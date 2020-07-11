package me.felipe.wizly.rankup;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import me.felipe.wizly.rankup.events.ChatL;
import me.felipe.wizly.rankup.events.Events;
import me.felipe.wizly.rankup.utils.Msg;
import me.felipe.wizly.rankup.utils.SQL;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	
	private static Main instance;
	public static SQL sql;
	public static HashSet<Rank> ranks = new HashSet<>();
	public static List<Rank> rank_list = new ArrayList<Rank>();
	public static HashMap<Integer, Rank> rank_pos = new HashMap<>();
	public static HashMap<String, RankPlayer> players = new HashMap<>();
	public static Economy economy;
	
	public void onEnable(){
		instance = this;
		
		saveDefaultConfig();
		
		sql = new SQL();
		loadRanks();
		loadPlayerRanks();
		setupEconomy();
		
		getServer().getPluginManager().registerEvents(new Events(), this);
		getServer().getPluginManager().registerEvents(new ChatL(), this);
		
		debug("Plugin habilitado.");
	}
	
	public void debug(String msg){
		Bukkit.getConsoleSender().sendMessage("§b[WizlyRanks] " + msg);
	}

	public static Main getInstance(){
		return instance;
	}
	public void loadPlayerRanks(){
		try {
			Connection c = sql.getNewConnection();
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Ranks");
			int i = 0;
			while(rs.next()){
				String player = rs.getString("Player");
				String rank = rs.getString("Rank");
				RankPlayer rp = new RankPlayer(player);
				
				int position = Integer.valueOf(rank.split(":")[1]);
				
				if (rank_pos.containsKey(position)){
					Rank r = rank_pos.get(position);
					rp.setRank(r);
					players.put(rp.getPlayer(), rp);
					i++;
				}
			}
			Main.getInstance().debug("Foram carregados " + i + " usuarios.");
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	public void loadRanks(){
		int i = 0;
		for (String a : getConfig().getConfigurationSection("ranks").getKeys(false)){
			Rank r = new Rank(a, getConfig().getInt("ranks." + a + ".rankup_cost"), 
					getConfig().getString("ranks." + a + ".display_name")
					.replaceAll("&", "§"), getConfig().getInt("ranks." + a + ".posicao"),
					getConfig().getString("ranks." + a + ".pex_group"));
			ranks.add(r);
			rank_list.add(r);
			rank_pos.put(r.getPosition(), r);
			i++;
		}
		Collections.sort(rank_list, new Comparator<Rank>() {
			
			@Override
			public int compare(Rank c1, Rank c2){
				
				return (int) (c1.getCusto() - c2.getCusto());
				
			}
		});
		debug("Foram carregados " + i + " ranks.");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender instanceof Player){
			Player p = (Player) sender;
			if (label.equalsIgnoreCase("ranks")){
				p.sendMessage(Msg.get("rank_cab"));
				p.sendMessage("");
				for (Rank r : rank_list){
					p.sendMessage(Msg.get("rank_list")
							.replace("@rank", r.getDisplayName())
							.replace("@custo", r.getCusto() + ""));
				}
				p.sendMessage("");
			}
			if (label.equalsIgnoreCase("rankup")){
				if (players.containsKey(p.getName())){
					RankPlayer rp = players.get(p.getName());
					if (rp.getRank() != null){
						Rank r = rp.getRank();
						int next = r.getPosition() + 1;
						if (rank_pos.containsKey(next)){
							Rank prox = rank_pos.get(next);
							if (economy.getBalance(p) < prox.getCusto()){
								p.sendMessage(Msg.get("insuficient_money"));
							} else {
								economy.withdrawPlayer(p, prox.getCusto());
								p.playSound(p.getLocation(), Sound.LEVEL_UP, 10, 1);
								rp.setRank(prox);
								rp.update();
								rp.save();
								p.sendMessage(Msg.get("evoluiu").replace("@rank", prox.getDisplayName()));
								if (getConfig().getBoolean("rankup.anunciar"))
									Bukkit.broadcastMessage(getConfig().getString("rankup.anuncio")
											.replace("@player", p.getName()).replace("@rank", r.getDisplayName())
											.replaceAll("&", "§"));
							}
						} else {
							p.sendMessage(Msg.get("max"));
						}
					} else {
						int rank = 1;
						if (rank_pos.containsKey(rank)){
							Rank r = rank_pos.get(rank);
							if (economy.getBalance(p) < r.getCusto()){
								p.sendMessage(Msg.get("insuficient_money"));
							} else {
								economy.withdrawPlayer(p, r.getCusto());
								rp.setRank(r);
								rp.update();
								rp.save();
								p.sendMessage(Msg.get("evoluiu").replace("@rank", r.getDisplayName()));
								if (getConfig().getBoolean("rankup.anunciar"))
									Bukkit.broadcastMessage(getConfig().getString("rankup.anuncio")
											.replace("@player", p.getName()).replace("@rank", r.getDisplayName())
											.replaceAll("&", "§"));
							}
						} else {
							p.sendMessage(Msg.get("unknown"));
						}
					}
				}
			}
		}
		return super.onCommand(sender, command, label, args);
	}
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
}

