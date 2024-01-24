package org.polyfrost.luckyutils.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import org.polyfrost.luckyutils.LuckyUtils;
import org.polyfrost.luckyutils.hud.HotPotatoHud;

/**
 * The main Config entrypoint that extends the Config type and inits the config options.
 * See <a href="https://docs.polyfrost.cc/oneconfig/config/adding-options">this link</a> for more config Options
 */
public class HotPotatoConfig extends Config {
    @HUD(
            name = "Hot Potato Timer"
    )
    public HotPotatoHud hud = new HotPotatoHud();

    @Dropdown(
            name = "Notification Location", // Name of the Dropdown
            options = {"Action Bar", "Chat", "Off"} // Options available.
    )
    public int notificationLocation = 0; // Default option (in this case "Action Bar")

    @Switch(
            name = "Notification Sound"
    )
    public boolean notificationSound = false; // The default value for the boolean Switch.

    public HotPotatoConfig() {
        super(new Mod("Hot Potato Timer", ModType.UTIL_QOL), "HotPotatoConfig" + ".json");
        initialize();
    }
}

