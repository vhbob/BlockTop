package com.vhbob.blocktop.contests;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.vhbob.blocktop.BlockTopPlugin;
import com.vhbob.blocktop.util.GenUtil;

public class Contest {

	private HashMap<UUID, Long> participants;
	private LocalDateTime endDate;
	private Material specialBlock;
	private boolean special;
	private ContestType type;
	private HashMap<Integer, ArrayList<ItemStack>> rewards;

	public Contest(LocalDateTime endDate, ContestType type) {
		this.type = type;
		this.endDate = endDate;
		this.participants = new HashMap<UUID, Long>();
		this.special = false;
		this.specialBlock = null;
		this.rewards = new HashMap<Integer, ArrayList<ItemStack>>();
	}

	public Contest(LocalDateTime endDate, Material specialBlock, ContestType type) {
		this.type = type;
		this.special = true;
		this.specialBlock = specialBlock;
		this.endDate = endDate;
		this.participants = new HashMap<UUID, Long>();
		this.rewards = new HashMap<Integer, ArrayList<ItemStack>>();
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public boolean isSpecial() {
		return special;
	}

	public Material getBlockType() {
		return specialBlock;
	}

	public ContestType getType() {
		return type;
	}

	public HashMap<UUID, Long> getParticipants() {
		return participants;
	}

	public HashMap<Integer, ArrayList<ItemStack>> getRewards() {
		return rewards;
	}

	public void addReward(ItemStack item, int place) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		if (rewards.containsKey(place))
			items = rewards.get(place);
		items.add(item);
		rewards.put(place, items);
	}

	public void setReward(ArrayList<ItemStack> rew, int place) {
		rewards.put(place, rew);
	}

	public void setReward(ItemStack[] rew, int place) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (ItemStack item : rew)
			items.add(item);
		rewards.put(place, items);
	}

	public void addParticipant(UUID id, long broken) {
		participants.put(id, broken);
	}

	public void registerBreak(UUID id, long broken) {
		if (participants.containsKey(id))
			participants.put(id, participants.get(id) + broken);
		else
			participants.put(id, broken);
	}

	public void finish() {
		HashMap<String, Long> sortedParticipants = new HashMap<String, Long>();
		for (UUID id : participants.keySet()) {
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(id);
			sortedParticipants.put(offlinePlayer.getName(), participants.get(id));
		}
		int currentPlace = 1;
		for (String name : sortedParticipants.keySet()) {
			if (name == null)
				continue;
			if (rewards.containsKey(currentPlace)) {
				@SuppressWarnings("deprecation")
				// Player's name shouldnt change between the last piece of code and here
				OfflinePlayer p = Bukkit.getOfflinePlayer(name);
				if (BlockTopPlugin.itemsToGive.containsKey(p.getUniqueId())) {
					BlockTopPlugin.itemsToGive.get(p.getUniqueId()).addAll(rewards.get(currentPlace));
				} else {
					BlockTopPlugin.itemsToGive.put(p.getUniqueId(), rewards.get(currentPlace));
				}
				if (p.isOnline()) {
					Player player = (Player) p;
					player.sendMessage(ChatColor.GREEN + "You won rewards from a contest! Do /topminer to claim!");
				}
			}
		}
		participants.clear();
	}

	public HashMap<String, Long> getTopTen() {
		HashMap<String, Long> topTen = new HashMap<String, Long>();
		for (UUID id : participants.keySet()) {
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(id);
			topTen.put(offlinePlayer.getName(), participants.get(id));
		}
		topTen = GenUtil.sortByValue(topTen);
		if (topTen.size() < 10) {
			return topTen;
		} else {
			HashMap<String, Long> realTopTen = new HashMap<String, Long>();
			int i = 0;
			for (String name : topTen.keySet()) {
				++i;
				realTopTen.put(name, topTen.get(name));
				if (i >= 10)
					break;
			}
			return realTopTen;
		}
	}

}
