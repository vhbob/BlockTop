package com.vhbob.blocktop.datastorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.vhbob.blocktop.BlockTopPlugin;
import com.vhbob.blocktop.util.GenUtil;

public class PlayerData {

	public static void savePlayerData(UUID playerID) {
		// Load file
		File dataFile = new File(BlockTopPlugin.instance.getDataFolder() + File.separator + "PlayerData"
				+ File.separator + playerID + ".yml");
		dataFile.getParentFile().mkdirs();
		if (!dataFile.exists())
			try {
				dataFile.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		YamlConfiguration fileConfig = GenUtil.loadConfigFromFile(dataFile);
		// Save total broke
		if (BlockTopPlugin.totalBroke.containsKey(playerID))
			fileConfig.set("total-broke", BlockTopPlugin.totalBroke.get(playerID));
		// Save items to give
		if (BlockTopPlugin.itemsToGive.containsKey(playerID))
			fileConfig.set("items", BlockTopPlugin.itemsToGive.get(playerID));

		try {
			fileConfig.save(dataFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadPlayerData(UUID playerID) {
		File playerFile = new File(BlockTopPlugin.instance.getDataFolder() + File.separator + "PlayerData"
				+ File.separator + playerID + ".yml");
		if (!playerFile.exists())
			return;
		YamlConfiguration playerConfig = GenUtil.loadConfigFromFile(playerFile);
		if (playerConfig.contains("total-broke"))
			BlockTopPlugin.totalBroke.put(playerID, playerConfig.getLong("total-broke"));
		else
			BlockTopPlugin.totalBroke.put(playerID, 0L);
		if (playerConfig.contains("items")) {
			@SuppressWarnings("unchecked")
			ArrayList<ItemStack> items = (ArrayList<ItemStack>) playerConfig.get("items");
			if (!items.isEmpty())
				BlockTopPlugin.itemsToGive.put(playerID, items);
		}
	}

}
