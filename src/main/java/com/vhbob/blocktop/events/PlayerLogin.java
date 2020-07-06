package com.vhbob.blocktop.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.vhbob.blocktop.BlockTopPlugin;
import com.vhbob.blocktop.datastorage.PlayerData;

public class PlayerLogin implements Listener {

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if (!BlockTopPlugin.totalBroke.containsKey(e.getPlayer().getUniqueId())) {
			PlayerData.loadPlayerData(e.getPlayer().getUniqueId());
		}
	}

}
