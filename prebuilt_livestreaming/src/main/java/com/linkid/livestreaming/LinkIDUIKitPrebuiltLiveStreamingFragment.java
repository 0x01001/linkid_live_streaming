package com.linkid.livestreaming;

import android.Manifest.permission;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.zegocloud.uikit.ZegoUIKit;
import com.zegocloud.uikit.components.audiovideocontainer.ZegoAudioVideoComparator;
import com.zegocloud.uikit.components.audiovideocontainer.ZegoAudioVideoViewConfig;
import com.zegocloud.uikit.components.audiovideocontainer.ZegoLayout;
import com.zegocloud.uikit.components.audiovideocontainer.ZegoLayoutGalleryConfig;
import com.zegocloud.uikit.components.audiovideocontainer.ZegoLayoutMode;
import com.zegocloud.uikit.components.audiovideocontainer.ZegoLayoutPictureInPictureConfig;
import com.zegocloud.uikit.components.audiovideocontainer.ZegoViewPosition;
import com.linkid.livestreaming.LinkIDLiveStreamingManager.LinkIDLiveStreamingListener;
import com.linkid.livestreaming.core.PrebuiltUICallBack;
import com.linkid.livestreaming.core.LinkIDDialogInfo;
import com.linkid.livestreaming.core.LinkIDLiveStreamingRole;
import com.linkid.livestreaming.core.LinkIDTranslationText;
import com.linkid.livestreaming.databinding.LivestreamingFragmentLivestreamingBinding;
import com.linkid.livestreaming.internal.components.ConfirmDialog;
import com.linkid.livestreaming.internal.components.LiveMemberList;
import com.linkid.livestreaming.internal.components.ReceiveCoHostRequestDialog;
import com.linkid.livestreaming.internal.components.LinkIDAudioVideoForegroundView;
import com.linkid.livestreaming.internal.components.LinkIDScreenShareForegroundView;
import com.linkid.livestreaming.internal.core.PKService.PKInfo;
import com.linkid.livestreaming.internal.core.RTCRoomProperty;
import com.linkid.livestreaming.widget.LinkIDAcceptCoHostButton;
import com.linkid.livestreaming.widget.LinkIDRefuseCoHostButton;
import com.zegocloud.uikit.service.defines.ZegoAudioVideoUpdateListener;
import com.zegocloud.uikit.service.defines.ZegoMeRemovedFromRoomListener;
import com.zegocloud.uikit.service.defines.ZegoRoomPropertyUpdateListener;
import com.zegocloud.uikit.service.defines.ZegoTurnOnYourCameraRequestListener;
import com.zegocloud.uikit.service.defines.ZegoUIKitCallback;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;
import com.zegocloud.uikit.service.defines.ZegoUserUpdateListener;
import im.zego.zegoexpress.constants.ZegoOrientation;
import im.zego.zegoexpress.constants.ZegoVideoConfigPreset;
import im.zego.zegoexpress.entity.ZegoVideoConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class LinkIDUIKitPrebuiltLiveStreamingFragment extends Fragment implements PrebuiltUICallBack {

    private OnBackPressedCallback onBackPressedCallback;
    private LivestreamingFragmentLivestreamingBinding binding;
    private Map<LinkIDLiveStreamingRole, List<View>> bottomMenuBarExtendedButtons = new HashMap<>();
    private ConfirmDialog receiveCoHostInviteDialog;
    private ReceiveCoHostRequestDialog receiveCoHostRequestDialog;
    private LiveMemberList livememberList;
    private boolean isLocalUserHost = false;
    private boolean hostFirst = true;
    private Runnable hideTipsRunnable = new Runnable() {
        @Override
        public void run() {
            binding.liveToast.setVisibility(View.GONE);
        }
    };
    private View backgroundView;
    private IntentFilter configurationChangeFilter;
    private BroadcastReceiver configurationChangeReceiver;

    public LinkIDUIKitPrebuiltLiveStreamingFragment() {
        // Required empty public constructor
    }

    /**
     * @param appID    You can create a project and obtain the appID from the [ZEGO Console]().
     * @param appSign  You can create a project and obtain the appSign from the [ZEGO Console]().
     * @param userID   The ID of the currently logged-in user. It can be any valid string, typically, you would use the ID from your own user system.
     * @param userName The name of the currently logged-in user. It can be any valid string, typically, you would use the name from your own user system.
     * @param liveID   The ID of the live broadcast. This ID is the unique identifier for the current live broadcast, so you need to ensure its uniqueness. It can be any valid string. Users providing the same liveID will log in to the same live broadcast room.
     * @param config   The configuration for initializing the live broadcast.
     * @return
     */
    public static LinkIDUIKitPrebuiltLiveStreamingFragment newInstance(long appID, String appSign, String userID,
        String userName, String liveID, LinkIDUIKitPrebuiltLiveStreamingConfig config) {

        LinkIDUIKitPrebuiltLiveStreamingFragment fragment = new LinkIDUIKitPrebuiltLiveStreamingFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("appID", appID);
        bundle.putString("appSign", appSign);
        bundle.putString("liveID", liveID);
        bundle.putString("userID", userID);
        bundle.putString("userName", userName);
        fragment.setArguments(bundle);

        LinkIDLiveStreamingManager.getInstance().setPrebuiltConfig(config);
        return fragment;
    }

    /**
     * @param appID    You can create a project and obtain the appID from the [ZEGO Console]().
     * @param token  You can create a project and obtain the ServerSecret from the [ZEGO Console]() and then generate token by your server to avoid leaking your appSign.
     * @param userID   The ID of the currently logged-in user. It can be any valid string, typically, you would use the ID from your own user system.
     * @param userName The name of the currently logged-in user. It can be any valid string, typically, you would use the name from your own user system.
     * @param liveID   The ID of the live broadcast. This ID is the unique identifier for the current live broadcast, so you need to ensure its uniqueness. It can be any valid string. Users providing the same liveID will log in to the same live broadcast room.
     * @param config   The configuration for initializing the live broadcast.
     * @return
     */
    public static LinkIDUIKitPrebuiltLiveStreamingFragment newInstanceWithToken(long appID, String token, String userID,
        String userName, String liveID, LinkIDUIKitPrebuiltLiveStreamingConfig config) {
        LinkIDUIKitPrebuiltLiveStreamingFragment fragment = new LinkIDUIKitPrebuiltLiveStreamingFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("appID", appID);
        bundle.putString("appToken", token);
        bundle.putString("liveID", liveID);
        bundle.putString("userID", userID);
        bundle.putString("userName", userName);
        fragment.setArguments(bundle);

        LinkIDLiveStreamingManager.getInstance().setPrebuiltConfig(config);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        long appID = arguments.getLong("appID");
        String appSign = arguments.getString("appSign");
        String userName = arguments.getString("userName");
        String userID = arguments.getString("userID");
        String token = arguments.getString("appToken");

        if (appID != 0) {
            LinkIDLiveStreamingManager.getInstance().init(requireActivity().getApplication(), appID, appSign);
            if (!TextUtils.isEmpty(token)) {
                ZegoUIKit.renewToken(token);
            }
            LinkIDLiveStreamingManager.getInstance().login(userID, userName, new ZegoUIKitCallback() {
                @Override
                public void onResult(int errorCode) {
                    if (errorCode == 0) {
                        String liveID = getArguments().getString("liveID");
                        if (!TextUtils.isEmpty(liveID)) {
                            LinkIDLiveStreamingManager.getInstance().joinRoom(liveID, errorCode2 -> {
                                if (errorCode2 == 0) {
                                    onRoomJoinSucceed();
                                } else {
                                    onRoomJoinFailed();
                                }
                            });
                        }
                    } else {
                        requireActivity().finish();
                    }
                }
            });
        }

        configurationChangeFilter = new IntentFilter();
        configurationChangeFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");

        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                boolean hostStarted = LinkIDLiveStreamingManager.getInstance().isLiveStarted() && isLocalUserHost;
                boolean isNotHost = !isLocalUserHost;
                LinkIDUIKitPrebuiltLiveStreamingConfig liveConfig = LinkIDLiveStreamingManager.getInstance()
                    .getPrebuiltConfig();
                if (liveConfig.confirmDialogInfo != null && (hostStarted || isNotHost)) {
                    showQuitDialog(getDialogInfo());
                } else {
                    leaveRoom();
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private void leaveRoom() {
        if (configurationChangeReceiver != null) {
            requireActivity().unregisterReceiver(configurationChangeReceiver);
            configurationChangeReceiver = null;
        }
        if (livememberList != null) {
            livememberList.dismiss();
        }

        LinkIDLiveStreamingManager.getInstance().leaveRoom();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = LivestreamingFragmentLivestreamingBinding.inflate(inflater, container, false);
        if (backgroundView != null) {
            binding.liveBackgroundViewParent.removeAllViews();
            binding.liveBackgroundViewParent.addView(backgroundView);
        }
        binding.liveGroup.setVisibility(View.GONE);
        binding.previewGroup.setVisibility(View.GONE);
        return binding.getRoot();
    }

    private void onRoomJoinFailed() {
        requireActivity().finish();
    }

    private void onRoomJoinSucceed() {
        configurationChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ZegoOrientation orientation = ZegoOrientation.ORIENTATION_0;

                if (Surface.ROTATION_0 == requireActivity().getWindowManager().getDefaultDisplay().getRotation()) {
                    orientation = ZegoOrientation.ORIENTATION_0;
                } else if (Surface.ROTATION_180 == requireActivity().getWindowManager().getDefaultDisplay()
                    .getRotation()) {
                    orientation = ZegoOrientation.ORIENTATION_180;
                } else if (Surface.ROTATION_270 == requireActivity().getWindowManager().getDefaultDisplay()
                    .getRotation()) {
                    orientation = ZegoOrientation.ORIENTATION_270;
                } else if (Surface.ROTATION_90 == requireActivity().getWindowManager().getDefaultDisplay()
                    .getRotation()) {
                    orientation = ZegoOrientation.ORIENTATION_90;
                }
                ZegoUIKit.setAppOrientation(orientation);
            }
        };

        requireActivity().registerReceiver(configurationChangeReceiver, configurationChangeFilter);

        LinkIDUIKitPrebuiltLiveStreamingConfig config = LinkIDLiveStreamingManager.getInstance().getPrebuiltConfig();
        String userID = ZegoUIKit.getLocalUser().userID;
        isLocalUserHost = config.role == LinkIDLiveStreamingRole.HOST;

        if (config.turnOnCameraWhenJoining || config.turnOnMicrophoneWhenJoining) {
            requestPermissionIfNeeded((allGranted, grantedList, deniedList) -> {
                if (config.turnOnCameraWhenJoining) {
                    if (grantedList.contains(permission.CAMERA)) {
                        ZegoUIKit.turnCameraOn(userID, true);
                    }
                } else {
                    ZegoUIKit.turnCameraOn(userID, false);
                }
                if (config.turnOnMicrophoneWhenJoining) {
                    if (grantedList.contains(permission.RECORD_AUDIO)) {
                        ZegoUIKit.turnMicrophoneOn(userID, true);
                    }
                } else {
                    ZegoUIKit.turnMicrophoneOn(userID, false);
                }
            });
        }

        LinkIDLiveStreamingManager.getInstance().setPrebuiltUiCallBack(this);

        initPreviewBtns();
        initLiveBtns();

        if (isLocalUserHost) {
            showPreview();
            Map<String, String> map = new HashMap<>();
            map.put(RTCRoomProperty.HOST, userID);
            map.put(RTCRoomProperty.LIVE_STATUS, RTCRoomProperty.LIVE_STATUS_STOP);
            LinkIDLiveStreamingManager.getInstance().updateRoomProperties(map);
            LinkIDLiveStreamingManager.getInstance().resumePlayingAllAudioVideo(false);
        } else {
            showLiveView();
            LinkIDLiveStreamingManager.getInstance().pausePlayingAllAudioVideo(false);
        }

        ZegoUIKit.setAudioOutputToSpeaker(config.useSpeakerWhenJoining);

        addUIKitListeners();

        LinkIDLiveStreamingManager.getInstance().addLiveStreamingListener(new LinkIDLiveStreamingListener() {

            @Override
            public void onPKStarted() {

                binding.livePkLayout.setVisibility(View.VISIBLE);
                binding.liveVideoContainer.setVisibility(View.GONE);

                if (LinkIDLiveStreamingManager.getInstance().isCurrentUserCoHost()) {
                    if (config.translationText != null && config.translationText.coHostEndBecausePK != null) {
                        showTopTips(config.translationText.coHostEndBecausePK, true);
                    }
                }

                if (binding.previewGroup.getVisibility() == View.VISIBLE) {
                    Map<String, String> map = new HashMap<>();
                    map.put(RTCRoomProperty.LIVE_STATUS, RTCRoomProperty.LIVE_STATUS_START);
                    LinkIDLiveStreamingManager.getInstance().updateRoomProperties(map);
                }
            }

            @Override
            public void onPKEnded() {
                binding.livePkLayout.setVisibility(View.INVISIBLE);
                binding.liveVideoContainer.setVisibility(View.VISIBLE);

                String hostID = LinkIDLiveStreamingManager.getInstance().getHostID();
                ZegoUIKitUser hostUser = ZegoUIKit.getUser(hostID);
                binding.liveVideoContainer.updateLayout();

            }
        });
        binding.livePkLayout.onJoinRoomSucceed();
        binding.livePkLayout.setPKBattleConfig(config.pkBattleConfig);
        binding.livePkLayout.setPrebuiltAudioVideoConfig(config.audioVideoViewConfig);
        binding.livePkLayout.setAvatarViewProvider(config.avatarViewProvider);
    }

    private void addUIKitListeners() {
        LinkIDUIKitPrebuiltLiveStreamingConfig config = LinkIDLiveStreamingManager.getInstance().getPrebuiltConfig();
        ZegoUIKit.addAudioVideoUpdateListener(new ZegoAudioVideoUpdateListener() {
            @Override
            public void onAudioVideoAvailable(List<ZegoUIKitUser> userList) {
            }

            @Override
            public void onAudioVideoUnAvailable(List<ZegoUIKitUser> userList) {
                for (ZegoUIKitUser uiKitUser : userList) {
                    if (Objects.equals(uiKitUser.userID, LinkIDLiveStreamingManager.getInstance().getHostID())) {
                        hostFirst = true;
                    }
                }
            }
        });

        ZegoUIKit.addRoomPropertyUpdateListener(new ZegoRoomPropertyUpdateListener() {
            @Override
            public void onRoomPropertyUpdated(String key, String oldValue, String newValue) {
                if (ZegoUIKit.getLocalUser() == null) {
                    return;
                }

                String userID = ZegoUIKit.getLocalUser().userID;
                isLocalUserHost = Objects.equals(LinkIDLiveStreamingManager.getInstance().getHostID(), userID);
                if (Objects.equals(RTCRoomProperty.HOST, key)) {
                    if (newValue != null) {
                        ZegoUIKitUser uiKitUser = ZegoUIKit.getUser(LinkIDLiveStreamingManager.getInstance().getHostID());
                        if (uiKitUser != null) {
                            binding.liveTopHostIcon.setTextOnly(uiKitUser.userName);
                            binding.liveTopHostName.setText(uiKitUser.userName);
                        } else {
                            binding.liveTopHostIcon.setTextOnly("");
                            binding.liveTopHostName.setText("");
                        }
                        // second host in ,pre become audience
                        if (oldValue != null && oldValue.equals(userID) && !isLocalUserHost) {
                            ZegoUIKit.turnCameraOn(userID, false);
                            ZegoUIKit.turnMicrophoneOn(userID, false);
                            LinkIDLiveStreamingManager.getInstance().pausePlayingAllAudioVideo(false);
                            showLiveView();
                            LinkIDLiveStreamingManager.getInstance().setCurrentRole(LinkIDLiveStreamingRole.AUDIENCE);
                            binding.liveBackgroundViewParent.setVisibility(View.VISIBLE);
                            binding.liveVideoContainer.setVisibility(View.GONE);
                        }
                    }
                    binding.liveVideoContainer.updateLayout();
                }
                if (Objects.equals(RTCRoomProperty.LIVE_STATUS, key)) {
                    if (RTCRoomProperty.LIVE_STATUS_START.equals(newValue)) {
                        LinkIDLiveStreamingManager.getInstance().resumePlayingAllAudioVideo(false);
                        binding.liveBackgroundViewParent.setVisibility(View.GONE);
                        if (LinkIDLiveStreamingManager.getInstance().getPKInfo() == null) {
                            binding.liveVideoContainer.setVisibility(View.VISIBLE);
                        }
                        if (oldValue != null) {
                            showLiveView();
                        }
                    } else if (RTCRoomProperty.LIVE_STATUS_STOP.equals(newValue)) {
                        LinkIDLiveStreamingManager.getInstance().pausePlayingAllAudioVideo(false);
                        hostFirst = true;
                        if (RTCRoomProperty.LIVE_STATUS_START.equals(oldValue)) {
                            if (!isLocalUserHost) {
                                ZegoUIKit.turnCameraOn(userID, false);
                                ZegoUIKit.turnMicrophoneOn(userID, false);
                                if (config.LinkIDLiveStreamingEndListener != null) {
                                    config.LinkIDLiveStreamingEndListener.onLiveStreamingEnded();
                                }
                            }

                        }
                        if (isLocalUserHost) {
                            binding.liveBackgroundViewParent.setVisibility(View.GONE);
                            if (LinkIDLiveStreamingManager.getInstance().getPKInfo() == null) {
                                binding.liveVideoContainer.setVisibility(View.VISIBLE);
                            }
                            LinkIDLiveStreamingManager.getInstance().setCurrentRole(LinkIDLiveStreamingRole.HOST);
                        } else {
                            binding.liveBackgroundViewParent.setVisibility(View.VISIBLE);
                            binding.liveVideoContainer.setVisibility(View.GONE);
                            LinkIDLiveStreamingManager.getInstance().setCurrentRole(LinkIDLiveStreamingRole.AUDIENCE);
                            dismissReceiveCoHostInviteDialog();
                            binding.liveBottomMenuBar.onLiveEnd();
                        }
                    }
                }
                if (Objects.equals(RTCRoomProperty.ENABLE_CHAT, key)) {
                    if (!isLocalUserHost) {
                        if (RTCRoomProperty.ENABLE_CHAT_DISABLE.equals(newValue)) {
                            binding.liveBottomMenuBar.enableChat(false);
                        } else if (RTCRoomProperty.ENABLE_CHAT_ENABLE.equals(newValue)) {
                            binding.liveBottomMenuBar.enableChat(true);
                        }
                    }
                }
            }

            @Override
            public void onRoomPropertiesFullUpdated(List<String> updateKeys, Map<String, String> oldProperties,
                Map<String, String> properties) {
                LinkIDUIKitPrebuiltLiveStreamingConfig config = LinkIDLiveStreamingManager.getInstance()
                    .getPrebuiltConfig();
                if (config.role == LinkIDLiveStreamingRole.HOST) {
                    String currentUserID = ZegoUIKit.getLocalUser().userID;
                    boolean thereIsHostInRoom = false;
                    for (int i = 0; i < updateKeys.size(); ++i) {
                        String key = updateKeys.get(i);
                        if (RTCRoomProperty.HOST.equals(key) && (Objects.equals(properties.get(key), "")
                            || Objects.equals(properties.get(key), currentUserID))) {
                            thereIsHostInRoom = true;
                            break;
                        }
                    }
                    if (thereIsHostInRoom) {
                        config.role = LinkIDLiveStreamingRole.AUDIENCE;
                    }
                }
            }
        });
        ZegoUIKit.addUserUpdateListener(new ZegoUserUpdateListener() {
            @Override
            public void onUserJoined(List<ZegoUIKitUser> userInfoList) {
                for (ZegoUIKitUser uiKitUser : userInfoList) {
                    if (Objects.equals(uiKitUser.userID, LinkIDLiveStreamingManager.getInstance().getHostID())) {
                        binding.liveTopHostIcon.setText(uiKitUser.userName, false);
                        binding.liveTopHostName.setText(uiKitUser.userName);
                        break;
                    }
                    LinkIDLiveStreamingManager.getInstance().removeUserStatus(uiKitUser.userID);
                }
            }

            @Override
            public void onUserLeft(List<ZegoUIKitUser> userInfoList) {
                for (ZegoUIKitUser uiKitUser : userInfoList) {
                    LinkIDLiveStreamingManager.getInstance().removeUserStatus(uiKitUser.userID);
                }
                if (!LinkIDLiveStreamingManager.getInstance().isCurrentUserHost()) {
                    PKInfo pkInfo = LinkIDLiveStreamingManager.getInstance().getPKInfo();
                    if (pkInfo != null) {
                        for (ZegoUIKitUser zegosdkUser : userInfoList) {
                            if (zegosdkUser.userID.equals(pkInfo.hostUserID)) {
                                LinkIDLiveStreamingManager.getInstance().stopPKBattleInner();
                            }
                        }
                    }
                }
            }
        });

        ZegoUIKit.addTurnOnYourMicrophoneRequestListener(fromUser -> {
            if (config.needConfirmWhenOthersTurnOnYourCamera) {
                LinkIDDialogInfo dialogInfo = config.othersTurnOnYourMicrophoneConfirmDialogInfo;
                if (dialogInfo != null) {
                    String message = dialogInfo.message;
                    if (message.contains("%s")) {
                        message = String.format(message, fromUser.userName);
                    }
                    new ConfirmDialog.Builder(getContext()).setTitle(dialogInfo.title).setMessage(message)
                        .setPositiveButton(dialogInfo.confirmButtonName, (dialog, which) -> {
                            dialog.dismiss();
                            requestPermissionIfNeeded((allGranted, grantedList, deniedList) -> {
                                String userID = ZegoUIKit.getLocalUser().userID;
                                if (grantedList.contains(permission.RECORD_AUDIO)) {
                                    ZegoUIKit.turnMicrophoneOn(userID, true);
                                }
                            });
                        }).setNegativeButton(dialogInfo.cancelButtonName, (dialog, which) -> {
                            dialog.dismiss();
                        }).build().show();
                }
            } else {
                requestPermissionIfNeeded((allGranted, grantedList, deniedList) -> {
                    String userID = ZegoUIKit.getLocalUser().userID;
                    if (grantedList.contains(permission.RECORD_AUDIO)) {
                        ZegoUIKit.turnMicrophoneOn(userID, true);
                    }
                });
            }
        });

        ZegoUIKit.addTurnOnYourCameraRequestListener(new ZegoTurnOnYourCameraRequestListener() {
            @Override
            public void onTurnOnYourCameraRequest(ZegoUIKitUser fromUser) {
                if (config.needConfirmWhenOthersTurnOnYourMicrophone) {
                    LinkIDDialogInfo dialogInfo = config.othersTurnOnYourCameraConfirmDialogInfo;
                    if (dialogInfo != null) {
                        String message = dialogInfo.message;
                        if (message.contains("%s")) {
                            message = String.format(message, fromUser.userName);
                        }
                        new ConfirmDialog.Builder(getContext()).setTitle(dialogInfo.title).setMessage(message)
                            .setPositiveButton(dialogInfo.confirmButtonName, (dialog, which) -> {
                                dialog.dismiss();
                                requestPermissionIfNeeded((allGranted, grantedList, deniedList) -> {
                                    String userID = ZegoUIKit.getLocalUser().userID;
                                    if (grantedList.contains(permission.CAMERA)) {
                                        ZegoUIKit.turnCameraOn(userID, true);
                                    }
                                });
                            }).setNegativeButton(dialogInfo.cancelButtonName, (dialog, which) -> {
                                dialog.dismiss();
                            }).build().show();
                    }
                } else {
                    requestPermissionIfNeeded((allGranted, grantedList, deniedList) -> {
                        String userID = ZegoUIKit.getLocalUser().userID;
                        if (grantedList.contains(permission.CAMERA)) {
                            ZegoUIKit.turnCameraOn(userID, true);
                        }
                    });
                }
            }
        });
        ZegoUIKit.addOnMeRemovedFromRoomListener(new ZegoMeRemovedFromRoomListener() {
            @Override
            public void onMeRemovedFromRoom() {
                LinkIDUIKitPrebuiltLiveStreamingConfig config = LinkIDLiveStreamingManager.getInstance()
                    .getPrebuiltConfig();
                if (config.removedFromRoomListener == null) {
                    leaveRoom();
                    requireActivity().finish();
                } else {
                    config.removedFromRoomListener.onMeRemovedFromRoom();
                }
            }
        });
    }

    public void setBackgroundView(View view) {
        this.backgroundView = view;
    }

    private void initLiveBtns() {
        LinkIDUIKitPrebuiltLiveStreamingConfig config = LinkIDLiveStreamingManager.getInstance().getPrebuiltConfig();
        if (config.role != LinkIDLiveStreamingRole.HOST) {
            binding.liveBackgroundViewParent.setVisibility(View.VISIBLE);
            binding.liveVideoContainer.setVisibility(View.GONE);
        }
        LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
        if (translationText != null && translationText.noHostOnline != null) {
            binding.liveNoHostHint.setText(translationText.noHostOnline);
        }
        initVideoContainer();

        if (config.confirmDialogInfo != null) {
            binding.liveTopExit.setConfirmDialogInfo(getDialogInfo());
        }
        binding.liveTopExit.setLeaveLiveListener(() -> {
            if (config.leaveLiveStreamingListener != null) {
                config.leaveLiveStreamingListener.onLeaveLiveStreaming();
            } else {
                leaveRoom();
                requireActivity().finish();
            }
        });

        binding.liveTopMemberCount.setOnClickListener(v -> {
            livememberList = new LiveMemberList(getContext());
            if (config.memberListConfig != null) {
                livememberList.setMemberListItemViewProvider(config.memberListConfig.memberListItemViewProvider);
            }
            if (config.avatarViewProvider != null) {
                livememberList.setAvatarViewProvider(config.avatarViewProvider);
            }
            livememberList.setEnableCoHosting(config.isEnableCoHosting());
            livememberList.show();
        });

        binding.liveBottomMenuBar.setConfig(config.bottomMenuBarConfig);
        for (Entry<LinkIDLiveStreamingRole, List<View>> entry : bottomMenuBarExtendedButtons.entrySet()) {
            binding.liveBottomMenuBar.addExtendedButtons(entry.getValue(), entry.getKey());
        }
        if (config.role == LinkIDLiveStreamingRole.HOST) {
            LinkIDLiveStreamingManager.getInstance().setCurrentRole(LinkIDLiveStreamingRole.HOST);
        } else if (config.role == LinkIDLiveStreamingRole.COHOST) {
            LinkIDLiveStreamingManager.getInstance().setCurrentRole(LinkIDLiveStreamingRole.COHOST);
        } else {
            LinkIDLiveStreamingManager.getInstance().setCurrentRole(LinkIDLiveStreamingRole.AUDIENCE);
        }

        binding.liveBottomMenuBar.setScreenShareVideoConfig(config.screenSharingVideoConfig);

    }

    private LinkIDDialogInfo getDialogInfo() {
        LinkIDUIKitPrebuiltLiveStreamingConfig config = LinkIDLiveStreamingManager.getInstance().getPrebuiltConfig();
        if (TextUtils.isEmpty(config.confirmDialogInfo.title)) {
            LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
            if (translationText != null && translationText.stopLiveDialogInfo != null) {
                config.confirmDialogInfo.title = translationText.stopLiveDialogInfo.title;
            }
        }
        if (TextUtils.isEmpty(config.confirmDialogInfo.message)) {
            LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
            if (translationText != null && translationText.stopLiveDialogInfo != null) {
                config.confirmDialogInfo.message = translationText.stopLiveDialogInfo.message;
            }
        }
        if (TextUtils.isEmpty(config.confirmDialogInfo.confirmButtonName)) {
            LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
            if (translationText != null && translationText.stopLiveDialogInfo != null) {
                config.confirmDialogInfo.confirmButtonName = translationText.stopLiveDialogInfo.confirmButtonName;
            }
        }
        if (TextUtils.isEmpty(config.confirmDialogInfo.cancelButtonName)) {
            LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
            if (translationText != null && translationText.stopLiveDialogInfo != null) {
                config.confirmDialogInfo.cancelButtonName = translationText.stopLiveDialogInfo.cancelButtonName;
            }
        }

        return config.confirmDialogInfo;
    }

    private void initPreviewBtns() {
        LinkIDUIKitPrebuiltLiveStreamingConfig config = LinkIDLiveStreamingManager.getInstance().getPrebuiltConfig();
        binding.previewBack.setOnClickListener(v -> {
            if (config.leaveLiveStreamingListener != null) {
                config.leaveLiveStreamingListener.onLeaveLiveStreaming();
            } else {
                leaveRoom();
                requireActivity().finish();
            }
        });
        binding.previewSwitch.setVisibility(config.turnOnCameraWhenJoining ? View.VISIBLE : View.GONE);

        if (config.translationText != null && config.translationText.startLiveStreamingButton != null) {
            binding.previewStart.setText(config.translationText.startLiveStreamingButton);
        }
        binding.previewStart.setOnClickListener(v -> {
            if (config.onStartLiveButtonPressed != null) {
                config.onStartLiveButtonPressed.onClick(v);
            }
        });
        if (config.startLiveButton != null) {
            binding.previewStartParent.removeView(binding.previewStart);
            binding.previewStartParent.addView(config.startLiveButton);
        }
    }

    private void showPreview() {
        binding.liveGroup.setVisibility(View.GONE);
        binding.previewGroup.setVisibility(View.VISIBLE);
    }

    public void showLiveView() {
        LinkIDUIKitPrebuiltLiveStreamingConfig config = LinkIDLiveStreamingManager.getInstance().getPrebuiltConfig();
        binding.liveGroup.setVisibility(View.VISIBLE);
        binding.liveTopMemberCount.setVisibility(config.showMemberButton ? View.VISIBLE : View.GONE);
        binding.previewGroup.setVisibility(View.GONE);
    }

    private void initVideoContainer() {
        LinkIDUIKitPrebuiltLiveStreamingConfig config = LinkIDLiveStreamingManager.getInstance().getPrebuiltConfig();
        if (config.zegoLayout == null) {
            ZegoLayout layout = new ZegoLayout();
            layout.mode = ZegoLayoutMode.PICTURE_IN_PICTURE;
            ZegoLayoutPictureInPictureConfig pipConfig = new ZegoLayoutPictureInPictureConfig();
            pipConfig.smallViewDefaultPosition = ZegoViewPosition.BOTTOM_RIGHT;
            pipConfig.removeViewWhenAudioVideoUnavailable = true;
            layout.config = pipConfig;
            binding.liveVideoContainer.setLayout(layout);
        } else {
            binding.liveVideoContainer.setLayout(config.zegoLayout);
        }
        if (config.audioVideoViewConfig != null) {
            ZegoAudioVideoViewConfig audioVideoViewConfig = new ZegoAudioVideoViewConfig();
            audioVideoViewConfig.showSoundWavesInAudioMode = config.audioVideoViewConfig.showSoundWaveOnAudioView;
            audioVideoViewConfig.useVideoViewAspectFill = config.audioVideoViewConfig.useVideoViewAspectFill;
            binding.liveVideoContainer.setAudioVideoConfig(audioVideoViewConfig);

            if (config.videoConfig != null) {
                ZegoVideoConfigPreset zegoVideoConfigPreset = ZegoVideoConfigPreset.getZegoVideoConfigPreset(
                    config.videoConfig.resolution.value());
                ZegoUIKit.setVideoConfig(new ZegoVideoConfig(zegoVideoConfigPreset));
            }
        }
        binding.liveVideoContainer.setAudioVideoComparator(new ZegoAudioVideoComparator() {
            @Override
            public List<ZegoUIKitUser> sortAudioVideo(List<ZegoUIKitUser> userList) {
                ZegoUIKitUser host = ZegoUIKit.getUser(LinkIDLiveStreamingManager.getInstance().getHostID());
                if (userList.contains(host)) {
                    if (hostFirst) {
                        userList.remove(host);
                        userList.add(0, host);
                        hostFirst = false;
                    }
                }
                return userList;
            }
        });

        if (config.audioVideoViewConfig != null) {
            binding.liveVideoContainer.setAudioVideoForegroundViewProvider(config.audioVideoViewConfig.provider);
        } else {
            binding.liveVideoContainer.setAudioVideoForegroundViewProvider((parent, uiKitUser) -> {
                LinkIDAudioVideoForegroundView foregroundView = new LinkIDAudioVideoForegroundView(parent.getContext(),
                    uiKitUser.userID);
                return foregroundView;
            });
        }

        if (config.avatarViewProvider != null) {
            binding.liveVideoContainer.setAvatarViewProvider(config.avatarViewProvider);
        }

        binding.liveVideoContainer.setScreenShareForegroundViewProvider((parent, uiKitUser) -> {
            LinkIDScreenShareForegroundView foregroundView = new LinkIDScreenShareForegroundView(parent, uiKitUser.userID);
            foregroundView.setParentContainer(binding.liveVideoContainer);

            if (config.zegoLayout.config instanceof ZegoLayoutGalleryConfig) {
                ZegoLayoutGalleryConfig galleryConfig = (ZegoLayoutGalleryConfig) config.zegoLayout.config;
                foregroundView.setShowFullscreenModeToggleButtonRules(
                    galleryConfig.showScreenSharingFullscreenModeToggleButtonRules);
            }

            return foregroundView;
        });
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

        PermissionX.init(requireActivity()).permissions(permissions).onExplainRequestReason((scope, deniedList) -> {
            String message = "";
            String camera = "";
            String mic = "";
            String ok = "";
            String micAndCamera = "";
            LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
            if (translationText != null) {
                camera = translationText.permissionExplainCamera;
                mic = translationText.permissionExplainMic;
                micAndCamera = translationText.permissionExplainMicAndCamera;
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
            scope.showRequestReasonDialog(deniedList, message, ok);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        leaveRoom();
    }

    private void showQuitDialog(LinkIDDialogInfo dialogInfo) {
        new ConfirmDialog.Builder(getContext()).setTitle(dialogInfo.title).setMessage(dialogInfo.message)
            .setPositiveButton(dialogInfo.confirmButtonName, (dialog, which) -> {
                if (onBackPressedCallback != null) {
                    onBackPressedCallback.setEnabled(false);
                }
                dialog.dismiss();
                leaveRoom();
                requireActivity().onBackPressed();
            }).setNegativeButton(dialogInfo.cancelButtonName, (dialog, which) -> {
                dialog.dismiss();
            }).build().show();
    }

    /**
     *
     * @param viewList The list of custom buttons to be added.
     * @param role The role to which these buttons will be added for display.
     */
    public void addButtonToBottomMenuBar(List<View> viewList, LinkIDLiveStreamingRole role) {
        bottomMenuBarExtendedButtons.put(role, viewList);
        if (binding != null) {
            binding.liveBottomMenuBar.addExtendedButtons(viewList, role);
        }
    }

    public void clearBottomMenuBarExtendButtons(LinkIDLiveStreamingRole role) {
        bottomMenuBarExtendedButtons.remove(role);
        if (binding != null) {
            binding.liveBottomMenuBar.clearExtendedButtons(role);
        }
    }

    @Override
    public void showReceiveCoHostRequestDialog(ZegoUIKitUser inviter, int type, String data) {
        receiveCoHostRequestDialog = new ReceiveCoHostRequestDialog(getContext(), inviter, type, data);
        receiveCoHostRequestDialog.show();
    }

    @Override
    public void dismissReceiveCoHostRequestDialog() {
        if (receiveCoHostRequestDialog != null) {
            receiveCoHostRequestDialog.dismiss();
        }
    }

    @Override
    public void showReceiveCoHostInviteDialog(ZegoUIKitUser inviter, int type, String data) {
        Context context = getContext();
        LinkIDAcceptCoHostButton acceptButton = new LinkIDAcceptCoHostButton(context);
        acceptButton.setInviterID(inviter.userID);
        LinkIDRefuseCoHostButton refuseButton = new LinkIDRefuseCoHostButton(context);
        refuseButton.setInviterID(inviter.userID);
        refuseButton.setRequestCallbackListener(v -> {
            if (receiveCoHostInviteDialog != null) {
                receiveCoHostInviteDialog.dismiss();
            }
            LinkIDLiveStreamingManager.getInstance().removeUserStatus(inviter.userID);
        });
        acceptButton.setRequestCallbackListener(v -> {
            if (receiveCoHostInviteDialog != null) {
                receiveCoHostInviteDialog.dismiss();
            }
            LinkIDLiveStreamingManager.getInstance().removeUserStatus(inviter.userID);
            showCoHostButtons();
        });

        String title = "";
        String message = "";
        String cancelButtonName = "";
        String confirmButtonName = "";
        LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
        if (translationText != null) {
            LinkIDDialogInfo dialogInfo = translationText.receivedCoHostInvitationDialogInfo;
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
        acceptButton.setText(confirmButtonName);
        refuseButton.setText(cancelButtonName);
        receiveCoHostInviteDialog = new ConfirmDialog.Builder(context).setTitle(title).setMessage(message)
            .setCustomPositiveButton(acceptButton).setCustomNegativeButton(refuseButton).build();
        receiveCoHostInviteDialog.show();
    }

    @Override
    public void dismissReceiveCoHostInviteDialog() {
        if (receiveCoHostInviteDialog != null) {
            receiveCoHostInviteDialog.dismiss();
        }
    }

    @Override
    public void removeCoHost(ZegoUIKitUser inviter, int type, String data) {
        ZegoUIKitUser localUser = ZegoUIKit.getLocalUser();
        if (localUser == null) {
            return;
        }
        ZegoUIKit.turnCameraOn(localUser.userID, false);
        ZegoUIKit.turnMicrophoneOn(localUser.userID, false);
        LinkIDLiveStreamingManager.getInstance().setCurrentRole(LinkIDLiveStreamingRole.AUDIENCE);
    }

    @Override
    public void showRequestCoHostButton() {
        binding.liveBottomMenuBar.showRequestCoHostButton();
    }

    @Override
    public void showCoHostButtons() {
        requestPermissionIfNeeded(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, @NonNull List<String> grantedList,
                @NonNull List<String> deniedList) {
                String localUserID = ZegoUIKit.getLocalUser().userID;
                LinkIDUIKitPrebuiltLiveStreamingConfig config = LinkIDLiveStreamingManager.getInstance()
                    .getPrebuiltConfig();
                ZegoUIKit.turnCameraOn(localUserID, config.turnOnCameraWhenCohosted);
                ZegoUIKit.turnMicrophoneOn(localUserID, true);
                LinkIDLiveStreamingManager.getInstance().setCurrentRole(LinkIDLiveStreamingRole.COHOST);
            }
        });
    }

    @Override
    public void showRedPoint() {
        binding.liveTopMemberCount.showRedPoint();
        if (livememberList != null) {
            livememberList.updateList();
        }
    }

    @Override
    public void hideRedPoint() {
        binding.liveTopMemberCount.hideRedPoint();
        if (livememberList != null) {
            livememberList.updateList();
        }
    }

    @Override
    public void showTopTips(String tips, boolean green) {
        binding.liveToast.setText(tips);
        binding.liveToast.setVisibility(View.VISIBLE);
        if (green) {
            binding.liveToast.setBackgroundColor(Color.parseColor("#55BC9E"));
        } else {
            binding.liveToast.setBackgroundColor(Color.parseColor("#BD5454"));
        }
        Handler handler = binding.getRoot().getHandler();
        if (handler != null) {
            handler.removeCallbacks(hideTipsRunnable);
            handler.postDelayed(hideTipsRunnable, 2000);
        }
    }
}