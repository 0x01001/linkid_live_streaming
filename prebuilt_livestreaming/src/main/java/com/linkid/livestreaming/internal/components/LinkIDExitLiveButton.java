package com.linkid.livestreaming.internal.components;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.linkid.livestreaming.core.LinkIDDialogInfo;

public class LinkIDExitLiveButton extends AppCompatImageView {

    private LinkIDDialogInfo confirmDialogInfo;
    private LinkIDLeaveLiveStreamingListener leaveLiveListener;

    public LinkIDExitLiveButton(@NonNull Context context) {
        super(context);
        initView();
    }

    public LinkIDExitLiveButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView() {
        setOnClickListener(null);
        setImageResource(com.zegocloud.uikit.R.drawable.zego_uikit_icon_nav_close);
        setOnClickListener(v -> {
            invokeWhenClick();
        });
    }

    public void setConfirmDialogInfo(LinkIDDialogInfo info) {
        confirmDialogInfo = info;
    }

    private void invokeWhenClick() {
        boolean isActivity = getContext() instanceof Activity;
        if (isActivity && confirmDialogInfo != null) {
            showQuitDialog(confirmDialogInfo);
        } else {
            if (leaveLiveListener != null) {
                leaveLiveListener.onLeaveLiveStreaming();
            }
        }
    }

    public void setLeaveLiveListener(LinkIDLeaveLiveStreamingListener listener) {
        this.leaveLiveListener = listener;
    }

    private void showQuitDialog(LinkIDDialogInfo dialogInfo) {
        new ConfirmDialog.Builder(getContext()).setTitle(dialogInfo.title).setMessage(dialogInfo.message)
            .setPositiveButton(dialogInfo.confirmButtonName, (dialog, which) -> {
                if (leaveLiveListener != null) {
                    leaveLiveListener.onLeaveLiveStreaming();
                }
                dialog.dismiss();
            }).setNegativeButton(dialogInfo.cancelButtonName, (dialog, which) -> {
                dialog.dismiss();
            }).build().show();
    }

}
