package com.vhbob.blocktop.events;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.vhbob.blocktop.BlockTopPlugin;
import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

public class RegisterBreak implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (e.isCancelled())
			return;
		UUID id = e.getPlayer().getUniqueId();
		HashMap<UUID, Long> blocksBroke = BlockTopPlugin.totalBroke;
		if (blocksBroke.containsKey(id)) {
			blocksBroke.put(id, blocksBroke.get(id) + 1);
		} else {
			blocksBroke.put(id, 1L);
		}
		if (BlockTopPlugin.daily != null)
			BlockTopPlugin.daily.registerBreak(id, 1);
		if (BlockTopPlugin.weekly != null)
			BlockTopPlugin.weekly.registerBreak(id, 1);
		if (BlockTopPlugin.monthly != null)
			BlockTopPlugin.monthly.registerBreak(id, 1);
		if (BlockTopPlugin.custom != null && e.getBlock().getType() == BlockTopPlugin.custom.getBlockType())
			BlockTopPlugin.custom.registerBreak(id, 1);
	}

	@EventHandler
	public void onExplode(TEBlockExplodeEvent e) {
		if (e.isCancelled())
			return;
		UUID id = e.getPlayer().getUniqueId();
		HashMap<UUID, Long> blocksBroke = BlockTopPlugin.totalBroke;
		if (blocksBroke.containsKey(id)) {
			blocksBroke.put(id, blocksBroke.get(id) + e.blockList().size());
		} else {
			blocksBroke.put(id, 1L);
		}
		if (BlockTopPlugin.daily != null)
			BlockTopPlugin.daily.registerBreak(id, e.blockList().size());
		if (BlockTopPlugin.weekly != null)
			BlockTopPlugin.weekly.registerBreak(id, e.blockList().size());
		if (BlockTopPlugin.monthly != null)
			BlockTopPlugin.monthly.registerBreak(id, e.blockList().size());
		if (BlockTopPlugin.custom != null)
			for (Block b : e.blockList())
				if (b.getType() == BlockTopPlugin.custom.getBlockType())
					BlockTopPlugin.custom.registerBreak(id, 1);
	}

}
