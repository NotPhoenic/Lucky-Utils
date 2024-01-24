package org.polyfrost.luckyutils;

import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import org.polyfrost.luckyutils.config.DiamondTimerConfig;
import org.polyfrost.luckyutils.config.GauntletUpgradeConfig;
import org.polyfrost.luckyutils.config.HotPotatoConfig;
import org.polyfrost.luckyutils.config.MiracleTimerConfig;
import cc.polyfrost.oneconfig.events.event.InitializationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.polyfrost.luckyutils.programming.DiamondNotifications;
import org.polyfrost.luckyutils.programming.GauntletUpgradeCounter;
import org.polyfrost.luckyutils.programming.HotPotatoTimer;
import org.polyfrost.luckyutils.programming.MiracleNotifications;

/**
 * The entrypoint of the Example Mod that initializes it.
 *
 * @see Mod
 * @see InitializationEvent
 */
@Mod(modid = LuckyUtils.MODID, name = LuckyUtils.NAME, version = LuckyUtils.VERSION)
public class LuckyUtils {
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    // Sets the variables from `gradle.properties`. See the `blossom` config in `build.gradle.kts`.
    @Mod.Instance(MODID)
    public static LuckyUtils INSTANCE; // Adds the instance of the mod, so we can access other variables.
    public static MiracleTimerConfig miracleTimerConfig;
    public static DiamondTimerConfig diamondTimerConfig;

    public static HotPotatoConfig hotPotatoConfig;

    public static GauntletUpgradeConfig gauntletUpgradeConfig;

    // Register the config and commands.
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        CommandManager.register(new DiamondNotifications.Reset());
        CommandManager.register(new MiracleNotifications.Reset());
        CommandManager.register(new GauntletUpgradeCounter.Reset());


        miracleTimerConfig = new MiracleTimerConfig();
        MiracleNotifications miracleNotifications = new MiracleNotifications(miracleTimerConfig);
        diamondTimerConfig = new DiamondTimerConfig();
        DiamondNotifications diamondNotifications = new DiamondNotifications(diamondTimerConfig);

        hotPotatoConfig = new HotPotatoConfig();
        HotPotatoTimer hotPotatoTimer = new HotPotatoTimer(hotPotatoConfig);

        gauntletUpgradeConfig = new GauntletUpgradeConfig();
        GauntletUpgradeCounter gauntletUpgradeCounter = new GauntletUpgradeCounter(gauntletUpgradeConfig);

    }
}
