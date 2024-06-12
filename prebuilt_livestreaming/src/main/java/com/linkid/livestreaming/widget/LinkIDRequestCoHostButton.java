package com.linkid.livestreaming.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zegocloud.uikit.ZegoUIKit;
import com.zegocloud.uikit.components.common.ZTextButton;
import com.zegocloud.uikit.plugin.common.PluginCallbackListener;
import com.linkid.livestreaming.R;
import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.linkid.livestreaming.core.LinkIDTranslationText;
import com.linkid.livestreaming.internal.components.LiveInvitationType;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;
import com.zegocloud.uikit.utils.Utils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LinkIDRequestCoHostButton extends ZTextButton {

    public LinkIDRequestCoHostButton(@NonNull Context context) {
        super(context);
        initView();
    }

    public LinkIDRequestCoHostButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private PluginCallbackListener callbackListener;

    @Override
    protected void initView() {
        super.initView();
        setBackgroundResource(R.drawable.livestreaming_bg_cohost_btn);
        LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
        if (translationText != null) {
            setText(translationText.requestCoHostButton);
        }
        setTextColor(Color.WHITE);
        setTextSize(13);
        setGravity(Gravity.CENTER);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        setPadding(Utils.dp2px(14, displayMetrics), 0, Utils.dp2px(16, displayMetrics), 0);
        setCompoundDrawablePadding(Utils.dp2px(6, displayMetrics));
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.livestreaming_bottombar_cohost, 0, 0, 0);
    }

    @Override
    protected boolean beforeClick() {
        boolean isPKStarted = LinkIDLiveStreamingManager.getInstance().getPKInfo() != null;
        if (isPKStarted) {
            LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
            if (translationText != null && translationText.coHostEndBecausePK != null) {
                LinkIDLiveStreamingManager.getInstance().showTopTips(translationText.coHostEndBecausePK, true);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void afterClick() {
        super.afterClick();
        if (!LinkIDLiveStreamingManager.getInstance().isLiveStarted()) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", -1);
            LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
            if (translationText != null && translationText.requestCoHostFailed != null) {
                map.put("message", translationText.requestCoHostFailed);
            }
            if (callbackListener != null) {
                callbackListener.callback(map);
            }
            LinkIDLiveStreamingManager.getInstance().showTopTips((String) map.get("message"), false);
            return;
        }

        String hostUserID = LinkIDLiveStreamingManager.getInstance().getHostID();
        ZegoUIKitUser hostUser = ZegoUIKit.getUser(hostUserID);
        if (TextUtils.isEmpty(hostUserID) || hostUser == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", -1);
            LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
            if (translationText != null && translationText.requestCoHostFailed != null) {
                map.put("message", translationText.requestCoHostFailed);
            }
            if (callbackListener != null) {
                callbackListener.callback(map);
            }
            LinkIDLiveStreamingManager.getInstance().showTopTips((String) map.get("message"), false);
            return;
        }

        LinkIDLiveStreamingManager.getInstance()
            .sendCoHostRequest(Collections.singletonList(hostUserID), 60, LiveInvitationType.REQUEST_COHOST.getValue(),
                "",
                new PluginCallbackListener() {
                    @Override
                    public void callback(Map<String, Object> result) {
                        int code = (int) result.get("code");
                        if (code != 0) {
                            LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance()
                                .getTranslationText();
                            if (translationText != null && translationText.requestCoHostFailed != null) {
                                result.put("message", translationText.requestCoHostFailed);
                            }
                        } else {
                            LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance()
                                .getTranslationText();
                            if (translationText != null && translationText.sendRequestCoHostToast != null) {
                                result.put("message", translationText.sendRequestCoHostToast);
                            }
                            LinkIDLiveStreamingManager.getInstance().setUserStatus(hostUserID,
                                LinkIDLiveStreamingManager.SEND_COHOST_REQUEST);
                        }
                        LinkIDLiveStreamingManager.getInstance().showTopTips((String) result.get("message"), code == 0);
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
