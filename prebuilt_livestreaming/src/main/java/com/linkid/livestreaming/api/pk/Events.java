package com.linkid.livestreaming.api.pk;

import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.linkid.livestreaming.internal.core.PKListener;

public class Events {

    public void addPKListener(PKListener listener) {
        LinkIDLiveStreamingManager.getInstance().addPKListener(listener);
    }

    public void removePKListener(PKListener listener) {
        LinkIDLiveStreamingManager.getInstance().removePKListener(listener);
    }
}
