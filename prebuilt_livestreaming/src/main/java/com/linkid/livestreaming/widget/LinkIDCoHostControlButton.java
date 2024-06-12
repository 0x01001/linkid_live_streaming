package com.linkid.livestreaming.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zegocloud.uikit.ZegoUIKit;
import com.zegocloud.uikit.plugin.common.PluginCallbackListener;
import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.linkid.livestreaming.LinkIDLiveStreamingManager.LinkIDLiveStreamingListener;
import com.linkid.livestreaming.core.LinkIDDialogInfo;
import com.linkid.livestreaming.core.LinkIDLiveStreamingRole;
import com.linkid.livestreaming.core.LinkIDTranslationText;
import com.linkid.livestreaming.internal.components.ConfirmDialog.Builder;
import com.linkid.livestreaming.internal.core.PKService.PKInfo;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;
import java.util.Collections;
import java.util.Map;
import timber.log.Timber;

public class LinkIDCoHostControlButton extends FrameLayout {

    private LinkIDRequestCoHostButton requestCoHostButton;
    private LinkIDCancelRequestCoHostButton cancelRequestCoHostButton;
    private LinkIDEndCoHostButton endCoHostButton;

    public LinkIDCoHostControlButton(@NonNull Context context) {
        super(context);
        initView();
    }

    public LinkIDCoHostControlButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LinkIDCoHostControlButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        requestCoHostButton = new LinkIDRequestCoHostButton(getContext());
        requestCoHostButton.setRequestCallbackListener(new PluginCallbackListener() {
            @Override
            public void callback(Map<String, Object> result) {
                int code = (int) result.get("code");
                if (code == 0) {
                    showCancelRequestCoHostButton();
                }
            }
        });
        addView(requestCoHostButton);
        cancelRequestCoHostButton = new LinkIDCancelRequestCoHostButton(getContext());
        cancelRequestCoHostButton.setRequestCallbackListener(new PluginCallbackListener() {
            @Override
            public void callback(Map<String, Object> result) {
                int code = (int) result.get("code");
                if (code == 0) {
                    showRequestCoHostButton();
                }
            }
        });
        addView(cancelRequestCoHostButton);
        endCoHostButton = new LinkIDEndCoHostButton(getContext());
        endCoHostButton.setOnClickListener(v -> {
            if (getContext() instanceof Activity) {
                String title = "";
                String message = "";
                String cancelButtonName = "";
                String confirmButtonName = "";
                LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
                if (translationText != null) {
                    LinkIDDialogInfo dialogInfo = translationText.endConnectionDialogInfo;
                    if (dialogInfo != null && dialogInfo.title != null) {
                        title = dialogInfo.title;
                    }
                    if (dialogInfo != null && dialogInfo.message != null) {
                        message = dialogInfo.message;
                    }
                    if (dialogInfo != null && dialogInfo.cancelButtonName != null) {
                        cancelButtonName = dialogInfo.cancelButtonName;
                    }
                    if (dialogInfo != null && dialogInfo.confirmButtonName != null) {
                        confirmButtonName = dialogInfo.confirmButtonName;
                    }
                }
                new Builder(getContext()).setTitle(title).setMessage(message)
                    .setPositiveButton(confirmButtonName, (dialog, which) -> {
                        LinkIDLiveStreamingManager.getInstance().endCoHost();
                        showRequestCoHostButton();
                        dialog.dismiss();
                    }).setNegativeButton(cancelButtonName, (dialog, which) -> {
                        dialog.dismiss();
                    }).build().show();
            }
        });
        addView(endCoHostButton);
        LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
        if (translationText != null && translationText.requestCoHostButton != null) {
            requestCoHostButton.setText(translationText.requestCoHostButton);
        }
        if (translationText != null && translationText.cancelRequestCoHostButton != null) {
            cancelRequestCoHostButton.setText(translationText.cancelRequestCoHostButton);
        }
        if (translationText != null && translationText.endCoHostButton != null) {
            endCoHostButton.setText(translationText.endCoHostButton);
        }
        showRequestCoHostButton();

        LinkIDLiveStreamingManager.getInstance().addLiveStreamingListener(new LinkIDLiveStreamingListener() {

            @Override
            public void onPKStarted() {
                for (int i = 0; i < getChildCount(); i++) {
                    getChildAt(i).setVisibility(GONE);
                }
            }

            @Override
            public void onPKEnded() {
                LinkIDLiveStreamingRole userRole = LinkIDLiveStreamingManager.getInstance().getCurrentUserRole();
                if (userRole == LinkIDLiveStreamingRole.AUDIENCE) {
                    showRequestCoHostButton();
                }
            }
        });
    }

    public void showRequestCoHostButton() {
        PKInfo pkInfo = LinkIDLiveStreamingManager.getInstance().getPKInfo();
        if (pkInfo != null) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(GONE);
        }
        requestCoHostButton.setVisibility(VISIBLE);
    }

    public void showCancelRequestCoHostButton() {
        PKInfo pkInfo = LinkIDLiveStreamingManager.getInstance().getPKInfo();
        if (pkInfo != null) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(GONE);
        }
        cancelRequestCoHostButton.setVisibility(VISIBLE);
    }

    public void showEndCoHostButton() {
        PKInfo pkInfo = LinkIDLiveStreamingManager.getInstance().getPKInfo();
        if (pkInfo != null) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(GONE);
        }
        endCoHostButton.setVisibility(VISIBLE);
    }

    public void onLiveEnd() {
        if (cancelRequestCoHostButton.getVisibility() == VISIBLE) {
            String hostUserID = LinkIDLiveStreamingManager.getInstance().getHostID();
            ZegoUIKitUser uiKitUser = ZegoUIKit.getUser(hostUserID);
            if (uiKitUser == null) {
                return;
            }
            ZegoUIKit.getSignalingPlugin().cancelInvitation(Collections.singletonList(hostUserID), "", null);
        }
    }
}
