package com.hardcodecoder.pulsemusic.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.hardcodecoder.pulsemusic.themes.ColorUtil;
import com.hardcodecoder.pulsemusic.themes.ThemeColors;
import com.hardcodecoder.pulsemusic.themes.TintHelper;


public class AccentColorTextInputLayout extends TextInputLayout {

    public AccentColorTextInputLayout(@NonNull Context context) {
        this(context, null);
    }

    public AccentColorTextInputLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AccentColorTextInputLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        int colorOnSurface = ThemeColors.getCurrentColorOnSurface();

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_focused}, // [0]
                new int[]{android.R.attr.state_hovered}, // [1]
                new int[]{-android.R.attr.state_enabled}, // [2]
                new int[]{}, // [3]
        };
        int[] colors = new int[states.length];

        colors[0] = ThemeColors.getAccentColorForCurrentTheme();
        colors[1] = ColorUtil.changeAlphaComponentTo(colorOnSurface, 0.42f);
        colors[2] = ColorUtil.changeAlphaComponentTo(colorOnSurface, 0.38f);
        colors[3] = ColorUtil.changeAlphaComponentTo(colorOnSurface, 0.42f);

        setDefaultHintTextColor(ColorStateList.valueOf(colorOnSurface));
        setHintTextColor(ThemeColors.getAccentColorStateList());
        setBoxStrokeColorStateList(new ColorStateList(states, colors));

        if (getEditText() == null) return;
        TintHelper.setAccentTintToCursor(getEditText());
    }
}