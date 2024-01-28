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
import org.polyfrost.luckyutils.config.GauntletUpgradeConfig;
import org.polyfrost.luckyutils.config.TimerDemoConfig;

import java.util.Arrays;

public class TimerDemo {

    private static int seconds = 0;
    private boolean inGame = false;

    private static TimerDemoConfig config;

    public TimerDemo(TimerDemoConfig config) {
        EventManager.INSTANCE.register(this);
        TimerDemo.config = config;
        TimerDemo.config.hud.setText(formatSeconds(0));
        Thread timerThread = new Thread(() -> {
            while (true) {
                try {
                    // Sleep for 1 second
                    Thread.sleep(1000);
                    // Increment the timer every second
                    seconds++;
                    TimerDemo.config.hud.setText(formatSeconds(seconds));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timerThread.start();
    }
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

}
