package com.linkid.livestreaming.widget;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.FragmentActivity;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.linkid.livestreaming.core.LinkIDTranslationText;
import com.linkid.livestreaming.internal.core.RTCRoomProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinkIDStartLiveButton extends androidx.appcompat.widget.AppCompatButton {

    protected GestureDetectorCompat gestureDetectorCompat;

    public LinkIDStartLiveButton(@NonNull Context context) {
        super(context);
        initView();
    }

    public LinkIDStartLiveButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LinkIDStartLiveButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        gestureDetectorCompat = new GestureDetectorCompat(getContext(), new SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                requestPermissionIfNeeded((allGranted, grantedList, deniedList) -> {
                    Map<String, String> map = new HashMap<>();
                    map.put(RTCRoomProperty.LIVE_STATUS,RTCRoomProperty.LIVE_STATUS_START);
                    LinkIDLiveStreamingManager.getInstance().updateRoomProperties(map);
                });
                performClick();
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetectorCompat.onTouchEvent(event);
    }

    private void requestPermissionIfNeeded(RequestCallback requestCallback) {
        List<String> permissions = Arrays.asList(permission.CAMERA, permission.RECORD_AUDIO);

        boolean allGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
            }
        }
        if (allGranted) {
            requestCallback.onResult(true, permissions, new ArrayList<>());
            return;
        }

        if (getContext() instanceof FragmentActivity) {
            PermissionX.init((FragmentActivity) getContext()).permissions(permissions).onExplainRequestReason((scope, deniedList) -> {
                String message = "";
                String camera = "";
                String mic = "";
                String settings = "";
                String cancel = "";
                String ok = "";
                String micAndCamera = "";
                String settingsCamera = "";
                String settingsMic = "";
                String settingsMicAndCamera = "";
                LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
                if (translationText != null) {
                    camera = translationText.permissionExplainCamera;
                    mic = translationText.permissionExplainMic;
                    micAndCamera = translationText.permissionExplainMicAndCamera;
                    settings = translationText.settings;
                    cancel = translationText.cancel;
                    settingsCamera = translationText.settingCamera;
                    settingsMic = translationText.settingMic;
                    settingsMicAndCamera = translationText.settingMicAndCamera;
                    ok = translationText.ok;
                }
                if (deniedList.size() == 1) {
                    if (deniedList.contains(permission.CAMERA)) {
                        message = camera;
                    } else if (deniedList.contains(permission.RECORD_AUDIO)) {
                        message = mic;
                    }
                } else {
                    message = micAndCamera;
                }
                scope.showRequestReasonDialog(deniedList, message,ok);
            }).onForwardToSettings((scope, deniedList) -> {
                String message = "";
                String settings = "";
                String cancel = "";
                String settingsCamera = "";
                String settingsMic = "";
                String settingsMicAndCamera = "";
                LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
                if (translationText != null) {
                    settings = translationText.settings;
                    cancel = translationText.cancel;
                    settingsCamera = translationText.settingCamera;
                    settingsMic = translationText.settingMic;
                    settingsMicAndCamera = translationText.settingMicAndCamera;
                }
                if (deniedList.size() == 1) {
                    if (deniedList.contains(permission.CAMERA)) {
                        message = settingsCamera;
                    } else if (deniedList.contains(permission.RECORD_AUDIO)) {
                        message = settingsMic;
                    }
                } else {
                    message = settingsMicAndCamera;
                }
                scope.showForwardToSettingsDialog(deniedList, message, settings, cancel);
            }).request(new RequestCallback() {
                @Override
                public void onResult(boolean allGranted, @NonNull List<String> grantedList,
                    @NonNull List<String> deniedList) {
                    if (requestCallback != null) {
                        requestCallback.onResult(allGranted, grantedList, deniedList);
                    }
                }
            });
        } else {
            requestCallback.onResult(false, new ArrayList<>(), new ArrayList<>());
        }
    }
}
