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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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

    private static LinkedList<Integer> miracleDrops = new LinkedList<>(Arrays.asList(458, 720, 1070, 1410, 1430, 1918, 2386, 2631));
    private static int ticks = 0;
    private static int seconds = 0;

    private static boolean inGame = false;

    static MiracleTimerConfig config;


    public MiracleNotifications(MiracleTimerConfig config) {
        EventManager.INSTANCE.register(this);
        MiracleNotifications.config = config;
        MiracleNotifications.config.hud.setText(formatSeconds(0));

    }
    @Subscribe
    private void onChatSend(ChatSendEvent event) {
        if (!MiracleNotifications.config.enabled) {
            return;
        }
        String text = event.message;
        if (text.equals("/l") || text.equals("/l b") || text.equals("/zoo")) {
            inGame = false;
            ticks = 0;
            seconds = 0;
            miracleDrops = new LinkedList<>(Arrays.asList(458, 720, 1070, 1410, 1430, 1918, 2386, 2631));
            MiracleNotifications.config.hud.setText(formatSeconds(0));
        }
        if (text.equals("/rejoin")) {
            inGame = true;
        }
    }

    @Subscribe
    private void onChatReceive(ChatReceiveEvent event) {
        if (!MiracleNotifications.config.enabled) {
            return;
        }

        if (event.message.getUnformattedText().equals("                       Bed Wars Lucky Blocks")) {
            if (seconds < 5) {
                inGame = true;
                MiracleNotifications.config.hud.setText(formatSeconds(miracleDrops.getFirst() - seconds));
                if (event.message.getUnformattedText().equals("                            Reward Summary")
                        || event.message.getUnformattedText().equals("                         Slumber Items Gained")) {
                    inGame = false;
                    ticks = 0;
                    seconds = 0;
                    MiracleNotifications.config.hud.setText(formatSeconds(0));
                    miracleDrops = new LinkedList<>(Arrays.asList(458, 720, 1070, 1410, 1430, 1918, 2386, 2631));
                }
            }
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
        if (!config.enabled || !inGame || event.stage != Stage.START || miracleDrops.isEmpty()) {
            return;
        }
        if (ticks != 20) {
            ticks++;
            return;
        }
        seconds++;
        ticks = 0;

        int dropTimeDifference = miracleDrops.getFirst() - seconds;

        if (dropTimeDifference == 30) {
            notifyMiracleBlocks("Miracle Blocks drop in 30 seconds!");
        }
        if (dropTimeDifference == 0) {
            notifyMiracleBlocks("Miracle Blocks dropped!");
            if (MiracleNotifications.config.notificationSound) {
                Minecraft.getMinecraft().thePlayer.playSound("random.orb", 0.25f, 0.5f);
            }
            emptySafetyCheck();
        }
        MiracleNotifications.config.hud.setText(formatSeconds(miracleDrops.getFirst() - seconds));
    }
    private void notifyMiracleBlocks(String message) {
        if (MiracleNotifications.config.notificationLocation == 0) {
            UChat.actionBar("§c" + message);
        } else if (MiracleNotifications.config.notificationLocation == 1) {
            UChat.chat("§c" + message);
        }
    }

    private void emptySafetyCheck() {
        if (!miracleDrops.isEmpty()) {
            miracleDrops.removeFirst();
        } else {
            MiracleNotifications.config.hud.setText("No Next");
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
            miracleDrops = new LinkedList<>(Arrays.asList(458, 720, 1070, 1410, 1430, 1918, 2386, 2631));
        }
    }
}

