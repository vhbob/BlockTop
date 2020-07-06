package com.vhbob.blocktop;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.vhbob.blocktop.commands.AddReward;
import com.vhbob.blocktop.commands.CreateContest;
import com.vhbob.blocktop.commands.TopMiner;
import com.vhbob.blocktop.contests.Contest;
import com.vhbob.blocktop.contests.ContestType;
import com.vhbob.blocktop.datastorage.ContestStorage;
import com.vhbob.blocktop.datastorage.PlayerData;
import com.vhbob.blocktop.events.ClaimItem;
import com.vhbob.blocktop.events.ClickInInv;
import com.vhbob.blocktop.events.EditRewards;
import com.vhbob.blocktop.events.PlayerLogin;
import com.vhbob.blocktop.events.RegisterBreak;

public class BlockTopPlugin extends JavaPlugin {

	public static Contest daily, weekly, monthly, custom;
	public static HashMap<UUID, ArrayList<ItemStack>> itemsToGive;
	public static HashMap<UUID, Long> totalBroke;
	public static BlockTopPlugin instance;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		instance = this;
		Bukkit.getConsoleSender().sendMessage("Registering Lists");
		totalBroke = new HashMap<UUID, Long>();
		itemsToGive = new HashMap<UUID, ArrayList<ItemStack>>();
		Bukkit.getConsoleSender().sendMessage("Registering Player Data");
		for (Player p : getServer().getOnlinePlayers())
			PlayerData.loadPlayerData(p.getUniqueId());
		Bukkit.getPluginManager().registerEvents(new RegisterBreak(), this);
		getCommand("CreateContest").setExecutor(new CreateContest());
		getCommand("TopMiner").setExecutor(new TopMiner());
		Bukkit.getPluginManager().registerEvents(new ClickInInv(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerLogin(), this);
		Bukkit.getPluginManager().registerEvents(new EditRewards(), this);
		Bukkit.getPluginManager().registerEvents(new ClaimItem(), this);
		Bukkit.getConsoleSender().sendMessage("Registering Contest Data");
		// Setup monthly contest
		daily = ContestStorage.loadContest(ContestType.DAILY);
		if (daily == null)
			daily = new Contest(LocalDateTime.now().plusDays(1L), ContestType.DAILY);
		if (!daily.getEndDate().isAfter(LocalDateTime.now())) {
			Contest newDaily = new Contest(LocalDateTime.now().plusDays(1L), ContestType.DAILY);
			for (int place : daily.getRewards().keySet())
				newDaily.setReward(daily.getRewards().get(place), place);
			daily.finish();
			daily = newDaily;
		}
		// Setup monthly contest
		weekly = ContestStorage.loadContest(ContestType.WEEKLY);
		if (weekly == null)
			weekly = new Contest(LocalDateTime.now().plusWeeks(1L), ContestType.WEEKLY);
		if (!weekly.getEndDate().isAfter(LocalDateTime.now())) {
			Contest newWeekly = new Contest(LocalDateTime.now().plusWeeks(1L), ContestType.WEEKLY);
			for (int place : weekly.getRewards().keySet())
				newWeekly.setReward(weekly.getRewards().get(place), place);
			weekly.finish();
			weekly = newWeekly;
		}
		// Setup monthly contest
		monthly = ContestStorage.loadContest(ContestType.MONTHLY);
		if (monthly == null)
			monthly = new Contest(LocalDateTime.now().plusMonths(1L), ContestType.MONTHLY);
		if (!monthly.getEndDate().isAfter(LocalDateTime.now())) {
			Contest newMonthly = new Contest(LocalDateTime.now().plusMonths(1L), ContestType.MONTHLY);
			for (int place : monthly.getRewards().keySet())
				newMonthly.setReward(monthly.getRewards().get(place), place);
			monthly.finish();
			monthly = newMonthly;
		}
		// Setup custom contest
		custom = ContestStorage.loadContest(ContestType.CUSTOM);
		if (custom != null) {
			if (!custom.getEndDate().isAfter(LocalDateTime.now())) {
				custom.finish();
				custom = null;
			} else {
				prepareEnd(custom);
			}
		}
		getCommand("Rewards").setExecutor(new AddReward());
	}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("Saving player data");
		HashSet<UUID> needCheck = new HashSet<UUID>();
		needCheck.addAll(itemsToGive.keySet());
		needCheck.addAll(totalBroke.keySet());
		for (UUID id : needCheck)
			PlayerData.savePlayerData(id);
		Bukkit.getConsoleSender().sendMessage("Saving contest data");
		if (custom != null)
			ContestStorage.saveContest(custom);
		else {
			ContestStorage.deleteContest(new File(BlockTopPlugin.instance.getDataFolder() + File.separator + "Contests"
					+ File.separator + "Custom.yml"));
		}
		if (monthly != null)
			ContestStorage.saveContest(monthly);
		else {
			ContestStorage.deleteContest(new File(BlockTopPlugin.instance.getDataFolder() + File.separator + "Contests"
					+ File.separator + "Monthly.yml"));
		}
		if (weekly != null)
			ContestStorage.saveContest(weekly);
		else {
			ContestStorage.deleteContest(new File(BlockTopPlugin.instance.getDataFolder() + File.separator + "Contests"
					+ File.separator + "Weekly.yml"));
		}
		if (daily != null)
			ContestStorage.saveContest(daily);
		else {
			ContestStorage.deleteContest(new File(BlockTopPlugin.instance.getDataFolder() + File.separator + "Contests"
					+ File.separator + "Daily.yml"));
		}
	}

	public static void prepareEnd(final Contest c) {
		new BukkitRunnable() {
			public void run() {
				c.finish();
				// Create new contest if needed
				if (c.getType() == ContestType.DAILY) {
					Contest newDaily = new Contest(LocalDateTime.now().plusDays(1L), ContestType.DAILY);
					for (int place : daily.getRewards().keySet())
						newDaily.setReward(daily.getRewards().get(place), place);
					daily = newDaily;
				} else if (c.getType() == ContestType.WEEKLY) {
					Contest newWeekly = new Contest(LocalDateTime.now().plusWeeks(1L), ContestType.WEEKLY);
					for (int place : weekly.getRewards().keySet())
						newWeekly.setReward(weekly.getRewards().get(place), place);
					weekly = newWeekly;
				} else if (c.getType() == ContestType.MONTHLY) {
					Contest newMonthly = new Contest(LocalDateTime.now().plusMonths(1L), ContestType.MONTHLY);
					for (int place : monthly.getRewards().keySet())
						newMonthly.setReward(monthly.getRewards().get(place), place);
					monthly.finish();
					monthly = newMonthly;
				}
			}
		}.runTaskLater(BlockTopPlugin.instance, 20 * ChronoUnit.SECONDS.between(LocalDateTime.now(), c.getEndDate()));
	}

}
