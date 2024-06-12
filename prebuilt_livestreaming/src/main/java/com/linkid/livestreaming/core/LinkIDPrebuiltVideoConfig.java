package com.linkid.livestreaming.core;

import com.zegocloud.uikit.components.common.ZegoPresetResolution;

public class LinkIDPrebuiltVideoConfig {

    public ZegoPresetResolution resolution = ZegoPresetResolution.PRESET_360P;

    public LinkIDPrebuiltVideoConfig(ZegoPresetResolution resolution) {
        this.resolution = resolution;
    }

    public LinkIDPrebuiltVideoConfig() {
    }
}
