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
    private static int seconds = 15;
    boolean inGame = false;
    static boolean havePotato = false;

    static HotPotatoConfig config;

    public HotPotatoTimer(HotPotatoConfig config) {
        EventManager.INSTANCE.register(this);
        HotPotatoTimer.config = config;
        HotPotatoTimer.config.hud.setText("No Potato");
    }
    private String cleanChatMessage(String text) {
        if (text.startsWith("<")) {
            text = text.substring(2).trim();
        }
        return text;
    }

    @Subscribe
    private void onChatSend(ChatSendEvent event) {
        if (HotPotatoTimer.config.enabled) {
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
        String text = cleanChatMessage(event.message.getUnformattedTextForChat());
        if (text.equals("▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")) {
            if (seconds == 15) {
                inGame = true;
            } else {
                inGame = false;
            }
        }
        switch (text) {
            case "You have the hot potato! Quickly punch someone to pass it on!":
                startPotatoClock();
                break;
            case "The Hot Potato exploded!":
                resetPotatoClock();
                break;
            case "Du har den varme kartoffel! Skynd dig at slå nogen for at videresende den!":
                startPotatoClock();
                break;
            case "Den varme kartoffel eksploderede!":
                resetPotatoClock();
                break;
            case "Du besitzt die heiße Kartoffel! Schlage schnell jemand anderes, um sie weiterzugeben!":
                startPotatoClock();
                break;
            case "Die heiße Kartoffel ist explodiert!":
                resetPotatoClock();
                break;
            case "¡Tienes la Patata Caliente! ¡Golpea rápidamente a alguien para pasársela!":
                startPotatoClock();
                break;
            case "¡La Patata Caliente explotó!":
                resetPotatoClock();
                break;
            case "Você está com a batata quente! Bata rapidamente em alguém para a passar!":
                startPotatoClock();
                break;
            case "A batata quente explodiu!":
                resetPotatoClock();
                break;
        }
    }

    private static void startPotatoClock() {
        havePotato = true;
        HotPotatoTimer.config.hud.setText("15");
    }

    private static void resetPotatoClock() {
        havePotato = false;
        seconds = 15;
        ticks = 0;
        HotPotatoTimer.config.hud.setText("No Potato");
    }

    @Subscribe
    private void onTick(TickEvent event) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning() && HotPotatoTimer.config.enabled && event.stage == Stage.START && inGame) {
            ItemStack slotOne = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[0];
            if(slotOne == null || !slotOne.getDisplayName().equals("Hot Potato")) {
                resetPotatoClock();
            }
            if(havePotato) {
                if (ticks != 20) {
                    ticks++;
                } else {
                    seconds--;
                    ticks = 0;
                    HotPotatoTimer.config.hud.setText(String.valueOf(seconds));
                    if (seconds == 3) {
                        Minecraft.getMinecraft().thePlayer.playSound("random.orb", .4f,.3f);
                    } else if (seconds == 2) {
                        Minecraft.getMinecraft().thePlayer.playSound("random.orb", .3f,.1f);
                    } else if (seconds == 1) {
                        Minecraft.getMinecraft().thePlayer.playSound("random.orb", .25f,.01f);
                    } else if (seconds == 0) {
                        resetPotatoClock();
                    }
                }
            }
        }
    }
}
