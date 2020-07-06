package com.vhbob.blocktop.events;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.vhbob.blocktop.BlockTopPlugin;

public class ClaimItem implements Listener {

	private static ArrayList<Player> claiming;

	public ClaimItem() {
		if (claiming == null)
			claiming = new ArrayList<Player>();
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (claiming.contains(p)) {
			if (e.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY
					|| e.getClickedInventory() != p.getOpenInventory().getTopInventory()) {
				p.sendMessage(ChatColor.RED + "You must shift click to claim items");
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (claiming.contains(p)) {
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			for (ItemStack item : e.getInventory().getContents()) {
				if (item != null)
					items.add(item);
			}
			if (!items.isEmpty())
				BlockTopPlugin.itemsToGive.put(p.getUniqueId(), items);
			else if (BlockTopPlugin.itemsToGive.containsKey(p.getUniqueId())) {
				BlockTopPlugin.itemsToGive.get(p.getUniqueId()).clear();
			}

			claiming.remove(p);
		}
	}

	public static void addClaiming(Player p) {
		claiming.add(p);
	}

}
