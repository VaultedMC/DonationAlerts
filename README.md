<div align="center">
  <img src="https://i.imgur.com/5dZBolg.png" width="200">
  
  <h1>Donation Alerts</h1>

  A lightweight **Spigot/Paper plugin** for broadcasting **grouped store / donation alerts** in-game.  
Alerts are **merged per player** over a configurable delay to prevent spam and produce clean, polished announcements.

Built for **VaultedMC**, but usable on any modern Minecraft server.
</div>

---

## ✨ Features

- 📣 **Grouped alerts per player**  
  Multiple purchases within a short time window are merged into one alert
- ⏱️ **Configurable alert delay**  
  Prevents spam when multiple commands fire rapidly
- 🎨 **Fully customizable alert format**  
  Supports multi-line messages and custom styling
- 🔊 **Broadcast sound support**
- ⚙️ **Optional console commands on alert**
- 🔁 **Live config reload command**
- 🧩 **PlaceholderAPI support** for alert commands

---

## 📦 Installation

1. Build or download the plugin JAR
2. Place it into your server’s `plugins/` directory
3. Start or restart the server
4. Edit `plugins/DonationAlerts/config.yml`
5. (Optional) Reload using `/donationalert reload`

---

## 🧾 Commands

### /donationalert <player> <item name>

Adds an alert entry for a player.  
Alerts are queued and merged based on the configured delay.

Permission: `donationalerts.command`

### /donationalert reload

Reloads the plugin configuration.

---

## ⚙️ Configuration

```yml
# Command Permission
# The permission required to use the /donationalert command
Command-Permission: "donationalerts.command"

# The message sent if the player doesn't have permission to use the command
No-Permission: "&cYou don't have permission to use this command!"

# The message sent if the player enters invalid arguments in the command
# Example: /donationalert test
Usage: "&cUsage: /donationalert <player> <item name>"

# The message sent when the config is reloaded
Reloaded-Config: "&aReloaded config!"

# Alert Delay
# The amount of time (in seconds) before alerting after we received a command.
# This can be used to merge alerts into a single alert
Alert-Delay: 1

# %item-name% - The item name
# %amount% - The amount times they purchased the item, for example, 5x crate keys
Item-Line: "<cr>&7%amount%x &f&n%item-name%</cr>"

# Alert Commands
# These commands will be ran when the alert message is sent
Alert-Commands: []

# Alert Sound
# This sound will be broadcast when the alert message is sent
Alert-Sound: "ENTITY.PLAYER.LEVELUP:1:2"

# Alert Format
# Valid Placeholders:
# %player% - Player Name
Alert-Format:
  - "<cr>&8&m                    &r &f&nꜱᴛᴏʀᴇ ᴀʟᴇʀᴛ &8&m                    </cr>"
  - ""
  - "<cr>&f&n%player%&r &6ᴘᴜʀᴄʜᴀꜱᴇᴅ ᴛʜᴇ ꜰᴏʟʟᴏᴡɪɴɢ:</cr>"
  - ""
  - "%item-lines%"
  - ""
  - "<cr>&6&nᴛʜᴀɴᴋ ʏᴏᴜ!</cr>"
  - ""
  - "<cr>&7&nꜱᴛᴏʀᴇ.ᴠᴀᴜʟᴛᴇᴅ-ᴍᴄ.ɴᴇᴛ</cr>"
  - "<cr>&8&m                                                              </cr>"
```

---

## 🧠 How It Works

- Alerts are stored per player UUID
- Each /donationalert call:
    - Adds an item entry
    - Updates the player's last activity timestamp
- A repeating async task:
    - Waits for the alert delay to expire
    - Broadcasts the formatted alert
    - Plays a sound to all players
    - Executes optional console commands
- After broadcast, the alert is removed

Example merge behavior:

```
/donationalert Steve Crate Key  
/donationalert Steve Crate Key  
/donationalert Steve Rank Upgrade
```

Produces one alert:

```
2x Crate Key
1x Rank Upgrade
```

---

## 🔌 Dependencies

- Spigot or Paper
- PlaceholderAPI (optional, only required if using placeholders in Alert-Commands)

---

## 🛠️ Building From Source

```bash
git clone https://github.com/VaultedMC/DonationAlerts.git  
cd DonationAlerts
mvn clean package
```

The compiled JAR will be in target/.

---

## 🤝 Contributing

Pull requests and issues are welcome.  
Keep changes clean and aligned with the existing alert batching logic.

---

## 📄 License

The project is licensed under the MIT license.
