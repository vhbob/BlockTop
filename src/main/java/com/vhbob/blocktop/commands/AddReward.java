package com.vhbob.blocktop.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.vhbob.blocktop.BlockTopPlugin;
import com.vhbob.blocktop.contests.ContestType;
import com.vhbob.blocktop.events.EditRewards;
import com.vhbob.blocktop.util.GenUtil;

public class AddReward implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("Rewards")) {
			if (sender.hasPermission(command.getPermission())) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					if (args.length == 2) {
						if (ContestType.contains(args[0])) {
							if (GenUtil.isInt(args[1])) {
								int place = Integer.parseInt(args[1]);
								ContestType type = ContestType.valueOf(args[0]);
								Inventory inv = Bukkit.createInventory(null, 54, args[0] + " : " + args[1]);
								if (type == ContestType.CUSTOM) {
									if (BlockTopPlugin.custom.getRewards().containsKey(place)) {
										for (ItemStack item : BlockTopPlugin.custom.getRewards().get(place)) {
											if (item != null)
												inv.addItem(item);
										}
									}
									EditRewards.editingContest.put(p, BlockTopPlugin.custom);
								}
								if (type == ContestType.DAILY) {
									if (BlockTopPlugin.daily.getRewards().containsKey(place)) {
										for (ItemStack item : BlockTopPlugin.daily.getRewards().get(place)) {
											if (item != null)
												inv.addItem(item);
										}
									}
									EditRewards.editingContest.put(p, BlockTopPlugin.daily);
								}
								if (type == ContestType.WEEKLY) {
									if (BlockTopPlugin.weekly.getRewards().containsKey(place)) {
										for (ItemStack item : BlockTopPlugin.weekly.getRewards().get(place)) {
											if (item != null)
												inv.addItem(item);
										}
									}
									EditRewards.editingContest.put(p, BlockTopPlugin.weekly);
								}
								if (type == ContestType.MONTHLY) {
									if (BlockTopPlugin.monthly.getRewards().containsKey(place)) {
										for (ItemStack item : BlockTopPlugin.monthly.getRewards().get(place)) {
											if (item != null)
												inv.addItem(item);
										}
									}
									EditRewards.editingContest.put(p, BlockTopPlugin.monthly);
								}
								EditRewards.editingPlace.put(p, place);
								p.openInventory(inv);
							} else {
								p.sendMessage(
										ChatColor.RED + "Usage: /rewards (DAILY / WEEKLY / MONTHLY / CUSTOM) (place)");
							}
						} else {
							p.sendMessage(
									ChatColor.RED + "Usage: /rewards (DAILY / WEEKLY / MONTHLY / CUSTOM) (place)");
						}
					} else {
						p.sendMessage(ChatColor.RED + "Usage: /rewards (DAILY / WEEKLY / MONTHLY / CUSTOM) (place)");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "You must be a player to do this");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Missing permission: " + command.getPermission());
			}
		}
		return false;
	}

}
