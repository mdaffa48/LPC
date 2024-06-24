package me.wikmor.lpc.listeners;

import de.themoep.minedown.MineDown;
import io.papermc.paper.text.PaperComponents;
import me.clip.placeholderapi.PlaceholderAPI;
import me.wikmor.lpc.LPC;
import me.wikmor.lpc.utils.Colorize;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.stream.Collectors;

public class SpigotListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatHighestPriority(final AsyncPlayerChatEvent event) {
        if (LPC.getPriority().equalsIgnoreCase("HIGHEST")) {
            this.handle(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChatHighPriority(final AsyncPlayerChatEvent event) {
        if (LPC.getPriority().equalsIgnoreCase("HIGH")) {
            this.handle(event);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChatNormalPriority(final AsyncPlayerChatEvent event) {
        if (LPC.getPriority().equalsIgnoreCase("NORMAL")) {
            this.handle(event);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onChatLowPriority(final AsyncPlayerChatEvent event) {
        if (LPC.getPriority().equalsIgnoreCase("LOW")) {
            this.handle(event);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatLowestPriority(final AsyncPlayerChatEvent event) {
        if (LPC.getPriority().equalsIgnoreCase("LOWEST")) {
            this.handle(event);
        }
    }

    private void handle(AsyncPlayerChatEvent event) {
        final String message = event.getMessage();
        final Player player = event.getPlayer();

        // Get a LuckPerms cached metadata for the player.
        final CachedMetaData metaData = LPC.luckPerms().getPlayerAdapter(Player.class).getMetaData(player);
        final String group = metaData.getPrimaryGroup();

        FileConfiguration config = LPC.instance().getConfig();
        String format = config.getString(config.getString("group-formats." + group) != null ? "group-formats." + group : "chat-format")
                .replace("{message}", message)
                .replace("{prefix}", metaData.getPrefix() != null ? metaData.getPrefix() : "")
                .replace("{suffix}", metaData.getSuffix() != null ? metaData.getSuffix() : "")
                .replace("{prefixes}", metaData.getPrefixes().keySet().stream().map(key -> metaData.getPrefixes().get(key)).collect(Collectors.joining()))
                .replace("{suffixes}", metaData.getSuffixes().keySet().stream().map(key -> metaData.getSuffixes().get(key)).collect(Collectors.joining()))
                .replace("{world}", player.getWorld().getName())
                .replace("{name}", player.getName())
                .replace("{displayname}", player.getDisplayName())
                .replace("{username-color}", metaData.getMetaValue("username-color") != null ? metaData.getMetaValue("username-color") : "")
                .replace("{message-color}", metaData.getMetaValue("message-color") != null ? metaData.getMetaValue("message-color") : "");

        // Parse with placeholder api
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            format = PlaceholderAPI.setPlaceholders(player, format);
        }
        // Parse it with mine down
        format = TextComponent.toLegacyText(MineDown.parse(format));

        // Set the format
        event.setFormat(format);
    }
    
}
