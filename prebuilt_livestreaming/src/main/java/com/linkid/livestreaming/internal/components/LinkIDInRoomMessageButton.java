package com.linkid.livestreaming.internal.components;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import com.zegocloud.uikit.ZegoUIKit;
import com.linkid.livestreaming.R;
import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.linkid.livestreaming.core.LinkIDInRoomMessageInputBoard;
import com.zegocloud.uikit.service.defines.ZegoRoomPropertyUpdateListener;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LinkIDInRoomMessageButton extends AppCompatImageView {

    private ZegoRoomPropertyUpdateListener roomPropertyUpdateListener;
    private LinkIDInRoomMessageInputBoard inputBoard;

    public LinkIDInRoomMessageButton(@NonNull Context context) {
        super(context);
        iniView();
    }

    public LinkIDInRoomMessageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        iniView();
    }

    public LinkIDInRoomMessageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniView();
    }

    private void iniView() {
        if (getContext() instanceof Activity) {
            setOnClickListener(v -> {
                inputBoard = new LinkIDInRoomMessageInputBoard(getContext());
                inputBoard.show();
            });
        }

        setScaleType(ScaleType.FIT_XY);
        StateListDrawable sld = new StateListDrawable();
        sld.addState(new int[]{android.R.attr.state_enabled},
            ContextCompat.getDrawable(getContext(), R.drawable.livestreaming_icon_im));
        sld.addState(new int[]{}, ContextCompat.getDrawable(getContext(), R.drawable.livestreaming_icon_im_disable));
        setImageDrawable(sld);

        roomPropertyUpdateListener = new ZegoRoomPropertyUpdateListener() {
            @Override
            public void onRoomPropertyUpdated(String key, String oldValue, String newValue) {
                if (ZegoUIKit.getLocalUser() == null) {
                    return;
                }
                String userID = ZegoUIKit.getLocalUser().userID;
                boolean isLocalUserHost = Objects.equals(LinkIDLiveStreamingManager.getInstance().getHostID(), userID);
                if (!isLocalUserHost) {
                    if ("enableChat".equals(key)) {
                        if (newValue.equals("0")) {
                            setEnabled(false);
                            if (inputBoard != null && inputBoard.isShowing()) {
                                inputBoard.dismiss();
                            }
                        } else if (newValue.equals("1")) {
                            setEnabled(true);
                        }
                    }
                }
            }

            @Override
            public void onRoomPropertiesFullUpdated(List<String> updateKeys, Map<String, String> oldProperties,
                Map<String, String> properties) {

            }
        };
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ZegoUIKit.addRoomPropertyUpdateListener(roomPropertyUpdateListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ZegoUIKit.removeRoomPropertyUpdateListener(roomPropertyUpdateListener);
    }

}
