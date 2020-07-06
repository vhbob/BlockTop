package com.vhbob.blocktop.datastorage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import com.vhbob.blocktop.BlockTopPlugin;
import com.vhbob.blocktop.contests.Contest;
import com.vhbob.blocktop.contests.ContestType;
import com.vhbob.blocktop.util.GenUtil;

public class ContestStorage {

	public static void saveContest(Contest c) {
		File contestFile = new File(BlockTopPlugin.instance.getDataFolder() + File.separator + "Contests"
				+ File.separator + c.getType() + ".yml");
		contestFile.getParentFile().mkdirs();
		if (!contestFile.exists())
			try {
				contestFile.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		YamlConfiguration contestConfig = GenUtil.loadConfigFromFile(contestFile);
		// save user data
		for (UUID id : c.getParticipants().keySet())
			contestConfig.set("blocks-broken." + id.toString(), c.getParticipants().get(id));
		// save rewards
		for (int place : c.getRewards().keySet())
			contestConfig.set("rewards." + place, c.getRewards().get(place));
		// save end date
		contestConfig.set("end-date", c.getEndDate().toString());
		// save material if needed
		if (c.getType() == ContestType.CUSTOM)
			contestConfig.set("block-type", c.getBlockType().toString());
		try {
			contestConfig.save(contestFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static Contest loadContest(ContestType type) {
		Contest contest = null;
		File contestFile = new File(
				BlockTopPlugin.instance.getDataFolder() + File.separator + "Contests" + File.separator + type + ".yml");
		if (contestFile.exists()) {
			YamlConfiguration contestConfig = GenUtil.loadConfigFromFile(contestFile);
			if (type == ContestType.CUSTOM)
				contest = new Contest(LocalDateTime.parse(contestConfig.getString("end-date")),
						Material.valueOf(contestConfig.getString("block-type")), type);
			else
				contest = new Contest(LocalDateTime.parse(contestConfig.getString("end-date")), type);
			// load players
			if (contestConfig.contains("blocks-broken"))
				for (String idString : contestConfig.getConfigurationSection("blocks-broken").getKeys(false))
					contest.addParticipant(UUID.fromString(idString),
							contestConfig.getLong("blocks-broken." + idString));
			// load rewards
			if (contestConfig.contains("rewards"))
				for (String placeString : contestConfig.getConfigurationSection("rewards").getKeys(false))
					contest.setReward((ArrayList<ItemStack>) contestConfig.get("rewards." + placeString),
							Integer.parseInt(placeString));

		}
		return contest;
	}

	public static void deleteContest(File contestFile) {
		if (contestFile.exists()) {
			contestFile.delete();
		}
	}

}
