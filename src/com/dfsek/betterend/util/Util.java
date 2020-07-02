package com.dfsek.betterend.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.dfsek.betterend.Main;
import com.dfsek.betterend.UpdateChecker;
import com.dfsek.betterend.UpdateChecker.UpdateReason;
import com.dfsek.betterend.world.Biome;

public class Util {
	private static Main main = Main.getInstance();
	private static Logger logger = main.getLogger();
	private Util(){}
	public static Object chooseOnWeight(Object[] items, int[] weights) {
		double completeWeight = 0.0;
		for (int weight : weights)
			completeWeight += weight;
		double r = Math.random() * completeWeight;
		double countWeight = 0.0;
		for (int i = 0; i < items.length; i++) {
			countWeight += weights[i];
			if (countWeight >= r)
				return items[i];
		}
		return null;
	}
	public static boolean tpBiome(Player p, String[] args) {
		if(args[1].equalsIgnoreCase("END") || args[1].equalsIgnoreCase("SHATTERED_END") || args[1].equalsIgnoreCase("VOID") || args[1].equalsIgnoreCase("STARFIELD") || args[1].equalsIgnoreCase("SHATTERED_FOREST") || args[1].equalsIgnoreCase("AETHER") || args[1].equalsIgnoreCase("AETHER_HIGHLANDS") || (Main.isPremium() && args[1].equalsIgnoreCase("AETHER_HIGHLANDS_FOREST")) || (Main.isPremium() && args[1].equalsIgnoreCase("AETHER_FOREST"))) {
			p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd]" + ChatColor.AQUA + " Locating biome \"" + ChatColor.DARK_AQUA + args[1] + ChatColor.AQUA +  "\"");
			int tries = 0;
			Location candidate = p.getLocation();
			while(tries < 10000) {
				Location candidateN = candidate.add(tries,0,0);
				if(Biome.fromLocation(candidateN).equals(Biome.fromString(args[1])) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
					p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "Teleporting...");
					p.teleport(candidateN);
					return true;
				}
				candidateN = candidate.add(-tries,0,0);
				if(Biome.fromLocation(candidateN).equals(Biome.fromString(args[1])) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
					p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "Teleporting...");
					p.teleport(candidateN);
					return true;
				}
				candidateN = candidate.add(0,0,tries);
				if(Biome.fromLocation(candidateN).equals(Biome.fromString(args[1])) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
					p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "Teleporting...");
					p.teleport(candidateN);
					return true;
				}
				candidateN = candidate.add(0,0,-tries);
				if(Biome.fromLocation(candidateN).equals(Biome.fromString(args[1])) && Math.sqrt(Math.pow(candidateN.getBlockX(), 2) + Math.pow(candidateN.getBlockZ(), 2)) > 1000) {
					p.sendMessage(ChatColor.DARK_AQUA + "[BetterEnd] " + ChatColor.AQUA + "Teleporting...");
					p.teleport(candidateN);
					return true;
				}
				tries++;
			}
			p.sendMessage("[BetterEnd] Unable to locate biome.");
			return true;
		} else return false;
	}
	public static void checkUpdates() {
		Main instance = Main.getInstance();
		UpdateChecker.init(instance, 79389).requestUpdateCheck().whenComplete((result, exception) -> {
			if (result.requiresUpdate()) {
				instance.getLogger().info(String.format(LangUtil.newVersion, result.getNewestVersion()));
				return;
			}

			UpdateReason reason = result.getReason();
			if (reason == UpdateReason.upToDate) {
				instance.getLogger().info(String.format(LangUtil.upToDate, result.getNewestVersion()));
			} else if (reason == UpdateReason.UNRELEASED_VERSION) {
				instance.getLogger().info(String.format(LangUtil.moreRecent, result.getNewestVersion()));
			} else {
				instance.getLogger().warning(LangUtil.updateError + reason);//Occurred
			}
		});
	}
	public static String getFileAsString(InputStream stream) throws IOException {
		String line;
		BufferedReader buf = new BufferedReader(new InputStreamReader(stream));
		line = buf.readLine();
		StringBuilder sb = new StringBuilder();
		while(line != null){
			sb.append(line).append("\n");
			line = buf.readLine();
		}
		buf.close();
		return sb.toString();
	}
	public static void copyResourcesToDirectory(JarFile fromJar, String jarDir, String destDir) throws IOException {
		for(Enumeration<JarEntry> entries = fromJar.entries(); entries.hasMoreElements();) {
			JarEntry entry = entries.nextElement();
			if(ConfigUtil.debug) Main.getInstance().getLogger().info(entry.getName());
			if(entry.getName().startsWith(jarDir + "/") && !entry.isDirectory()) {
				File dest = new File(destDir + File.separator + entry.getName().substring(jarDir.length() + 1));
				if(ConfigUtil.debug) Main.getInstance().getLogger().info("Output: " + dest.toString());
				if(dest.exists()) continue;
				File parent = dest.getParentFile();
				if(parent != null) {
					parent.mkdirs();
				}
				if(ConfigUtil.debug) Main.getInstance().getLogger().info("Output does not already exist. Creating... ");
				try(FileOutputStream out = new FileOutputStream(dest);
				InputStream in = fromJar.getInputStream(entry)) {
					byte[] buffer = new byte[8 * 1024];

					int s = 0;
					while ((s = in.read(buffer)) > 0) {
						out.write(buffer, 0, s);
					}
				} catch (IOException e) {
					throw new IOException("Could not copy asset from jar file", e);
				}
			}
		}
	}
	public static void logForEach(List<String> msgs, Level lvl) {
		for(String msg:msgs) {
			logger.log(lvl, ChatColor.translateAlternateColorCodes('&', msg));
		}
	}
	public static double getOffset(Random random, double amount) {
		double offset = 0;
		if(random.nextBoolean()) offset = random.nextBoolean() ? -amount : amount;
		return offset;
	}
}
