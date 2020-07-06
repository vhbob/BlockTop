package com.vhbob.blocktop.events;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.vhbob.blocktop.contests.Contest;

public class EditRewards implements Listener {

	public static HashMap<Player, Integer> editingPlace;
	public static HashMap<Player, Contest> editingContest;

	public EditRewards() {
		if (editingPlace == null)
			editingPlace = new HashMap<Player, Integer>();
		if (editingContest == null)
			editingContest = new HashMap<Player, Contest>();
	}

	@EventHandler
	public void onEdit(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (editingContest.containsKey(p)) {
			if (editingContest.get(p).getRewards().get(editingPlace.get(p)) != null) {
				editingContest.get(p).getRewards().get(editingPlace.get(p)).clear();
			}
			for (ItemStack item : e.getInventory().getContents()) {
				if (item != null)
					editingContest.get(p).addReward(item, editingPlace.get(p));
			}
			p.sendMessage(ChatColor.GREEN + "Set the rewards!");
			editingContest.remove(p);
			editingPlace.remove(p);
		}
	}

}
