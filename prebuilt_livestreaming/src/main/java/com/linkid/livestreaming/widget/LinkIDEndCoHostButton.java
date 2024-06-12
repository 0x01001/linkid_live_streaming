package com.linkid.livestreaming.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import com.linkid.livestreaming.R;
import com.linkid.livestreaming.LinkIDLiveStreamingManager;
import com.linkid.livestreaming.core.LinkIDTranslationText;
import com.zegocloud.uikit.utils.Utils;

public class LinkIDEndCoHostButton extends androidx.appcompat.widget.AppCompatButton {

    public LinkIDEndCoHostButton(Context context) {
        super(context);
        initView();
    }

    public LinkIDEndCoHostButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LinkIDEndCoHostButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    protected void initView() {
        setBackgroundResource(R.drawable.livestreaming_bg_end_cohost_btn);
        LinkIDTranslationText translationText = LinkIDLiveStreamingManager.getInstance().getTranslationText();
        if (translationText != null) {
            setText(translationText.endCoHostButton);
        }
        setTextColor(Color.WHITE);
        setTextSize(13);
        setGravity(Gravity.CENTER);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        setPadding(Utils.dp2px(14, displayMetrics), 0, Utils.dp2px(16, displayMetrics), 0);
        setCompoundDrawablePadding(Utils.dp2px(6, displayMetrics));
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.livestreaming_bottombar_cohost, 0, 0, 0);
    }
}
