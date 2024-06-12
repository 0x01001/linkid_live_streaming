package com.linkid.livestreaming.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zegocloud.uikit.components.common.ZTextButton;
import com.zegocloud.uikit.plugin.common.PluginCallbackListener;
import com.linkid.livestreaming.R;
import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.linkid.livestreaming.core.LinkIDTranslationText;
import com.linkid.livestreaming.internal.components.LiveInvitationType;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LinkIDInviteJoinCoHostButton extends ZTextButton {

    private PluginCallbackListener callbackListener;
    private ZegoUIKitUser invitee;

    public LinkIDInviteJoinCoHostButton(@NonNull Context context) {
        super(context);
    }

    public LinkIDInviteJoinCoHostButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        super.initView();
        setTextColor(Color.WHITE);
        setTextSize(14);
        setSingleLine();
        setEllipsize(TruncateAt.END);
        setGravity(Gravity.CENTER);
    }

    public void setInvitee(ZegoUIKitUser invitee) {
        this.invitee = invitee;
        LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
        if (translationText != null) {
            setText(String.format(translationText.inviteCoHostButton,invitee.userName));
        }
    }

    @Override
    protected boolean beforeClick() {
        boolean isPKStarted = LinkIDLiveStreamingManager.getInstance().getPKInfo() != null;
        if (isPKStarted) {
            LinkIDLiveStreamingManager.getInstance().showTopTips("cannot invite coHost because PK", true);
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void afterClick() {
        if (invitee != null) {
            if (LinkIDLiveStreamingManager.getInstance().hasInviteUserCoHost(invitee.userID)) {
                Map<String, Object> map = new HashMap<>();
                map.put("code", -1);
                LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
                if (translationText != null && translationText.repeatInviteCoHostFailedToast != null) {
                    map.put("message", translationText.repeatInviteCoHostFailedToast);
                }
                if (callbackListener != null) {
                    callbackListener.callback(map);
                }
                LinkIDLiveStreamingManager.getInstance().showTopTips((String) map.get("message"), false);
                return;
            }
        }

        LinkIDLiveStreamingManager.getInstance().sendCoHostRequest(Collections.singletonList(invitee.userID), 60,
            LiveInvitationType.INVITE_TO_COHOST.getValue(), "", new PluginCallbackListener() {
                @Override
                public void callback(Map<String, Object> result) {
                    int code = (int) result.get("code");
                    if (code != 0) {
                        LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
                        if (translationText != null && translationText.inviteCoHostFailedToast != null) {
                            result.put("message", translationText.inviteCoHostFailedToast);
                        }
                        LinkIDLiveStreamingManager.getInstance().showTopTips((String) result.get("message"), code == 0);
                    } else {
                        if (invitee != null) {
                            LinkIDLiveStreamingManager.getInstance()
                                .setUserStatus(invitee.userID, LinkIDLiveStreamingManager.INVITE_JOIN_COHOST);
                        }
                    }

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
