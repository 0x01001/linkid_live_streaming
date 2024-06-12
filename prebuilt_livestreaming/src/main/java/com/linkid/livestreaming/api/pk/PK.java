package com.linkid.livestreaming.api.pk;

import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.linkid.livestreaming.internal.core.PKService.PKInfo;
import com.linkid.livestreaming.internal.core.PKService.PKRequest;
import com.linkid.livestreaming.internal.core.UserRequestCallback;
import com.zegocloud.uikit.service.defines.ZegoUIKitCallback;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

public class PK {

    public Events events = new Events();

    /**
     * Accept an incoming PK battle request.
     *
     * @param requestID         The ID of the PK invitation, indicating agreement to which PK invitation.
     * @param anotherHostLiveID The Live ID of the other host participating in the PK.
     * @param anotherHostUser   Information of the other host participating in the PK.
     */
    public void acceptIncomingPKBattleRequest(String requestID, String anotherHostLiveID,
        ZegoUIKitUser anotherHostUser) {
        LinkIDLiveStreamingManager.getInstance()
            .acceptIncomingPKBattleRequest(requestID, anotherHostLiveID, anotherHostUser);
    }

    /**
     * Reject a PK battle start request.
     *
     * @param requestID The ID of the PK invitation, indicating rejection of which PK invitation.
     */
    public void rejectPKBattleStartRequest(String requestID) {
        LinkIDLiveStreamingManager.getInstance().rejectPKBattleStartRequest(requestID);
    }

    public boolean isAnotherHostMuted() {
        return LinkIDLiveStreamingManager.getInstance().isAnotherHostMuted();
    }

    /**
     * Mute the audio of the other host in the mixed stream.
     *
     * @param mute     Whether to mute the audio of the other host in the mixed stream.
     * @param callback The callback for the result of the operation.
     */
    public void muteAnotherHostAudio(boolean mute, ZegoUIKitCallback callback) {
        LinkIDLiveStreamingManager.getInstance().muteAnotherHostAudio(mute, callback);
    }

    /**
     * Start a PK battle with another host.
     *
     * @param anotherHostLiveID The Live ID of the other host participating in the PK.
     * @param anotherHostUserID The User ID of the other host participating in the PK.
     * @param anotherHostName   The name of the other host participating in the PK.
     */
    public void startPKBattleWith(String anotherHostLiveID, String anotherHostUserID, String anotherHostName) {
        LinkIDLiveStreamingManager.getInstance().startPKBattleWith(anotherHostLiveID, anotherHostUserID, anotherHostName);
    }

    public PKRequest getSendPKStartRequest() {
        return LinkIDLiveStreamingManager.getInstance().getSendPKStartRequest();
    }

    /**
     * Send a PK battle request.
     *
     * @param anotherHostUserID The User ID of the other host participating in the PK.
     * @param timeout           The timeout period.
     * @param customData        Custom data for transmission.
     * @param callback          The listener for the result of sending the invitation.
     */
    public void sendPKBattleRequest(String anotherHostUserID, int timeout, String customData,
        UserRequestCallback callback) {
        LinkIDLiveStreamingManager.getInstance().sendPKBattleRequest(anotherHostUserID, timeout, customData, callback);
    }

    /**
     * Send a PK battle request with default parameters.
     *
     * @param anotherHostUserID The User ID of the other host participating in the PK.
     * @param callback          The listener for the result of sending the invitation.
     */
    public void sendPKBattleRequest(String anotherHostUserID, UserRequestCallback callback) {
        LinkIDLiveStreamingManager.getInstance().sendPKBattleRequest(anotherHostUserID, 60, "", callback);
    }

    public void cancelPKBattleRequest(UserRequestCallback callback) {
        LinkIDLiveStreamingManager.getInstance().cancelPKBattleRequest("", callback);
    }

    /**
     * Cancel a PK battle request.
     *
     * @param customData Custom data for transmission.
     * @param callback   The listener for the result of canceling the invitation.
     */
    public void cancelPKBattleRequest(String customData, UserRequestCallback callback) {
        LinkIDLiveStreamingManager.getInstance().cancelPKBattleRequest(customData, callback);
    }

    public void stopPKBattle() {
        LinkIDLiveStreamingManager.getInstance().stopPKBattle();
    }

    public PKInfo getPKInfo() {
        return LinkIDLiveStreamingManager.getInstance().getPKInfo();
    }

}