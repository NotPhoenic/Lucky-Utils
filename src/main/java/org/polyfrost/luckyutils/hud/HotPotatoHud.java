package org.polyfrost.luckyutils.hud;

import cc.polyfrost.oneconfig.hud.SingleTextHud;

public class HotPotatoHud extends SingleTextHud {
    public HotPotatoHud() {
        super("", true);
    }
    private String text = "No Potato";

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getText(boolean example) {
        return text;
    }
}
