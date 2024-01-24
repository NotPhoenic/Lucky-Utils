package org.polyfrost.luckyutils.programming;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.events.event.ChatSendEvent;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.polyfrost.luckyutils.config.GauntletUpgradeConfig;
import org.polyfrost.luckyutils.config.HotPotatoConfig;

import java.util.Arrays;

public class GauntletUpgradeCounter {
    private static int upgrades = 0;

    private static int ticks = 0;
    private static int seconds = 0;
    private boolean inGame = false;

    private static GauntletUpgradeConfig config;

    public GauntletUpgradeCounter(GauntletUpgradeConfig config) {
        EventManager.INSTANCE.register(this);
        GauntletUpgradeCounter.config = config;
        GauntletUpgradeCounter.config.hud.setText("0");
    }
    private String cleanChatMessage(String text) {
        if (text.startsWith("<")) {
            text = text.substring(2).trim();
        }
        return text;
    }

    @Subscribe
    private void onChatSend(ChatSendEvent event) {
        if (GauntletUpgradeCounter.config.enabled) {
            String text = event.message;
            if (text.equals("/l") || text.equals("/lobby") || text.equals("/l b") || text.equals("/zoo")) {
                inGame = false;
                ticks = 0;
                seconds = 0;
                config.hud.setText(String.valueOf(0));
            }
        }
    }
    @Subscribe
    private void onChatReceive(ChatReceiveEvent event) {
        if(GauntletUpgradeCounter.config.enabled) {
            String text = cleanChatMessage(event.message.getUnformattedTextForChat());
            if (text.equals("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")) {
                if (seconds < 5) {
                    inGame = true;
                } else {
                    inGame = false;
                    ticks = 0;
                    seconds = 0;
                    GauntletUpgradeCounter.config.hud.setText(String.valueOf(0));

                }
            }
            String engOne = "You have received a gauntlet upgrade! Your punches are now more lethal!";
            String engMany = "You have received another gauntlet upgrade! Your punches are now even more lethal!";
            String dnOne = "Du har modtaget en håndopgradering! Dine slag er nu mere dødbringende!";
            String dnMany = "Du har modtaget en anden håndopgradering! Dine slag er nu endnu mere dødbringende!";
            String geOne = "Du hast ein Gauntlet Upgrade erhalten! Deine Schläge sind nun tödlicher!";
            String geMany = "Du hast noch ein Gauntlet Upgrade erhalten! Deine Schläge sind nun noch tödlicher!";
            String esOne = "¡Has recibido una mejora de puños, tus golpes son más letales ahora!";
            String esMany = "¡Has recibido otra mejora de puños, tus golpes son aún más letales ahora!";
            String brOne = "Você recebeu uma melhoria de soco! Seus ataques serão mais letais!";
            String brMany = "Você recebeu outra melhoria de soco! Seus ataques serão ainda mais letais!";
            String[] gauntlets = {engOne, engMany, dnOne, dnMany, geOne, geMany, esOne, esMany, brOne, brMany};
            if (Arrays.asList(gauntlets).contains(text)) {
                upgrades++;
                GauntletUpgradeCounter.config.hud.setText(String.valueOf(upgrades));
                if (GauntletUpgradeCounter.config.notificationLocation == 0) {
                    UChat.actionBar("§6You have §b" + upgrades + " §6Gauntlet Upgrades");
                } else if (GauntletUpgradeCounter.config.notificationLocation == 1) {
                    UChat.chat("§6You have §b" + upgrades + " §6Gauntlet Upgrades");
                }
            }
        }
    }

    @Subscribe
    private void onTick(TickEvent event) {
        if (GauntletUpgradeCounter.config.enabled && inGame && event.stage == Stage.START) {
            if (ticks != 20) {
                ticks++;
            } else {
                seconds++;
                ticks = 0;
            }
        }
    }

    @Command(value = "resetgauntlet", description = "Resets the game state manually if something goes wrong.")
    public static class Reset {
        @Main
        private void handle() {
            upgrades = 0;
            GauntletUpgradeCounter.config.hud.setText(String.valueOf(upgrades));
        }
    }

}
