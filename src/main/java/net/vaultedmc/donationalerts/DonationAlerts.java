package net.vaultedmc.donationalerts;

import lombok.Getter;
import net.bitbylogic.utils.message.format.Formatter;
import net.vaultedmc.donationalerts.command.DonationAlertCommand;
import net.vaultedmc.donationalerts.manager.AlertManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class DonationAlerts extends JavaPlugin {

    private AlertManager alertManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Formatter.registerConfig(new File("config.yml"));

        this.alertManager = new AlertManager(this);

        getCommand("donationalert").setExecutor(new DonationAlertCommand(this));
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();

        if (alertManager == null) {
            return;
        }

        alertManager.startAlertTask();
    }

}
