package com.linkid.livestreaming.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zegocloud.uikit.ZegoUIKit;
import com.zegocloud.uikit.plugin.common.PluginCallbackListener;
import com.zegocloud.uikit.plugin.invitation.components.ZegoAcceptInvitationButton;
import com.linkid.livestreaming.R;
import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.linkid.livestreaming.core.LinkIDTranslationText;
import java.util.Map;

public class LinkIDAcceptCoHostButton extends ZegoAcceptInvitationButton {

    private PluginCallbackListener callbackListener;

    public LinkIDAcceptCoHostButton(@NonNull Context context) {
        super(context);
    }

    public LinkIDAcceptCoHostButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        super.initView();
        LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
        if (translationText != null) {
            setText(translationText.agree);
        }
        setTextColor(Color.WHITE);
        setTextSize(16);
        setGravity(Gravity.CENTER);
    }

    @Override
    protected void invokedWhenClick() {
        ZegoUIKit.getSignalingPlugin().acceptInvitation(inviterID, "", new PluginCallbackListener() {
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
}
