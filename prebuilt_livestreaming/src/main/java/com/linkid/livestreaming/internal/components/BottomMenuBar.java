package com.linkid.livestreaming.internal.components;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import com.zegocloud.uikit.ZegoUIKit;
import com.zegocloud.uikit.components.audiovideo.ZegoLeaveButton;
import com.zegocloud.uikit.components.audiovideo.ZegoSwitchAudioOutputButton;
import com.zegocloud.uikit.components.audiovideo.ZegoSwitchCameraButton;
import com.zegocloud.uikit.components.common.ZegoScreenSharingToggleButton;
import com.linkid.livestreaming.R;
import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.linkid.livestreaming.LinkIDLiveStreamingManager.LinkIDLiveStreamingListener;
import com.linkid.livestreaming.core.LinkIDBottomMenuBarConfig;
import com.linkid.livestreaming.core.LinkIDLiveStreamingRole;
import com.linkid.livestreaming.core.LinkIDMenuBarButtonName;
import com.linkid.livestreaming.core.LinkIDPrebuiltVideoConfig;
import com.linkid.livestreaming.widget.LinkIDCoHostControlButton;
import com.linkid.livestreaming.widget.LinkIDEnableChatButton;
import com.zegocloud.uikit.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BottomMenuBar extends LinearLayout {

    private List<View> showList = new ArrayList<>();
    private List<View> hideList = new ArrayList<>();
    private Map<LinkIDLiveStreamingRole, List<View>> extendedButtons = new HashMap<>();
    private MoreDialog moreDialog;
    private LinearLayout childLinearLayout;
    private LinkIDBottomMenuBarConfig menuBarConfig = new LinkIDBottomMenuBarConfig();
    private LinkIDPrebuiltVideoConfig screenSharingVideoConfig;
    private Dialog beautyDialog;

    private LinkIDInRoomMessageButton messageButton;
    private Map<LinkIDMenuBarButtonName, View> enumViewMap = new HashMap<>();

    public BottomMenuBar(@NonNull Context context) {
        super(context);
        initView();
    }

    public BottomMenuBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BottomMenuBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(new LayoutParams(-1, -2));
        setGravity(Gravity.CENTER_HORIZONTAL);

        for (LinkIDMenuBarButtonName name : LinkIDMenuBarButtonName.values()) {
            View viewFromType = getViewFromType(name);
            enumViewMap.put(name, viewFromType);
        }

        messageButton = new LinkIDInRoomMessageButton(getContext());
        LinearLayout.LayoutParams btnParam = new LayoutParams(-2, -2);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int marginStart = Utils.dp2px(16, displayMetrics);
        int marginTop = Utils.dp2px(10, displayMetrics);
        btnParam.setMargins(marginStart, marginTop, 0, marginStart);
        addView(messageButton, btnParam);

        childLinearLayout = new LinearLayout(getContext());
        childLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        childLinearLayout.setGravity(Gravity.END);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, -2, 1);
        addView(childLinearLayout, params);
        int paddingEnd = Utils.dp2px(8, getResources().getDisplayMetrics());
        childLinearLayout.setPadding(0, 0, paddingEnd, 0);

        LinkIDLiveStreamingManager.getInstance().addLiveStreamingListener(new LinkIDLiveStreamingListener() {

            @Override
            public void onRoleChanged(LinkIDLiveStreamingRole liveStreamingRole) {
                notifyListChanged();
            }
        });
    }

    private List<View> getMenuBarViews(List<LinkIDMenuBarButtonName> list) {
        List<View> viewList = new ArrayList<>();
        for (LinkIDMenuBarButtonName zegoMenuBarButton : list) {
            View viewFromType = enumViewMap.get(zegoMenuBarButton);
            viewList.add(viewFromType);
        }
        return viewList;
    }

    private void addChildView(View view) {
        childLinearLayout.addView(view);
    }

    private void removeAllChildViews() {
        childLinearLayout.removeAllViews();
    }

    private LayoutParams generateChildImageLayoutParams() {
        int size = Utils.dp2px(36f, getResources().getDisplayMetrics());
        int marginTop = Utils.dp2px(10f, getResources().getDisplayMetrics());
        int marginBottom = Utils.dp2px(16f, getResources().getDisplayMetrics());
        int marginEnd = Utils.dp2px(8, getResources().getDisplayMetrics());
        LayoutParams layoutParams = new LayoutParams(size, size);
        layoutParams.topMargin = marginTop;
        layoutParams.bottomMargin = marginBottom;
        layoutParams.rightMargin = marginEnd;
        return layoutParams;
    }

    private LayoutParams generateChildTextLayoutParams() {
        int size = Utils.dp2px(36f, getResources().getDisplayMetrics());
        int marginTop = Utils.dp2px(10f, getResources().getDisplayMetrics());
        int marginBottom = Utils.dp2px(16f, getResources().getDisplayMetrics());
        int marginEnd = Utils.dp2px(8, getResources().getDisplayMetrics());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, size);
        layoutParams.topMargin = marginTop;
        layoutParams.bottomMargin = marginBottom;
        layoutParams.rightMargin = marginEnd;
        return layoutParams;
    }


    private View getViewFromType(LinkIDMenuBarButtonName name) {
        View view = null;
        switch (name) {
            case TOGGLE_CAMERA_BUTTON: {
                view = new LinkIDLiveCameraButton(getContext());
                LayoutParams params = generateChildImageLayoutParams();
                view.setLayoutParams(params);
            }
            break;
            case TOGGLE_MICROPHONE_BUTTON: {
                view = new LinkIDLiveMicrophoneButton(getContext());
                LayoutParams params = generateChildImageLayoutParams();
                view.setLayoutParams(params);
            }
            break;
            case SWITCH_CAMERA_FACING_BUTTON: {
                view = new ZegoSwitchCameraButton(getContext());
                LayoutParams params = generateChildImageLayoutParams();
                view.setLayoutParams(params);
            }
            break;
            case LEAVE_BUTTON: {
                view = new ZegoLeaveButton(getContext());
                LayoutParams params = generateChildImageLayoutParams();
                view.setLayoutParams(params);
            }
            break;
            case SWITCH_AUDIO_OUTPUT_BUTTON: {
                view = new ZegoSwitchAudioOutputButton(getContext());
                LayoutParams params = generateChildImageLayoutParams();
                view.setLayoutParams(params);
            }
            break;
            case COHOST_CONTROL_BUTTON: {
                view = new LinkIDCoHostControlButton(getContext());
                LayoutParams params = generateChildImageLayoutParams();
                params.width = LayoutParams.WRAP_CONTENT;
                view.setLayoutParams(params);
            }
            break;
            case ENABLE_CHAT_BUTTON: {
                view = new LinkIDEnableChatButton(getContext());
                LayoutParams params = generateChildImageLayoutParams();
                view.setLayoutParams(params);
            }
            break;
            case SCREEN_SHARING_TOGGLE_BUTTON: {
                view = new ZegoScreenSharingToggleButton(getContext());
                ((ZegoScreenSharingToggleButton) view).bottomBarStyle();
                if (screenSharingVideoConfig != null) {
                    ((ZegoScreenSharingToggleButton) view).setPresetResolution(screenSharingVideoConfig.resolution);
                }
                LayoutParams params = generateChildImageLayoutParams();
                view.setLayoutParams(params);
            }
            break;
            case BEAUTY_BUTTON: {
                view = new BeautyButton(getContext());
                LayoutParams params = generateChildImageLayoutParams();

                view.setOnClickListener(v -> {
                    if (beautyDialog == null) {
                        beautyDialog = ZegoUIKit.getBeautyPlugin().getBeautyDialog(getContext());
                    }
                    if (beautyDialog != null) {
                        beautyDialog.show();
                    }
                });
                view.setLayoutParams(params);
                if (ZegoUIKit.getBeautyPlugin().isPluginExited()) {
                    view.setVisibility(VISIBLE);
                } else {
                    view.setVisibility(GONE);
                }
            }
            break;

        }
        if (view != null) {
            view.setTag(name);
        }
        return view;
    }

    public void addExtendedButtons(List<View> viewList, LinkIDLiveStreamingRole role) {
        extendedButtons.put(role, viewList);
        if (role == LinkIDLiveStreamingManager.getInstance().getCurrentUserRole()) {
            notifyListChanged();
        }
    }

    public void clearExtendedButtons(LinkIDLiveStreamingRole role) {
        extendedButtons.remove(role);
        if (role == LinkIDLiveStreamingManager.getInstance().getCurrentUserRole()) {
            notifyListChanged();
        }
    }

    private void showMoreDialog() {
        if (moreDialog == null) {
            moreDialog = new MoreDialog(getContext());
        }
        if (!moreDialog.isShowing()) {
            moreDialog.show();
        }
        moreDialog.setHideChildren(hideList);
    }

    private void notifyListChanged() {
        removeAllChildViews();
        showList.clear();
        hideList.clear();

        LinkIDLiveStreamingRole currentRole = LinkIDLiveStreamingManager.getInstance().getCurrentUserRole();
        List<View> buildInViews = new ArrayList<>();
        List<View> extendedViews = new ArrayList<>();
        if (currentRole == LinkIDLiveStreamingRole.HOST) {
            buildInViews = getMenuBarViews(menuBarConfig.hostButtons);
            extendedViews = extendedButtons.get(LinkIDLiveStreamingRole.HOST);
        } else if (currentRole == LinkIDLiveStreamingRole.COHOST) {
            buildInViews = getMenuBarViews(menuBarConfig.coHostButtons);
            extendedViews = extendedButtons.get(LinkIDLiveStreamingRole.COHOST);
        } else if (currentRole == LinkIDLiveStreamingRole.AUDIENCE) {
            buildInViews = getMenuBarViews(menuBarConfig.audienceButtons);
            extendedViews = extendedButtons.get(LinkIDLiveStreamingRole.AUDIENCE);
        }
        List<View> menuBarViews = new ArrayList<>(buildInViews);
        if (extendedViews != null && !extendedViews.isEmpty()) {
            menuBarViews.addAll(extendedViews);
        }

        if (menuBarViews.size() <= menuBarConfig.menuBarButtonsMaxCount) {
            showList.addAll(menuBarViews);
        } else {
            int showChildCount = menuBarConfig.menuBarButtonsMaxCount - 1;
            if (showChildCount > 0) {
                showList.addAll(menuBarViews.subList(0, showChildCount));
                hideList = menuBarViews.subList(showChildCount, menuBarViews.size());
            }
            MoreButton moreButton = new MoreButton(getContext());
            LayoutParams params = generateChildImageLayoutParams();
            moreButton.setLayoutParams(params);
            showList.add(moreButton);
        }

        for (int i = 0; i < showList.size(); i++) {
            addChildView(showList.get(i));
        }
        if (moreDialog != null) {
            moreDialog.setHideChildren(hideList);
        }
        if (currentRole == LinkIDLiveStreamingRole.COHOST) {
            for (View view : showList) {
                if (view instanceof LinkIDCoHostControlButton) {
                    ((LinkIDCoHostControlButton) view).showEndCoHostButton();
                }
            }
            for (View view : hideList) {
                if (view instanceof LinkIDCoHostControlButton) {
                    ((LinkIDCoHostControlButton) view).showEndCoHostButton();
                }
            }
        } else if (currentRole == LinkIDLiveStreamingRole.AUDIENCE) {
            for (View view : showList) {
                if (view instanceof LinkIDCoHostControlButton) {
                    ((LinkIDCoHostControlButton) view).showRequestCoHostButton();
                }
            }
            for (View view : hideList) {
                if (view instanceof LinkIDCoHostControlButton) {
                    ((LinkIDCoHostControlButton) view).showRequestCoHostButton();
                }
            }
        }
    }

    private void showInRoomMessageButton(boolean show) {
        messageButton.setVisibility(show ? VISIBLE : GONE);
    }

    public void showRequestCoHostButton() {
        for (View view : showList) {
            if (view instanceof LinkIDCoHostControlButton) {
                ((LinkIDCoHostControlButton) view).showRequestCoHostButton();
            }
        }
        for (View view : hideList) {
            if (view instanceof LinkIDCoHostControlButton) {
                ((LinkIDCoHostControlButton) view).showRequestCoHostButton();
            }
        }
    }

    public void onLiveEnd() {
        for (View view : showList) {
            if (view instanceof LinkIDCoHostControlButton) {
                ((LinkIDCoHostControlButton) view).onLiveEnd();
            }
        }
        for (View view : hideList) {
            if (view instanceof LinkIDCoHostControlButton) {
                ((LinkIDCoHostControlButton) view).onLiveEnd();
            }
        }
    }

    public void setConfig(LinkIDBottomMenuBarConfig menuBarConfig) {
        if (menuBarConfig == null) {
            menuBarConfig = new LinkIDBottomMenuBarConfig();
        }
        this.menuBarConfig = menuBarConfig;
        showInRoomMessageButton(menuBarConfig.showInRoomMessageButton);
    }

    public void enableChat(boolean enable) {
        messageButton.setEnabled(enable);
    }

    public void setScreenShareVideoConfig(LinkIDPrebuiltVideoConfig screenSharingVideoConfig) {
        this.screenSharingVideoConfig = screenSharingVideoConfig;
        if (screenSharingVideoConfig == null) {
            return;
        }

        for (View view : showList) {
            if (view instanceof ZegoScreenSharingToggleButton) {
                ((ZegoScreenSharingToggleButton) view).setPresetResolution(screenSharingVideoConfig.resolution);
            }
        }
        for (View view : hideList) {
            if (view instanceof ZegoScreenSharingToggleButton) {
                ((ZegoScreenSharingToggleButton) view).setPresetResolution(screenSharingVideoConfig.resolution);
            }
        }
    }

    public class MoreButton extends AppCompatImageView {

        public MoreButton(@NonNull Context context) {
            super(context);
            initView();
        }

        public MoreButton(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            initView();
        }

        private void initView() {
            StateListDrawable sld = new StateListDrawable();
            sld.addState(new int[]{android.R.attr.state_pressed},
                ContextCompat.getDrawable(getContext(), R.drawable.livestreaming_icon_tab_more));
            sld.addState(new int[]{}, ContextCompat.getDrawable(getContext(), R.drawable.livestreaming_icon_tab_more));
            setImageDrawable(sld);
            setOnClickListener(v -> showMoreDialog());
        }
    }
}
