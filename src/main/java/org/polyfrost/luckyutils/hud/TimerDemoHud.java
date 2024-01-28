package org.polyfrost.luckyutils.hud;

import cc.polyfrost.oneconfig.hud.SingleTextHud;

public class TimerDemoHud extends SingleTextHud {
    public TimerDemoHud() {
        super("", true);
    }
    private String text = "00:00";

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getText(boolean example) {
        return text;
    }
}