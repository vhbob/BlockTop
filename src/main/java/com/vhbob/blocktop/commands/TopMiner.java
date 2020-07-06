package com.vhbob.blocktop.commands;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.vhbob.blocktop.BlockTopPlugin;
import com.vhbob.blocktop.events.ClickInInv;
import com.vhbob.blocktop.util.GenUtil;

public class TopMiner implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("topminer")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				// Create inventory
				int[] panes = { 1, 2, 3, 4, 5, 6, 7, 9, 10, 16, 17, 18, 26, 27, 28, 34, 35, 37, 38, 39, 40, 41, 42,
						43 };
				ItemStack corner = new ItemStack(Material.ENDER_PORTAL_FRAME);
				Inventory inv = Bukkit.createInventory(null, 45,
						ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "TopMiner");
				ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE);
				pane.setDurability((short) 10);
				ItemMeta cm = corner.getItemMeta();
				cm.setDisplayName(" ");
				corner.setItemMeta(cm);
				ItemMeta pm = pane.getItemMeta();
				pm.setDisplayName(" ");
				pane.setItemMeta(pm);
				for (int i = 0; i < panes.length; i++)
					inv.setItem(panes[i], pane);
				inv.setItem(0, corner);
				inv.setItem(8, corner);
				inv.setItem(36, corner);
				inv.setItem(44, corner);
				// Add head
				ItemStack head = new ItemStack(Material.SKULL_ITEM);
				SkullMeta sm = (SkullMeta) head.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(ChatColor.GRAY + "Blocks Broken: " + BlockTopPlugin.totalBroke.get(p.getUniqueId()));
				sm.setLore(lore);
				sm.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + p.getName());
				sm.setOwner(p.getName());
				head.setItemMeta(sm);
				inv.setItem(BlockTopPlugin.instance.getConfig().getInt("inventory-items.p-stats.slot"), head);
				// Add won items
				ItemStack claim = GenUtil.buildItem("inventory-items.claim");
				ItemMeta claimMeta = claim.getItemMeta();
				lore.clear();
				lore.add(ChatColor.GRAY + "Click me to see your rewards");
				claimMeta.setLore(lore);
				claim.setItemMeta(claimMeta);
				inv.setItem(BlockTopPlugin.instance.getConfig().getInt("inventory-items.claim.slot"), claim);
				// Add contest items
				ItemStack daily = GenUtil.buildItem("inventory-items.daily");
				ItemMeta dailyM = daily.getItemMeta();
				lore.clear();
				if (BlockTopPlugin.daily != null) {
					long minsDiff = ChronoUnit.MINUTES.between(LocalDateTime.now(), BlockTopPlugin.daily.getEndDate());
					long days = minsDiff / 1440;
					minsDiff %= 1440;
					long hrs = minsDiff / 60;
					minsDiff %= 60;
					lore.add(ChatColor.GRAY + "Ends in: " + days + "D " + hrs + "H " + minsDiff + "M");
				}
				dailyM.setLore(lore);
				daily.setItemMeta(dailyM);
				inv.setItem(BlockTopPlugin.instance.getConfig().getInt("inventory-items.daily.slot"), daily);

				ItemStack weekly = GenUtil.buildItem("inventory-items.weekly");
				ItemMeta weeklyM = weekly.getItemMeta();
				lore.clear();
				if (BlockTopPlugin.weekly != null) {
					long minsDiff = ChronoUnit.MINUTES.between(LocalDateTime.now(), BlockTopPlugin.weekly.getEndDate());
					long days = minsDiff / 1440;
					minsDiff %= 1440;
					long hrs = minsDiff / 60;
					minsDiff %= 60;
					lore.add(ChatColor.GRAY + "Ends in: " + days + "D " + hrs + "H " + minsDiff + "M");
				}
				weeklyM.setLore(lore);
				weekly.setItemMeta(weeklyM);
				inv.setItem(BlockTopPlugin.instance.getConfig().getInt("inventory-items.weekly.slot"), weekly);

				ItemStack monthly = GenUtil.buildItem("inventory-items.monthly");
				ItemMeta monthlyM = monthly.getItemMeta();
				lore.clear();
				if (BlockTopPlugin.monthly != null) {
					long minsDiff = ChronoUnit.MINUTES.between(LocalDateTime.now(),
							BlockTopPlugin.monthly.getEndDate());
					long days = minsDiff / 1440;
					minsDiff %= 1440;
					long hrs = minsDiff / 60;
					minsDiff %= 60;
					lore.add(ChatColor.GRAY + "Ends in: " + days + "D " + hrs + "H " + minsDiff + "M");
				}
				monthlyM.setLore(lore);
				monthly.setItemMeta(monthlyM);
				inv.setItem(BlockTopPlugin.instance.getConfig().getInt("inventory-items.monthly.slot"), monthly);

				ItemStack custom = GenUtil.buildItem("inventory-items.custom");
				ItemMeta customM = custom.getItemMeta();
				lore.clear();
				if (BlockTopPlugin.custom != null) {
					long minsDiff = ChronoUnit.MINUTES.between(LocalDateTime.now(), BlockTopPlugin.custom.getEndDate());
					long days = minsDiff / 1440;
					minsDiff %= 1440;
					long hrs = minsDiff / 60;
					minsDiff %= 60;
					lore.add(ChatColor.GRAY + "Ends in: " + days + "D " + hrs + "H " + minsDiff + "M");
				}
				customM.setLore(lore);
				custom.setItemMeta(customM);
				inv.setItem(BlockTopPlugin.instance.getConfig().getInt("inventory-items.custom.slot"), custom);

				ClickInInv.clicking.add(p);
				p.openInventory(inv);
			}
		}
		return false;
	}

}
