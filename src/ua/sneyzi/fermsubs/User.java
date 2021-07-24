package ua.sneyzi.fermsubs;

import com.google.common.collect.Maps;

import ua.sneyzi.fermsubs.nbtapi.OfflineNBT;
import ua.sneyzi.fermsubs.nbtapi.nbt.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class User {

	protected static Manager manager;
	private static File file;
	private static FileConfiguration config;
	

	static {
		file = new File(Main.getInstance().getDataFolder(), "players.yml");
		config = (FileConfiguration) YamlConfiguration.loadConfiguration(file);
	}
	
	public User() {
	}
	
	public static void disable(String nick, String subscription) {
		if (Bukkit.getPlayerExact(nick) != null) {
			Player player = Bukkit.getPlayerExact(nick);

			if (subscription.equals("fly")) {
				player.setAllowFlight(false);
				player.setFlying(false);
			}

			if (subscription.equals("god")) {
				Main.getInstance().getEssentials().getUser(player).setGodModeEnabled(false);
			}

			if (subscription.equals("protect")) {

			}
		} else if (Bukkit.getPlayerExact(nick) == null) {
			UUID uuid = Bukkit.getPlayerUniqueId(nick);

			if (subscription.equals("fly")) {
				try {
					String UUID = String.valueOf(uuid);
					OfflineNBT Off = new OfflineNBT(String.valueOf(Main.playerWorldFolder));
					NBTTagCompound playerNbt = Off.readFromNBT(UUID);
					playerNbt.setInteger("abilities.mayfly", 0);
					playerNbt.setInteger("abilities.flying", 0);

					Off.writeToNBT(UUID, playerNbt);
				} catch (Exception e) {
					e.getMessage();
				}
			}
			
			if (subscription.equals("god")) {
				if (Main.getInstance().getEssentials().getUser(uuid) != null) {
					Main.getInstance().getEssentials().getUser(uuid).setGodModeEnabled(false);
				}
			}

			if (subscription.equals("protect")) {

			}
		}
	}
	
	public static void addSubscription(String player, String subscription, long lifetime) {
		if (lifetime > 0) {
			if (config.getString("players." + player + ".sub-" + subscription + "-until") != null) {
				if (Long.parseLong(config.getString("players." + player + ".sub-" + subscription + "-until")) > 0) {
					long old = Long.parseLong(config.getString("players." + player + ".sub-" + subscription + "-until"));
					long oldTime = (old - (System.currentTimeMillis() / 1000));
					long sumTime = (System.currentTimeMillis() / 1000 + oldTime + lifetime);
					if (sumTime > 0) {
						config.set("players." + player + ".sub-" + subscription + "-until", Long.toString(sumTime));
						saveConfig();
					}
				}
			} else {
				config.set("players." + player + ".sub-" + subscription + "-until", Long.toString(System.currentTimeMillis() / 1000 + lifetime));
				saveConfig();
			}
		}
		updateTimedGroups();
	}
	
	public static void removeSubscription(String player, String subscription, long lifetime) {
		if (lifetime > 0) {
			if (config.getString("players." + player + ".sub-" + subscription + "-until") != null) {
				if (Long.parseLong(config.getString("players." + player + ".sub-" + subscription + "-until")) > 0) {
					long old = Long.parseLong(config.getString("players." + player + ".sub-" + subscription + "-until"));
					long oldTime = (old - (System.currentTimeMillis() / 1000));
					long newTime = oldTime - lifetime;
					long sumTime = System.currentTimeMillis() / 1000 + newTime;

					if (oldTime - lifetime > 0) {
						config.set("players." + player + ".sub-" + subscription + "-until", Long.toString(sumTime));
						saveConfig();
					} else {
						config.set("players." + player + ".sub-" + subscription + "-until", null);
						saveConfig();
						User.disable(player, subscription);
					}
				}
			} else {
			}
		}
		updateTimedGroups();
	}
	
	public static long hasSubscription(String player, String subscription) {
		if (config.getString("players." + player + ".sub-" + subscription + "-until") != null) {
			if (Long.parseLong(config.getString("players." + player + ".sub-" + subscription + "-until")) > 0) {
				return Long.parseLong(config.getString("players." + player + ".sub-" + subscription + "-until"));
			}
			
		}
		return 0;
	}
	
	public static Map<String, Map<String, String>> getAllOptions() {
		return getOptionsMap();
	}

	private static Map<String, String> collectOptions(ConfigurationSection section) {
		Map<String, String> options = new LinkedHashMap<>();
		for (String key : section.getKeys(true)) {
			if (section.isConfigurationSection(key)) {
				continue;
			}
			options.put(key.replace(section.getRoot().options().pathSeparator(), '.'), section.getString(key));
		}
		return options;
	}
		
	public static Map<String, Map<String, String>> getOptionsMap() {
		Map<String, Map<String, String>> allOptions = new HashMap<>();

		for (String str : config.getConfigurationSection("players").getKeys(false)) {
			allOptions.put(str, Collections.unmodifiableMap(collectOptions(config.getConfigurationSection("players." + str))));
		}

		return Collections.unmodifiableMap(allOptions);
	}
	
	public static void setOption(String nick, String option) {
		config.set("players." + nick + ".sub-" + option + "-until", null);
		saveConfig();
		
	}
	 	
	static void updateTimedGroups() {
		long nextExpiration = Long.MAX_VALUE;
		Set<Map.Entry<String, String>> removeGroups = new HashSet<>();
		String idf = "";

		for (Map.Entry<String, Map<String, String>> world : getAllOptions().entrySet()) {
			for (Map.Entry<String, String> entry : world.getValue().entrySet()) {
				String group = getTimedGroupName(entry.getKey());
				idf = world.getKey();

				if (group == null) { // Not a timed group
					continue;
				}
				long groupLifetime = Long.parseLong(entry.getValue());

				if (groupLifetime > 0 && groupLifetime <= System.currentTimeMillis() / 1000) { // check for expiration
					removeGroups.add(Maps.immutableEntry(group, world.getKey()));
				} else {
					nextExpiration = Math.min(nextExpiration, groupLifetime);
				}
			}
		}

		for (Map.Entry<String, String> ent : removeGroups) {
			setOption(ent.getValue(), ent.getKey());
			disable(ent.getValue(), ent.getKey());
		}

		if (nextExpiration < Long.MAX_VALUE) {
			Manager.scheduleTimedGroupsCheck(nextExpiration, idf);
		}
	}
	
	static String getTimedGroupName(String option) {
		if (!option.startsWith("sub-") && !option.endsWith("-until")) {
			return null;
		}
		return option.substring("sub-".length(), option.length() - "-until".length());
	}
	
	static void saveConfig() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void relo1adConfig() {
		YamlConfiguration.loadConfiguration(file);
	}
}