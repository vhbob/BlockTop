package com.vhbob.blocktop.events;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.vhbob.blocktop.BlockTopPlugin;

public class ClickInInv implements Listener {

	public static ArrayList<Player> clicking;

	public ClickInInv() {
		if (clicking == null)
			clicking = new ArrayList<Player>();
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (clicking.contains(p)) {
			e.setCancelled(true);
			if (e.getInventory() == p.getOpenInventory().getTopInventory()) {
				// Check if contest was clicked
				if (BlockTopPlugin.daily != null
						&& e.getSlot() == BlockTopPlugin.instance.getConfig().getInt("inventory-items.daily.slot")) {
					p.sendMessage(ChatColor.GREEN + "-=Top Ten Miners=-");
					HashMap<String, Long> topTen = BlockTopPlugin.daily.getTopTen();
					for (String name : topTen.keySet())
						p.sendMessage(ChatColor.GRAY + name + ": " + topTen.get(name));
					p.closeInventory();
				} else if (BlockTopPlugin.weekly != null
						&& e.getSlot() == BlockTopPlugin.instance.getConfig().getInt("inventory-items.weekly.slot")) {
					p.sendMessage(ChatColor.GREEN + "-=Top Ten Miners=-");
					HashMap<String, Long> topTen = BlockTopPlugin.weekly.getTopTen();
					for (String name : topTen.keySet())
						p.sendMessage(ChatColor.GRAY + name + ": " + topTen.get(name));
					p.closeInventory();
				} else if (BlockTopPlugin.monthly != null
						&& e.getSlot() == BlockTopPlugin.instance.getConfig().getInt("inventory-items.monthly.slot")) {
					p.sendMessage(ChatColor.GREEN + "-=Top Ten Miners=-");
					HashMap<String, Long> topTen = BlockTopPlugin.monthly.getTopTen();
					for (String name : topTen.keySet())
						p.sendMessage(ChatColor.GRAY + name + ": " + topTen.get(name));
					p.closeInventory();
				} else if (BlockTopPlugin.custom != null
						&& e.getSlot() == BlockTopPlugin.instance.getConfig().getInt("inventory-items.custom.slot")) {
					p.sendMessage(ChatColor.GREEN + "-=Top Ten Miners=-");
					HashMap<String, Long> topTen = BlockTopPlugin.custom.getTopTen();
					for (String name : topTen.keySet())
						p.sendMessage(ChatColor.GRAY + name + ": " + topTen.get(name));
					p.closeInventory();
				} else if (e.getSlot() == BlockTopPlugin.instance.getConfig().getInt("inventory-items.claim.slot")) {
					Inventory claimInv = Bukkit.createInventory(p, 54, "Shift Click Items to Claim!");
					if (BlockTopPlugin.itemsToGive.containsKey(p.getUniqueId())) {
						for (ItemStack item : BlockTopPlugin.itemsToGive.get(p.getUniqueId())) {
							claimInv.addItem(item);
						}
					}
					ClaimItem.addClaiming(p);
					ClaimItem.addClaiming(p);

					p.openInventory(claimInv);
				}
			}
		}
	}

	@EventHandler
	public void onInvClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (clicking.contains(p)) {
			clicking.remove(p);
		}
	}

}
