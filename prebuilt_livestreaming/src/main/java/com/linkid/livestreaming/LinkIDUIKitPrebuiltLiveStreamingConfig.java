package com.linkid.livestreaming;

import android.view.View;
import com.zegocloud.uikit.components.audiovideo.ZegoAvatarViewProvider;
import com.zegocloud.uikit.components.audiovideocontainer.ZegoLayout;
import com.zegocloud.uikit.components.common.ZegoPresetResolution;
import com.zegocloud.uikit.plugin.adapter.plugins.beauty.ZegoBeautyPluginConfig;
import com.linkid.livestreaming.core.LinkIDBottomMenuBarConfig;
import com.linkid.livestreaming.core.LinkIDDialogInfo;
import com.linkid.livestreaming.core.LinkIDLiveStreamingEndListener;
import com.linkid.livestreaming.core.LinkIDLiveStreamingPKBattleConfig;
import com.linkid.livestreaming.core.LinkIDLiveStreamingRole;
import com.linkid.livestreaming.core.LinkIDMemberListConfig;
import com.linkid.livestreaming.core.LinkIDMenuBarButtonName;
import com.linkid.livestreaming.core.LinkIDPrebuiltAudioVideoViewConfig;
import com.linkid.livestreaming.core.LinkIDPrebuiltVideoConfig;
import com.linkid.livestreaming.core.LinkIDTranslationText;
import com.linkid.livestreaming.internal.components.LinkIDLeaveLiveStreamingListener;
import com.linkid.livestreaming.widget.LinkIDStartLiveButton;
import com.zegocloud.uikit.service.defines.ZegoMeRemovedFromRoomListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LinkIDUIKitPrebuiltLiveStreamingConfig {

    public LinkIDLiveStreamingRole role = LinkIDLiveStreamingRole.AUDIENCE;
    public boolean turnOnCameraWhenJoining = false;
    public boolean turnOnMicrophoneWhenJoining = false;
    public boolean turnOnCameraWhenCohosted = true;
    public boolean useSpeakerWhenJoining = true;
    public LinkIDPrebuiltAudioVideoViewConfig audioVideoViewConfig = new LinkIDPrebuiltAudioVideoViewConfig();
    public LinkIDBottomMenuBarConfig bottomMenuBarConfig = new LinkIDBottomMenuBarConfig(new ArrayList<>(
        Arrays.asList(LinkIDMenuBarButtonName.TOGGLE_CAMERA_BUTTON, LinkIDMenuBarButtonName.TOGGLE_MICROPHONE_BUTTON,
            LinkIDMenuBarButtonName.SWITCH_CAMERA_FACING_BUTTON)), new ArrayList<>(
        Arrays.asList(LinkIDMenuBarButtonName.TOGGLE_CAMERA_BUTTON, LinkIDMenuBarButtonName.TOGGLE_MICROPHONE_BUTTON,
            LinkIDMenuBarButtonName.SWITCH_CAMERA_FACING_BUTTON, LinkIDMenuBarButtonName.COHOST_CONTROL_BUTTON)),
        new ArrayList<>(Collections.singletonList(LinkIDMenuBarButtonName.COHOST_CONTROL_BUTTON)));
    public LinkIDMemberListConfig memberListConfig;

    /**
     * if confirmDialogInfo is not null,a confirm dialog will show when host stop live or exit button is clicked or
     * back button is pressed. Please use {@link LinkIDTranslationText#stopLiveDialogInfo }  to custom stopLiveDialog
     * dialog texts.
     */
    @Deprecated
    public LinkIDDialogInfo confirmDialogInfo;
    public transient LinkIDLiveStreamingEndListener LinkIDLiveStreamingEndListener;
    public transient LinkIDLeaveLiveStreamingListener leaveLiveStreamingListener;
    public transient ZegoMeRemovedFromRoomListener removedFromRoomListener;
    public LinkIDTranslationText translationText = new LinkIDTranslationText();

    private boolean enableCoHosting;
    public boolean markAsLargeRoom = false;
    public boolean needConfirmWhenOthersTurnOnYourCamera = false;
    public boolean needConfirmWhenOthersTurnOnYourMicrophone = false;
    public LinkIDDialogInfo othersTurnOnYourCameraConfirmDialogInfo;
    public LinkIDDialogInfo othersTurnOnYourMicrophoneConfirmDialogInfo;
    public transient LinkIDStartLiveButton startLiveButton;
    public transient View.OnClickListener onStartLiveButtonPressed;
    public ZegoLayout zegoLayout;
    public LinkIDPrebuiltVideoConfig screenSharingVideoConfig = new LinkIDPrebuiltVideoConfig(
        ZegoPresetResolution.PRESET_540P);
    public LinkIDPrebuiltVideoConfig videoConfig = new LinkIDPrebuiltVideoConfig(ZegoPresetResolution.PRESET_360P);
    public transient ZegoAvatarViewProvider avatarViewProvider;
    public LinkIDLiveStreamingPKBattleConfig pkBattleConfig = new LinkIDLiveStreamingPKBattleConfig();
    public ZegoBeautyPluginConfig beautyConfig = new ZegoBeautyPluginConfig();
    public boolean showMemberButton = true;


    public static LinkIDUIKitPrebuiltLiveStreamingConfig host() {
        return host(false);
    }

    public static LinkIDUIKitPrebuiltLiveStreamingConfig host(boolean enableCoHosting) {
        LinkIDUIKitPrebuiltLiveStreamingConfig config = new LinkIDUIKitPrebuiltLiveStreamingConfig();
        config.role = LinkIDLiveStreamingRole.HOST;
        config.turnOnCameraWhenJoining = true;
        config.turnOnMicrophoneWhenJoining = true;
        config.confirmDialogInfo = new LinkIDDialogInfo();
        if (enableCoHosting) {
            config.bottomMenuBarConfig.audienceButtons = Collections.singletonList(
                LinkIDMenuBarButtonName.COHOST_CONTROL_BUTTON);
        } else {
            config.bottomMenuBarConfig.audienceButtons = new ArrayList<>();
        }
        config.enableCoHosting = enableCoHosting;
        return config;
    }

    public static LinkIDUIKitPrebuiltLiveStreamingConfig audience() {
        return audience(false);
    }

    public static LinkIDUIKitPrebuiltLiveStreamingConfig audience(boolean enableCoHosting) {
        LinkIDUIKitPrebuiltLiveStreamingConfig config = new LinkIDUIKitPrebuiltLiveStreamingConfig();
        config.role = LinkIDLiveStreamingRole.AUDIENCE;
        if (enableCoHosting) {
            config.bottomMenuBarConfig.audienceButtons = Collections.singletonList(
                LinkIDMenuBarButtonName.COHOST_CONTROL_BUTTON);
        } else {
            config.bottomMenuBarConfig.audienceButtons = new ArrayList<>();
        }
        config.enableCoHosting = enableCoHosting;
        return config;
    }

    public boolean isEnableCoHosting() {
        return enableCoHosting;
    }
}
