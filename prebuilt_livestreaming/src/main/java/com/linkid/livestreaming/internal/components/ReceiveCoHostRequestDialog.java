package com.linkid.livestreaming.internal.components;

import android.content.Context;
import com.linkid.livestreaming.internal.components.ConfirmDialog.Builder;
import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.linkid.livestreaming.R;
import com.linkid.livestreaming.core.LinkIDDialogInfo;
import com.linkid.livestreaming.core.LinkIDTranslationText;
import com.linkid.livestreaming.widget.LinkIDAcceptCoHostButton;
import com.linkid.livestreaming.widget.LinkIDRefuseCoHostButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

public class ReceiveCoHostRequestDialog {

    private final ConfirmDialog dialog;

    public ReceiveCoHostRequestDialog(Context context, ZegoUIKitUser inviter, int type, String data) {
        LinkIDAcceptCoHostButton acceptButton = new LinkIDAcceptCoHostButton(context);
        acceptButton.setInviterID(inviter.userID);
        LinkIDRefuseCoHostButton refuseButton = new LinkIDRefuseCoHostButton(context);
        refuseButton.setInviterID(inviter.userID);
        refuseButton.setRequestCallbackListener(result -> {
            int code = (int) result.get("code");
            if (code == 0) {
                dismiss();
                LinkIDLiveStreamingManager.getInstance().removeUserStatusAndCheck(inviter.userID);
            }
        });
        acceptButton.setRequestCallbackListener(result -> {
            int code = (int) result.get("code");
            if (code == 0) {
                dismiss();
                LinkIDLiveStreamingManager.getInstance().removeUserStatusAndCheck(inviter.userID);
            }
        });

        String title = "";
        String message = "";
        String cancelButtonName = "";
        String confirmButtonName = "";
        LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
        if (translationText != null) {
            LinkIDDialogInfo dialogInfo = translationText.receivedCoHostRequestDialogInfo;
            if (dialogInfo != null && dialogInfo.title != null) {
                title = dialogInfo.title;
            }
            if (dialogInfo != null && dialogInfo.message != null) {
                message = String.format(dialogInfo.message, inviter.userName);
            }
            if (dialogInfo != null && dialogInfo.cancelButtonName != null) {
                cancelButtonName = dialogInfo.cancelButtonName;
            }
            if (dialogInfo != null && dialogInfo.confirmButtonName != null) {
                confirmButtonName = dialogInfo.confirmButtonName;
            }
        }
        acceptButton.setText(cancelButtonName);
        refuseButton.setText(confirmButtonName);
        dialog = new Builder(context).setTitle(title).setMessage(message).setCustomPositiveButton(acceptButton)
            .setCustomNegativeButton(refuseButton).build();
        dialog.getWindow().setDimAmount(0.1f);
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
