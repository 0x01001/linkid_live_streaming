package com.linkid.livestreaming.internal.components;

import android.content.Context;
import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.linkid.livestreaming.core.LinkIDTranslationText;
import com.linkid.livestreaming.widget.LinkIDAcceptCoHostButton;
import com.linkid.livestreaming.widget.LinkIDRefuseCoHostButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

public class ReceiveCoHostInviteDialog {

    private final ConfirmDialog dialog;

    public ReceiveCoHostInviteDialog(Context context, ZegoUIKitUser inviter, int type, String data) {
        LinkIDAcceptCoHostButton acceptButton = new LinkIDAcceptCoHostButton(context);
        acceptButton.setInviterID(inviter.userID);
        LinkIDRefuseCoHostButton refuseButton = new LinkIDRefuseCoHostButton(context);
        refuseButton.setInviterID(inviter.userID);
        refuseButton.setRequestCallbackListener(result -> {
            int code = (int) result.get("code");
            if (code == 0) {
                dismiss();
            }
        });
        acceptButton.setRequestCallbackListener(result -> {
            int code = (int) result.get("code");
            if (code == 0) {
                dismiss();
            }
        });
        String title = "";
        String message = "";
        LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
        if (translationText != null && translationText.receivedCoHostRequestDialogInfo != null) {
            title = translationText.receivedCoHostRequestDialogInfo.title;
            message = translationText.receivedCoHostRequestDialogInfo.message;
        }
        dialog = new ConfirmDialog.Builder(context).setTitle(title)
            .setMessage(message)
            .setCustomPositiveButton(acceptButton)
            .setCustomNegativeButton(refuseButton).build();
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
