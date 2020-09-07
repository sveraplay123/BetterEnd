package com.dfsek.betterend.config;

import com.dfsek.betterend.BetterEnd;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class ConfigUtil {
    public static String lang;
    public static boolean debug;
    public static boolean doUpdateCheck;
    public static long updateCheckFrequency;
    public static boolean generateBigTreesEverywhere;
    public static int treeGrowthMultiplier;
    public static boolean parallel;
    public static boolean endCities;

    private ConfigUtil() {
    }

    public static void loadConfig(Logger logger, JavaPlugin main) {
        long start = System.nanoTime();
        logger.info("Loading configuration values...");
        main.reloadConfig();
        FileConfiguration config = main.getConfig();

        lang = config.getString("lang", "en_us");
        debug = config.getBoolean("debug", false);
        doUpdateCheck = config.getBoolean("update-checker.enable", true);
        updateCheckFrequency = config.getLong("update-checker.frequency", 21600L);
        generateBigTreesEverywhere = config.getBoolean("big-trees-everywhere", false);
        treeGrowthMultiplier = config.getInt("tree-sapling-growth-modifier", 8);
        parallel = config.getBoolean("parallel", true);
        endCities = config.getBoolean("generate-end-cities", false);


        LangUtil.loadlang(lang, logger);

        WorldConfig.reloadAll(main);

        logger.info("Complete. Time elapsed: " + ((double) (System.nanoTime() - start)) / 1000000 + "ms");
    }

    public static void init(Logger logger, BetterEnd main) {
        loadConfig(logger, main);
    }
}
