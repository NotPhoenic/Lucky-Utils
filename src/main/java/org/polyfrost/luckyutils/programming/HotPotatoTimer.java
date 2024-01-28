package org.polyfrost.luckyutils.programming;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.events.event.ChatSendEvent;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.polyfrost.luckyutils.config.HotPotatoConfig;

public class HotPotatoTimer {
    private static int ticks = 0;
    private static int seconds = 14;
    boolean inGame = false;
    static boolean havePotato = false;

    static HotPotatoConfig config;

    public HotPotatoTimer(HotPotatoConfig config) {
        EventManager.INSTANCE.register(this);
        HotPotatoTimer.config = config;
        HotPotatoTimer.config.hud.setText("No Potato");
    }

    @Subscribe
    private void onChatSend(ChatSendEvent event) {
        if (!HotPotatoTimer.config.enabled) {
            return;
        }
        String text = event.message;
        if (text.equals("/l") || text.equals("/l b") || text.equals("/zoo")) {
            inGame = false;
            ticks = 0;
            seconds = 0;
            HotPotatoTimer.config.hud.setText("No Potato");
        }
        if (text.equals("/rejoin")) {
            inGame = true;
        }
    }
    @Subscribe
    private void onChatReceive(ChatReceiveEvent event) {
        if (!HotPotatoTimer.config.enabled) {
            return;
        }
        if (event.message.getUnformattedText().equals("                       Bed Wars Lucky Blocks")) {
            inGame = true;
            return;
        }
        if (event.message.getUnformattedText().equals("                            Reward Summary")
                || event.message.getUnformattedText().equals("                         Slumber Items Gained")) {
            inGame = false;
        }

    }

    private static void startPotatoClock() {
        havePotato = true;
        HotPotatoTimer.config.hud.setText("15");
    }

    private static void resetPotatoClock() {
        havePotato = false;
        seconds = 14;
        ticks = 0;
        HotPotatoTimer.config.hud.setText("No Potato");
    }

    @Subscribe
    private void onTick(TickEvent event) {
        if (!HotPotatoTimer.config.enabled || !inGame || event.stage != Stage.START) {
            return;
        }
        ItemStack slotOne = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[0];
        if (slotOne == null || !slotOne.getDisplayName().equals("§bHot Potato")) {
            resetPotatoClock();
        }
        if (slotOne != null && slotOne.getDisplayName().equals("§bHot Potato")) {
            if (ticks != 20) {
                ticks++;
            } else {
                seconds--;
                ticks = 0;
                HotPotatoTimer.config.hud.setText(String.valueOf(seconds));
                if (seconds == 3) {
                    Minecraft.getMinecraft().thePlayer.playSound("random.orb", .4f, .3f);
                } else if (seconds == 2) {
                    Minecraft.getMinecraft().thePlayer.playSound("random.orb", .3f, .1f);
                } else if (seconds == 1) {
                    Minecraft.getMinecraft().thePlayer.playSound("random.orb", .25f, .01f);
                } else if (seconds == 0) {
                    resetPotatoClock();
                }
            }
        }
    }
}
