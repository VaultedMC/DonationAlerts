package net.vaultedmc.donationalerts.command;

import lombok.RequiredArgsConstructor;
import net.bitbylogic.utils.message.format.Formatter;
import net.vaultedmc.donationalerts.DonationAlerts;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class DonationAlertCommand implements CommandExecutor {

    private final DonationAlerts plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!sender.hasPermission(plugin.getConfig().getString("Command-Permission", "donationalerts.command"))) {
            sender.sendMessage(Formatter.format(plugin.getConfig().getString("No-Permission", "&cYou do not have permission to use this command!")));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(Formatter.format(plugin.getConfig().getString("Reloaded-Config", "&aReloaded config!")));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Formatter.format(plugin.getConfig().getString("Usage", "&cUsage: /donationalert <player> <item name>")));
            return true;
        }

        plugin.getAlertManager().addAlert(args[0], args[1]);
        return true;
    }

}
