package com.linkid.livestreaming.internal.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import com.linkid.livestreaming.R;
import com.linkid.livestreaming.core.LinkIDMemberButton;
import com.zegocloud.uikit.utils.Utils;

public class LinkIDCountedMemberButton extends FrameLayout {

    private ImageFilterView imageFilterView;

    public LinkIDCountedMemberButton(@NonNull Context context) {
        super(context);
        initView();
    }

    public LinkIDCountedMemberButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LinkIDCountedMemberButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public LinkIDCountedMemberButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr,
        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LinkIDMemberButton memberButton = new LinkIDMemberButton(getContext());
        memberButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.livestreaming_list_people, 0, 0, 0);
        int drawablePadding = Utils.dp2px(4, getResources().getDisplayMetrics());
        memberButton.setCompoundDrawablePadding(drawablePadding);
        memberButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        memberButton.setTextColor(Color.WHITE);
        memberButton.setGravity(Gravity.CENTER);
        int padding = Utils.dp2px(12, getResources().getDisplayMetrics());
        memberButton.setPadding(padding, 0, padding, 0);
        addView(memberButton);

        imageFilterView = new ImageFilterView(getContext());
        imageFilterView.setRoundPercent(1);
        imageFilterView.setBackgroundColor(Color.parseColor("#FF0D23"));
        hideRedPoint();

        int size = Utils.dp2px(8, getResources().getDisplayMetrics());
        FrameLayout.LayoutParams countParam = new LayoutParams(size, size);
        countParam.gravity = Gravity.END | Gravity.BOTTOM;
        addView(imageFilterView, countParam);
    }

    public void showRedPoint() {
        imageFilterView.setVisibility(VISIBLE);
    }

    public void hideRedPoint() {
        imageFilterView.setVisibility(GONE);
    }
}
