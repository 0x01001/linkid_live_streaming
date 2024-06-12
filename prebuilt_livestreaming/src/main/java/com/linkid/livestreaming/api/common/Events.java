package com.linkid.livestreaming.api.common;

import com.zegocloud.uikit.ZegoUIKit;
import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.zegocloud.uikit.service.defines.ZegoInRoomMessageListener;
import com.zegocloud.uikit.service.express.IExpressEngineEventHandler;

public class Events {

    public void addExpressEngineEventHandler(IExpressEngineEventHandler eventHandler) {
        ZegoUIKit.addEventHandler(eventHandler);
    }

    public void removeExpressEngineEventHandler(IExpressEngineEventHandler eventHandler) {
        ZegoUIKit.removeEventHandler(eventHandler);
    }

    public void addRoleChangedListener(RoleChangedListener listener) {
        LinkIDLiveStreamingManager.getInstance().addRoleListener(listener);
    }

    public void removeRoleChangedListener(RoleChangedListener listener) {
        LinkIDLiveStreamingManager.getInstance().removeRoleListener();
    }

    public void addInRoomMessageReceivedListener(ZegoInRoomMessageListener inRoomMessageListener) {
        ZegoUIKit.addInRoomMessageReceivedListener(inRoomMessageListener);
    }

    public void removeInRoomMessageReceivedListener(ZegoInRoomMessageListener inRoomMessageListener) {
        ZegoUIKit.removeInRoomMessageReceivedListener(inRoomMessageListener);
    }
}
