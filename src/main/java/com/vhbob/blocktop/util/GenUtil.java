package com.vhbob.blocktop.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.vhbob.blocktop.BlockTopPlugin;

import net.md_5.bungee.api.ChatColor;

public class GenUtil {

	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	public static YamlConfiguration loadConfigFromFile(File f) {
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return config;
	}

	public static ItemStack buildItem(String path) {
		ItemStack i = new ItemStack(Material.valueOf(BlockTopPlugin.instance.getConfig().getString(path + ".type")));
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&',
				BlockTopPlugin.instance.getConfig().getString(path + ".name")));
		i.setItemMeta(im);
		return i;
	}

	// function to sort hashmap by values
	public static HashMap<String, Long> sortByValue(HashMap<String, Long> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<String, Long>> list = new LinkedList<Map.Entry<String, Long>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
			public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, Long> temp = new LinkedHashMap<String, Long>();
		for (Map.Entry<String, Long> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

}
