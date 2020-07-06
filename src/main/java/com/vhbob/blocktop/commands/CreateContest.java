package com.vhbob.blocktop.commands;

import java.time.LocalDateTime;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.vhbob.blocktop.BlockTopPlugin;
import com.vhbob.blocktop.contests.Contest;
import com.vhbob.blocktop.contests.ContestType;
import com.vhbob.blocktop.util.GenUtil;

public class CreateContest implements CommandExecutor {

	// MAKE SURE NO SPECIAL CONTEST EXISTS
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("createcontest")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (p.getInventory().getItemInHand() == null
						|| p.getInventory().getItemInHand().getType() == Material.AIR) {
					p.sendMessage(ChatColor.RED + "You must be holding the block type you want to track");
					return false;
				}
				if (sender.hasPermission("contest.create")) {
					if (BlockTopPlugin.custom != null) {
						sender.sendMessage(ChatColor.RED + "There is already a custom contest going on!");
					}
					if (args.length == 1) {
						if (!GenUtil.isInt(args[0])) {
							p.sendMessage(ChatColor.RED + "Hours must be an integer");
							return false;
						}
						Long hours = Long.parseLong(args[0]);
						LocalDateTime endDate = LocalDateTime.now().plusHours(hours);
						Contest c = new Contest(endDate, p.getInventory().getItemInHand().getType(),
								ContestType.CUSTOM);
						sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD
								+ "Created a block breaking contest to end on: " + endDate.getDayOfWeek() + ", "
								+ endDate.getMonth() + ", " + endDate.getDayOfMonth() + ". At " + endDate.getHour()
								+ ":" + endDate.getMinute());
						sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Block Type: "
								+ p.getInventory().getItemInHand().getType());
						BlockTopPlugin.custom = c;
					} else {
						sender.sendMessage(ChatColor.RED + "Usage: /CreateContest (hours to last)");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Missing permission: contest.create");
				}
			}
		}
		return false;
	}

}
