package com.linkid.livestreaming.core;

public enum LinkIDLiveStreamingRole {
    HOST(0), COHOST(1), AUDIENCE(2);

    private int value;

    LinkIDLiveStreamingRole(int var3) {
        this.value = var3;
    }

    public int getValue() {
        return this.value;
    }

}
