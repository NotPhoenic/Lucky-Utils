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
import org.polyfrost.luckyutils.config.MiracleTimerConfig;

import java.util.Arrays;
import java.util.LinkedList;

public class MiracleNotifications {

    private static String formatSeconds(int timeInSeconds) {
        int hours = timeInSeconds / 3600;
        int secondsLeft = timeInSeconds - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (minutes < 10) {
            formattedTime += "0";
        }
        formattedTime += minutes + ":";

        if (seconds < 10) {
            formattedTime += "0";
        }
        formattedTime += seconds;

        return formattedTime;
    }    //https://stackoverflow.com/questions/22545644/how-to-convert-seconds-into-hhmmss

    private final LinkedList<Integer> miracleDrops =  new LinkedList<>(Arrays.asList(458, 720, 1070, 1410, 1430, 1918, 2386, 2631));
    private static int ticks = 0;
    private static int seconds = 0;

    private static boolean inGame = false;

    static MiracleTimerConfig config;


    public MiracleNotifications(MiracleTimerConfig config) {
        EventManager.INSTANCE.register(this);
        MiracleNotifications.config = config;
    }
    private String cleanChatMessage(String text) {
        if (text.startsWith("<")) {
            text = text.substring(2).trim();
        }
        return text;
    }

    @Subscribe
    private void onChatSend(ChatSendEvent event) {
        if (MiracleNotifications.config.enabled) {
            String text = event.message;
            if (text.equals("/l") || text.equals("/lobby") || text.equals("/l b") || text.equals("/zoo")) {
                inGame = false;
                ticks = 0;
                seconds = 0;
                config.hud.setText(formatSeconds(0));
            }
        }
    }
    @Subscribe
    private void onChatReceive(ChatReceiveEvent event) {
        if(MiracleNotifications.config.enabled) {
            String text = cleanChatMessage(event.message.getUnformattedTextForChat());
            if (text.equals("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")) {
                if (seconds < 5) {
                    inGame = true;
                    MiracleNotifications.config.hud.setText(formatSeconds(miracleDrops.getFirst() - seconds));
                } else {
                    inGame = false;
                    ticks = 0;
                    seconds = 0;
                    config.hud.setText(formatSeconds(0));
                }
            }
        }
    }
    @Subscribe
    private void onTick(TickEvent event) {
        if (config.enabled && inGame && event.stage == Stage.START && !miracleDrops.isEmpty()) {
            if (ticks != 20) {
                ticks++;
            } else {
                seconds++;
                ticks = 0;
                if (miracleDrops.getFirst() - seconds == 30) {
                    if (MiracleNotifications.config.notificationLocation == 0) {
                        UChat.actionBar("§cMiracle Blocks drop in 30 seconds!");
                    } else if (MiracleNotifications.config.notificationLocation == 1){
                        UChat.chat("§cMiracle Blocks drop in 30 seconds!");
                    }
                } else if (miracleDrops.getFirst() == seconds) {
                    if (MiracleNotifications.config.notificationLocation == 0) {
                        UChat.actionBar("§cMiracle Blocks dropped!");
                    } else if (MiracleNotifications.config.notificationLocation == 1){
                        UChat.chat("§cMiracle Blocks dropped!");
                    }
                    if (MiracleNotifications.config.notificationSound) {
                        Minecraft.getMinecraft().thePlayer.playSound("random.orb", .25f,.5f);
                    }
                    if (!miracleDrops.isEmpty()) {
                        miracleDrops.removeFirst();
                    } else {
                        MiracleNotifications.config.hud.setText("No Next");
                    }
                }
                MiracleNotifications.config.hud.setText(formatSeconds(miracleDrops.getFirst() - seconds));

            }
        }
    }
    @Command(value = "resetmiracle", description = "Resets the game state manually if something goes wrong.")
    public static class Reset {
        @Main
        private void handle() {
            inGame = false;
            ticks = 0;
            seconds = 0;
            MiracleNotifications.config.hud.setText(formatSeconds(0));
        }
    }
}

