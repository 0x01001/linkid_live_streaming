package com.linkid.livestreaming.core;

import java.util.ArrayList;
import java.util.List;

public class LinkIDBottomMenuBarConfig {

    public List<LinkIDMenuBarButtonName> hostButtons = new ArrayList<>();
    public List<LinkIDMenuBarButtonName> coHostButtons = new ArrayList<>();
    public List<LinkIDMenuBarButtonName> audienceButtons = new ArrayList<>();
    public int menuBarButtonsMaxCount = 4;
    public boolean showInRoomMessageButton = true;

    public LinkIDBottomMenuBarConfig() {

    }

    public LinkIDBottomMenuBarConfig(
        List<LinkIDMenuBarButtonName> hostButtons,
        List<LinkIDMenuBarButtonName> coHostButtons,
        List<LinkIDMenuBarButtonName> audienceButtons) {
        this.hostButtons = hostButtons;
        this.coHostButtons = coHostButtons;
        this.audienceButtons = audienceButtons;
    }
}
