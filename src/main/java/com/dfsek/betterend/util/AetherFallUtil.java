package com.dfsek.betterend.util;

import com.dfsek.betterend.biomes.BiomeGrid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.dfsek.betterend.generation.EndChunkGenerator;

public class AetherFallUtil {
	private AetherFallUtil() {}
	public static void init(Plugin plugin) {
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
			for(Player p: plugin.getServer().getOnlinePlayers()) {
				if(p.getWorld().getGenerator() instanceof EndChunkGenerator && p.getLocation().getY() < 0 && (ConfigUtil.fallToOverworld || BiomeGrid.fromWorld(p.getWorld()).getBiome(p.getLocation()).isAether())) p.teleport(new Location(Bukkit.getWorlds().get(0), p.getLocation().getX(), p.getWorld().getMaxHeight(), p.getLocation().getZ()));
			}
		}, 2L, 2L);
	}
}
