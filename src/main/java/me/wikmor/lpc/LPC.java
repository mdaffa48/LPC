package me.wikmor.lpc;

import me.wikmor.lpc.listeners.SpigotListener;
import me.wikmor.lpc.utils.Colorize;
import net.luckperms.api.LuckPerms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LPC extends JavaPlugin {

	private static LPC instance;
	public static boolean IS_PAPER = false;
	private static LuckPerms luckPerms;
	
	@Override
	public void onEnable() {
		instance = this;
		luckPerms = getServer().getServicesManager().load(LuckPerms.class);

		// Check if server using paper software
		try {
			Class.forName("com.destroystokyo.paper.ParticleBuilder");
			IS_PAPER = true;
		} catch (ClassNotFoundException ignored) {
		}

		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new SpigotListener(), this);
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (args.length == 1 && "reload".equals(args[0])) {
			reloadConfig();

			sender.sendMessage(Colorize.parse("&aLPC has been reloaded."));
			return true;
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
		if (args.length == 1)
			return Collections.singletonList("reload");

		return new ArrayList<>();
	}

	public static LPC instance() {
		return instance;
	}

	public static String getPriority() {
		return instance().getConfig().getString("priority", "HIGHEST");
	}

	public static LuckPerms luckPerms() {
		return luckPerms;
	}


}