package org.polyfrost.luckyutils.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import org.polyfrost.luckyutils.hud.GauntletUpgradeHud;
import org.polyfrost.luckyutils.hud.TimerDemoHud;

/**
 * The main Config entrypoint that extends the Config type and inits the config options.
 * See <a href="https://docs.polyfrost.cc/oneconfig/config/adding-options">this link</a> for more config Options
 */
public class TimerDemoConfig extends Config {
    @HUD(
            name = "Gauntlet Upgrade Counter"
    )
    public TimerDemoHud hud = new TimerDemoHud();

    public TimerDemoConfig() {
        super(new Mod("Gauntlet Upgrade Counter", ModType.UTIL_QOL), "GauntletUpgradeConfig" + ".json");
        initialize();
    }
}

