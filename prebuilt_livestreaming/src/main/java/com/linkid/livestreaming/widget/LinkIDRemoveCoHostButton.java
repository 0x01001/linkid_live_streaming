package com.linkid.livestreaming.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zegocloud.uikit.ZegoUIKit;
import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.linkid.livestreaming.core.LinkIDTranslationText;
import com.linkid.livestreaming.internal.components.LiveInvitationType;
import com.zegocloud.uikit.plugin.common.PluginCallbackListener;
import com.zegocloud.uikit.plugin.invitation.components.ZegoStartInvitationButton;
import com.zegocloud.uikit.plugin.adapter.utils.GenericUtils;
import com.linkid.livestreaming.R;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LinkIDRemoveCoHostButton extends ZegoStartInvitationButton {

    private PluginCallbackListener callbackListener;

    public LinkIDRemoveCoHostButton(@NonNull Context context) {
        super(context);
    }

    public LinkIDRemoveCoHostButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        super.initView();
        type = LiveInvitationType.REMOVE_COHOST.getValue();
        LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
        if (translationText != null) {
            setText(translationText.removeCoHostButton);
        }
        setBackground(null);
        setTextColor(Color.WHITE);
        setTextSize(14);
        setGravity(Gravity.CENTER);
    }

    @Override
    protected void invokedWhenClick() {
        List<String> idList = GenericUtils.map(invitees, zegoUIKitUser -> zegoUIKitUser.userID);
        LinkIDLiveStreamingManager.getInstance().sendCoHostRequest(idList, timeout, type, data, new PluginCallbackListener() {
            @Override
            public void callback(Map<String, Object> result) {
                if (callbackListener != null) {
                    callbackListener.callback(result);
                }
            }
        });
    }

    public void setRequestCallbackListener(PluginCallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    public void setInvitee(ZegoUIKitUser userInfo) {
        setInvitees(Collections.singletonList(userInfo));
    }
}
