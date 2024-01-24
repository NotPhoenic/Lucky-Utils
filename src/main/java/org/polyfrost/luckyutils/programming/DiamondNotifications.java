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
import org.polyfrost.luckyutils.config.DiamondTimerConfig;

import java.util.Arrays;
import java.util.LinkedList;


public class DiamondNotifications {

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

    private final LinkedList<Integer>  diamondDrops = new LinkedList<>(Arrays.asList(210, 360, 518, 676, 834, 990, 1080, 1160, 1240, 1320, 1400, 1480, 1560, 1640,
            1720, 1800, 1880, 1960, 2040, 2120, 2200, 2280, 2360, 2440, 2520, 2600, 2680, 2760, 2840, 2920, 3000));
    private static int ticks = 0;
    private static int seconds = 0;

    private static boolean inGame = false;

    private static DiamondTimerConfig config;

    public DiamondNotifications(DiamondTimerConfig config) {
        EventManager.INSTANCE.register(this);
        DiamondNotifications.config = config;
    }
    private String cleanChatMessage(String text) {
        if (text.startsWith("<")) {
            text = text.substring(2).trim();
        }
        return text;
    }

    @Subscribe
    private void onChatSend(ChatSendEvent event) {
        if (DiamondNotifications.config.enabled) {
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
        if(DiamondNotifications.config.enabled) {
            String text = cleanChatMessage(event.message.getUnformattedTextForChat());
            if (text.equals("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")) {
                if (seconds < 5) {
                    inGame = true;
                    DiamondNotifications.config.hud.setText(formatSeconds(diamondDrops.getFirst() - seconds));
                } else {
                    inGame = false;
                    ticks = 0;
                    seconds = 0;
                    DiamondNotifications.config.hud.setText(formatSeconds(0));
                }
            }
        }
    }

    @Subscribe
    private void onTick(TickEvent event) {
        if (DiamondNotifications.config.enabled && inGame && event.stage == Stage.START && !diamondDrops.isEmpty()) {
            if (ticks != 20) {
                ticks++;
            } else {
                seconds++;
                ticks = 0;
                if (diamondDrops.getFirst() - seconds == 30) {
                    if (DiamondNotifications.config.notificationLocation == 0) {
                        UChat.actionBar("§bDiamond Blocks drop in 30 seconds!");
                    } else if (DiamondNotifications.config.notificationLocation == 1){
                        UChat.chat("§bDiamond Blocks drop in 30 seconds!");
                    }
                } else if (diamondDrops.getFirst() == seconds) {
                    if (DiamondNotifications.config.notificationLocation == 0) {
                        UChat.actionBar("§bDiamond Blocks dropped!");
                    } else if (DiamondNotifications.config.notificationLocation == 1){
                        UChat.chat("§bDiamond Blocks dropped!");
                    }
                    if (DiamondNotifications.config.notificationSound) {
                        Minecraft.getMinecraft().thePlayer.playSound("random.orb", .25f,.5f);
                    }
                    if (!diamondDrops.isEmpty()) {
                        diamondDrops.removeFirst();
                    } else {
                        DiamondNotifications.config.hud.setText("No Next");

                    }
                }
                DiamondNotifications.config.hud.setText(formatSeconds(diamondDrops.getFirst() - seconds));
            }
        }
    }
    @Command(value = "resetdiamond", description = "Resets the game state manually if something goes wrong.")
    public static class Reset {
        @Main
        private void handle() {
            inGame = false;
            ticks = 0;
            seconds = 0;
            DiamondNotifications.config.hud.setText(formatSeconds(0));
        }
    }
}

