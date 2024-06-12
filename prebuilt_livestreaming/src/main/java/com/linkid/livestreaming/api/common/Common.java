package com.linkid.livestreaming.api.common;

import com.zegocloud.uikit.ZegoUIKit;
import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.zegocloud.uikit.service.defines.ZegoInRoomMessageSendStateListener;

public class Common {

    public Events events = new Events();

    public void unMuteAllAudioVideo() {
        LinkIDLiveStreamingManager.getInstance().unMuteAllAudioVideo();
    }

    public void muteAllAudioVideo() {
        LinkIDLiveStreamingManager.getInstance().muteAllAudioVideo();
    }

    /**
     *  message show on top UI
     * @param message  message content
     * @param green  message color ,green or red
     */
    public void showTopTips(String message, boolean green) {
        LinkIDLiveStreamingManager.getInstance().showTopTips(message, green);
    }

    /**
     *
     * @param message  in room message content
     * @param listener  send message result
     */
    public void sendInRoomMessage(String message, ZegoInRoomMessageSendStateListener listener) {
        ZegoUIKit.sendInRoomMessage(message, listener);
    }
}
