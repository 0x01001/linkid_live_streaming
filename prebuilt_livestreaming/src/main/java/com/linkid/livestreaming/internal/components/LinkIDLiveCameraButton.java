package com.linkid.livestreaming.internal.components;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zegocloud.uikit.ZegoUIKit;
import com.zegocloud.uikit.components.common.ZEGOImageButton;
import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;
import com.zegocloud.uikit.service.express.IExpressEngineEventHandler;

/**
 * only for local user
 */
public class LinkIDLiveCameraButton extends ZEGOImageButton {

    public LinkIDLiveCameraButton(@NonNull Context context) {
        super(context);
    }

    public LinkIDLiveCameraButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LinkIDLiveCameraButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView() {
        super.initView();
        setImageResource(com.zegocloud.uikit.R.drawable.zego_uikit_icon_camera_switch,
            com.zegocloud.uikit.R.drawable.zego_uikit_icon_camera_switch_off);
        ZegoUIKit.addEventHandler(new IExpressEngineEventHandler() {
            @Override
            public void onLocalCameraStateUpdate(boolean open) {
                super.onLocalCameraStateUpdate(open);
                updateState(open);
            }
        });
        ZegoUIKitUser localUser = ZegoUIKit.getLocalUser();
        if (localUser != null) {
            updateState(ZegoUIKit.isCameraOn(localUser.userID));
        }
    }

    @Override
    public void open() {
        super.open();
        if (LinkIDLiveStreamingManager.getInstance().getPKInfo() == null) {
            ZegoUIKitUser localUser = ZegoUIKit.getLocalUser();
            if (localUser != null) {
                ZegoUIKit.turnCameraOn(localUser.userID, true);
            }
        } else {
            ZegoUIKit.openCamera(true);
        }
    }

    @Override
    public void close() {
        super.close();
        if (LinkIDLiveStreamingManager.getInstance().getPKInfo() == null) {
            ZegoUIKitUser localUser = ZegoUIKit.getLocalUser();
            if (localUser != null) {
                ZegoUIKit.turnCameraOn(localUser.userID, false);
            }
        } else {
            ZegoUIKit.openCamera(false);
        }
    }
}
