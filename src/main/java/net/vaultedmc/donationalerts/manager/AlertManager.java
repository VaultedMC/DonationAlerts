package net.vaultedmc.donationalerts.manager;

import lombok.NonNull;
import me.clip.placeholderapi.PlaceholderAPI;
import net.bitbylogic.utils.Placeholder;
import net.bitbylogic.utils.message.format.Formatter;
import net.bitbylogic.utils.sound.SoundData;
import net.vaultedmc.donationalerts.DonationAlerts;
import net.vaultedmc.donationalerts.model.PendingAlert;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AlertManager {

    private final Map<UUID, PendingAlert> pendingAlerts = new ConcurrentHashMap<>();
    private final DonationAlerts plugin;

    private int alertTaskId = -1;

    public AlertManager(@NonNull DonationAlerts plugin) {
        this.plugin = plugin;

        startAlertTask();
    }

    public void startAlertTask() {
        if (alertTaskId != -1) {
            Bukkit.getScheduler().cancelTask(alertTaskId);
        }

        int alertDelay = plugin.getConfig().getInt("Alert-Delay", 1);
        SoundData alertSound = SoundData.fromString(plugin.getConfig().getString("Alert-Sound", "ENTITY.PLAYER.LEVELUP:1:2"));

        alertTaskId = new BukkitRunnable() {
            @Override
            public void run() {
                List<UUID> toRemove = new ArrayList<>();

                for (PendingAlert alert : pendingAlerts.values()) {
                    if (System.currentTimeMillis() < (alert.getLastUpdate() + (alertDelay * 1000L))) {
                        continue;
                    }

                    String itemLine = plugin.getConfig().getString("Item-Line", " &7%amount%x &6%item-name%");
                    Placeholder playerPlaceholder = new Placeholder("%player%", Bukkit.getOfflinePlayer(alert.getPlayerId()).getName());

                    for (String alertLine : plugin.getConfig().getStringList("Alert-Format")) {
                        if (!alertLine.equalsIgnoreCase("%item-lines%")) {
                            Bukkit.broadcastMessage(Formatter.format(alertLine, playerPlaceholder));
                            continue;
                        }

                        alert.getItems().forEach((itemName, amount) -> {
                            Bukkit.broadcastMessage(Formatter.format(itemLine,
                                    new Placeholder("%amount%", amount),
                                    new Placeholder("%item-name%", Formatter.format(itemName))
                            ));
                        });
                    }

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            alertSound.play(onlinePlayer);
                        }

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(alert.getPlayerId());

                        for (String command : plugin.getConfig().getStringList("Alert-Commands")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(offlinePlayer, command.replace("%player%", offlinePlayer.getName())));
                        }
                    });

                    toRemove.add(alert.getPlayerId());
                }

                toRemove.forEach(pendingAlerts::remove);
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20).getTaskId();
    }

    @SuppressWarnings("deprecation")
    public void addAlert(@NonNull String playerName, @NonNull String itemName) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);

        PendingAlert pendingAlert = pendingAlerts.computeIfAbsent(offlinePlayer.getUniqueId(), PendingAlert::new);
        pendingAlert.addItem(itemName);
        pendingAlert.setLastUpdate(System.currentTimeMillis());
    }

}
