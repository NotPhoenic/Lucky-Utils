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
import java.util.Map;
import java.util.HashMap;

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

    private LinkedList<Integer>  diamondDrops = new LinkedList<>(Arrays.asList(200, 360, 520, 680, 830, 990, 1080, 1160, 1240, 1320, 1400, 1480, 1560, 1640,
            1720, 1800, 1880, 1960, 2040, 2120, 2200, 2280, 2360, 2440, 2520, 2600, 2680, 2760, 2840, 2920, 3000));
    private static int ticks = 0;
    private static int seconds = 0;

    private static boolean inGame = false;

    private static DiamondTimerConfig config;

    public DiamondNotifications(DiamondTimerConfig config) {
        EventManager.INSTANCE.register(this);
        DiamondNotifications.config = config;
        DiamondNotifications.config.hud.setText(formatSeconds(0));
    }

    @Subscribe
    private void onChatSend(ChatSendEvent event) {
        if (!DiamondNotifications.config.enabled) {
            return;
        }
        String text = event.message;
        if (text.equals("/l") || text.equals("/l b") || text.equals("/zoo")) {
            inGame = false;
            ticks = 0;
            seconds = 0;
            diamondDrops = new LinkedList<>(Arrays.asList(200, 360, 520, 680, 830, 990, 1080, 1160, 1240, 1320, 1400, 1480, 1560, 1640,
                    1720, 1800, 1880, 1960, 2040, 2120, 2200, 2280, 2360, 2440, 2520, 2600, 2680, 2760, 2840, 2920, 3000));
            DiamondNotifications.config.hud.setText(formatSeconds(0));
        }
        if (text.equals("/rejoin")) {
            inGame = true;
        }
    }

    @Subscribe
    private void onChatReceive(ChatReceiveEvent event) {
        if (!DiamondNotifications.config.enabled) {
            return;
        }
        if (event.message.getUnformattedText().equals("                       Bed Wars Lucky Blocks")) {
            inGame = true;
            DiamondNotifications.config.hud.setText(formatSeconds(diamondDrops.getFirst() - seconds));
        }
        if (event.message.getUnformattedText().equals("                            Reward Summary")
                || event.message.getUnformattedText().equals("                         Slumber Items Gained")) {
            inGame = false;
            ticks = 0;
            seconds = 0;
            diamondDrops = new LinkedList<>(Arrays.asList(200, 360, 520, 680, 830, 990, 1080, 1160, 1240, 1320, 1400, 1480, 1560, 1640,
                    1720, 1800, 1880, 1960, 2040, 2120, 2200, 2280, 2360, 2440, 2520, 2600, 2680, 2760, 2840, 2920, 3000));
            DiamondNotifications.config.hud.setText(formatSeconds(0));
            return;
        }
        if (seconds != 0) {
            return;
        }

//        //le chat gpt
//        Map<String, Integer> messageToSeconds = new HashMap<String, Integer>() {{
//            put("Diamond Generators have been upgraded to Tier II", 360);
//            put("Emerald Generators have been upgraded to Tier II", 720);
//            put("Diamond Generators have been upgraded to Tier III", 1080);
//            put("Emerald Generators have been upgraded to Tier III", 1440);
//            put("All beds have been destroyed!", 1800);
//            put("SUDDEN DEATH:", 2400);
//        }};
//
//        String message = event.message.getUnformattedText();
//        for (String key : messageToSeconds.keySet()) {
//            if (message.startsWith(key)) {
//                seconds = messageToSeconds.get(key);
//                inGame = true;
//                break;
//            }
//        }
//        //le chat gpt
    }

    @Subscribe
    private void onTick(TickEvent event) {
        if (!DiamondNotifications.config.enabled || !inGame || event.stage != Stage.START || diamondDrops.isEmpty()) {
            return;
        }

        if (ticks % 20 != 0) {
            ticks++;
            return;
        }
        seconds++;
        ticks = 0;

        int dropTimeDifference = diamondDrops.getFirst() - seconds;
        if (dropTimeDifference == 30) {
            notifyDiamondBlocks("Diamond Blocks drop in 30 seconds!");
        }
        if (dropTimeDifference == 0) {
            notifyDiamondBlocks("Diamond Blocks dropped!");
            if (DiamondNotifications.config.notificationSound) {
                Minecraft.getMinecraft().thePlayer.playSound("random.orb", 0.25f, 0.5f);
            }
            handleDiamondDrops();
        }
        DiamondNotifications.config.hud.setText(formatSeconds(diamondDrops.getFirst() - seconds));
    }

    private void notifyDiamondBlocks(String message) {
        if (DiamondNotifications.config.notificationLocation == 0) {
            UChat.actionBar("§b" + message);
        } else if (DiamondNotifications.config.notificationLocation == 1) {
            UChat.chat("§b" + message);
        }
    }

    private void handleDiamondDrops() {
        if (!diamondDrops.isEmpty()) {
            diamondDrops.removeFirst();
        } else {
            DiamondNotifications.config.hud.setText("No Next");
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

