package com.linkid.livestreaming.core;

import java.io.Serializable;

public class LinkIDDialogInfo implements Serializable {

    public String title;
    public String message;
    public String cancelButtonName;
    public String confirmButtonName;

    public LinkIDDialogInfo() {
        this.title = "";
        this.message = "";
        this.cancelButtonName = "";
        this.confirmButtonName = "";
    }

    public LinkIDDialogInfo(String title, String message, String cancelButtonName, String confirmButtonName) {
        this.title = title;
        this.message = message;
        this.cancelButtonName = cancelButtonName;
        this.confirmButtonName = confirmButtonName;
    }
}
