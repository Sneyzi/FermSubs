package ua.sneyzi.fermsubs;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;

import ua.sneyzi.fermsubs.commands.FlyCommand;
import ua.sneyzi.fermsubs.commands.GodCommand;
import ua.sneyzi.fermsubs.commands.ProtectCommand;
import ua.sneyzi.fermsubs.commands.SubsCommand;
import ua.sneyzi.fermsubs.listeners.EventListener;
import ua.sneyzi.fermsubs.utils.Cooldown;
import ua.sneyzi.fermsubs.utils.Util;

public class Main extends JavaPlugin {
	public static ConsoleCommandSender clogger = Bukkit.getServer().getConsoleSender();
	protected Manager manager;
	private static Main instance;
	private File playersConfigFile;
	private FileConfiguration playersConfig;
	private Essentials essentials;
	public static File playerWorldFolder;
	public Cooldown cooldown;

	public Main() {
		instance = this;
	}

	@Override
	public void onEnable() {
		if (this.getServer().getPluginManager().getPlugin("Essentials") == null) {
			System.out.println("§cОШИБКА§7: §7Essentials не установлен!");
			setEnabled(false);
			return;
		}
		this.essentials = (Essentials) this.getServer().getPluginManager().getPlugin("Essentials");
		playerWorldFolder = new File(Bukkit.getWorlds().get(0).getWorldFolder(), "playerdata");
		this.saveDefaultConfig();
		this.createPlayersConfig();
		this.getServer().getPluginManager().registerEvents(new EventListener(this), this);

		this.getCommand("subs").setExecutor(new SubsCommand(this));
		
		if (getConfig().getBoolean("fly.enabled")) {
			this.getCommand("fly").setExecutor(new FlyCommand(this));
		}
		if (getConfig().getBoolean("god.enabled")) {
			this.getCommand("god").setExecutor(new GodCommand(this));
		}
		if (getConfig().getBoolean("protect.enabled")) {
			this.getCommand("protect").setExecutor(new ProtectCommand(this));
		}
		
		cooldown = new Cooldown();
		cooldown.runTaskTimerAsynchronously(this, 0, 20);

		if (this.manager == null) {
			this.manager = new Manager();
		}
		this.manager.initTimer();

		User.updateTimedGroups();
		Main.clogger.sendMessage(String.valueOf(Util.getMessage("messages.prefix") + " §fПлагин by §6Sneyzi §eуспешно §fзапущен."));
	}
	
	public void onDisable() {
		if (this.manager != null) {
			this.manager.end();
			this.manager = null;
		}
		Main.clogger.sendMessage(String.valueOf(Util.getMessage("messages.prefix") + " §fПлагин by §6Sneyzi §eуспешно §fвыключен."));
	}

	public static Main getInstance() {
		return instance;
	}
	
    public Essentials getEssentials() {
        return this.essentials;
    }

	private void createPlayersConfig() {
		playersConfigFile = new File(getDataFolder(), "players.yml");
		if (!playersConfigFile.exists()) {
			playersConfigFile.getParentFile().mkdirs();
			saveResource("players.yml", false);
		}

		playersConfig = new YamlConfiguration();
		try {
			playersConfig.load(playersConfigFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
