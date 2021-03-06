package ua.sneyzi.fermsubs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import ua.sneyzi.fermsubs.gui.ProtectMenu;

public class ProtectCommand implements CommandExecutor {

	private Plugin plugin;

	public ProtectCommand(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("?cOnly for players.");
			return true;
		}
		new ProtectMenu((Player) sender);
		return true;
	}
}