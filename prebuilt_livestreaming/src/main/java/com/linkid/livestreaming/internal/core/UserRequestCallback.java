package com.linkid.livestreaming.internal.core;

public interface UserRequestCallback {

    void onUserRequestSend(int errorCode, String requestID);
}
