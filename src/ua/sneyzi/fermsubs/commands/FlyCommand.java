package ua.sneyzi.fermsubs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import ua.sneyzi.fermsubs.gui.FlyMenu;

public class FlyCommand implements CommandExecutor {

	private Plugin plugin;

	public FlyCommand(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cOnly for players.");
			return true;
		}
		new FlyMenu((Player) sender);
		return true;
	}
}