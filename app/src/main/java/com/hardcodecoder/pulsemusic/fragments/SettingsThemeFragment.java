package com.hardcodecoder.pulsemusic.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.hardcodecoder.pulsemusic.R;
import com.hardcodecoder.pulsemusic.activities.SettingsActivity;
import com.hardcodecoder.pulsemusic.dialog.ThemeChooserBottomSheetDialogFragment;
import com.hardcodecoder.pulsemusic.interfaces.SettingsFragmentsListener;
import com.hardcodecoder.pulsemusic.themes.ThemeManager;
import com.hardcodecoder.pulsemusic.themes.ThemeManagerUtils;
import com.hardcodecoder.pulsemusic.utils.AppSettings;
import com.hardcodecoder.pulsemusic.views.SettingsToggleableItem;

import java.util.Objects;

public class SettingsThemeFragment extends Fragment {

    public static final String TAG = "SettingsThemeFragment";
    private SettingsFragmentsListener mListener;
    private Context mContext;

    public static SettingsThemeFragment getInstance() {
        return new SettingsThemeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_theme, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mListener = (SettingsFragmentsListener) getActivity();
        view.findViewById(R.id.laf_select_dark_theme).setOnClickListener(v -> {
            ThemeChooserBottomSheetDialogFragment dialog = ThemeChooserBottomSheetDialogFragment.getInstance();
            dialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), ThemeChooserBottomSheetDialogFragment.TAG);
        });

        // Get custom views
        SettingsToggleableItem enableDarkThemeLayout = view.findViewById(R.id.laf_enable_dark_mode);
        SwitchMaterial enableDarkThemeSwitch = enableDarkThemeLayout.findViewById(R.id.setting_toggleable_item_switch);
        enableDarkThemeSwitch.setSaveEnabled(false);

        SettingsToggleableItem enableAutoThemeLayout = view.findViewById(R.id.laf_enable_auto_mode);
        SwitchMaterial enableAutoThemeSwitch = enableAutoThemeLayout.findViewById(R.id.setting_toggleable_item_switch);
        enableAutoThemeSwitch.setSaveEnabled(false);

        // Configure state of views based on saved settings
        boolean darkModeEnable = AppSettings.isDarkModeEnabled(mContext);
        enableDarkThemeSwitch.setChecked(darkModeEnable);

        boolean autoModeEnable = AppSettings.isAutoThemeEnabled(mContext);
        enableAutoThemeSwitch.setChecked(autoModeEnable);
        enableDarkThemeSwitch.setEnabled(!autoModeEnable);
        enableDarkThemeLayout.setEnabled(!autoModeEnable);


        //Add listeners to switch views
        enableDarkThemeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                // Only detect changes for user interaction
                // and not for state restoration
                if (ThemeManagerUtils.toggleDarkTheme(mContext, isChecked))
                    applyTheme();
            }
        });
        enableDarkThemeLayout.setOnClickListener(v -> {
            // Trigger switch enable/disable
            enableDarkThemeSwitch.setChecked(!enableDarkThemeSwitch.isChecked());
        });

        enableAutoThemeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                // Only detect changes for user interaction
                // and not for state restoration

                enableDarkThemeLayout.setEnabled(!isChecked);
                enableDarkThemeSwitch.setEnabled(!isChecked);
                if (ThemeManagerUtils.toggleAutoTheme(mContext, isChecked))
                    applyTheme();
                else {
                    // User does not want auto theme based on time of day
                    // Revert to theme selected via darkThemeToggle
                    if (enableDarkThemeSwitch.isChecked() && !ThemeManager.isDarkModeEnabled()) {
                        // User previously select dark theme so when auto theme is
                        // disabled apply dark theme if not already applied
                        applyTheme();
                    } else if (!enableDarkThemeSwitch.isChecked() && ThemeManager.isDarkModeEnabled()) {
                        // User previously select light theme so when auto theme is
                        // disabled apply light theme if not already applied
                        applyTheme();
                    }
                }
            }
        });
        enableAutoThemeLayout.setOnClickListener(v -> {
            // Trigger switch enable/disable
            enableAutoThemeSwitch.setChecked(!enableAutoThemeSwitch.isChecked());
        });

    }

    private void applyTheme() {
        if (mListener instanceof SettingsActivity)
            mListener.onThemeChanged();
    }
}