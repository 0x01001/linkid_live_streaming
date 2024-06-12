package com.linkid.livestreaming.internal.core;

import android.view.View;
import android.view.ViewGroup;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;
import java.util.List;

public interface LinkIDLiveStreamingPKBattleViewProvider {

    View getView(ViewGroup parent, List<ZegoUIKitUser> uiKitUsers);
}
