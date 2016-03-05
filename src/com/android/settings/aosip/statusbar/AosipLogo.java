/*
 * Copyright (C) 2015 The Dirty Unicorns project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.aosip.statusbar;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.android.settings.R;
import com.android.internal.logging.MetricsLogger;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class AosipLogo extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String TAG = "AosipLogo";
    private static final String KEY_AOSIP_LOGO_COLOR = "status_bar_aosip_logo_color";
    private static final String KEY_AOSIP_LOGO_STYLE = "status_bar_aosip_logo_style";

    private ColorPickerPreference mAosipLogoColor;
    private ListPreference mAosipLogoStyle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.aosip_logo);

        PreferenceScreen prefSet = getPreferenceScreen();

            ContentResolver resolver = mContext.getContentResolver();
            mAosipLogoStyle = (ListPreference) findPreference(KEY_AOSIP_LOGO_STYLE);
            int aosipLogoStyle = Settings.System.getIntForUser(resolver,
                    Settings.System.STATUS_BAR_AOSIP_LOGO_STYLE, 0,
                    UserHandle.USER_CURRENT);
            mAosipLogoStyle.setValue(String.valueOf(aosipLogoStyle));
            mAosipLogoStyle.setSummary(mAosipLogoStyle.getEntry());
            mAosipLogoStyle.setOnPreferenceChangeListener(this);

        // AOSIP logo color
        mAosipLogoColor =
            (ColorPickerPreference) prefSet.findPreference(KEY_AOSIP_LOGO_COLOR);
        mAosipLogoColor.setOnPreferenceChangeListener(this);
        int intColor = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_AOSIP_LOGO_COLOR, 0xffffffff);
        String hexColor = String.format("#%08x", (0xffffffff & intColor));
            mAosipLogoColor.setSummary(hexColor);
            mAosipLogoColor.setNewPreviewColor(intColor);

    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
            ContentResolver resolver = mContext.getContentResolver();
        if (preference == mAosipLogoColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.STATUS_BAR_AOSIP_LOGO_COLOR, intHex);
            return true;
            } else if (preference == mAosipLogoStyle) {
                int aosipLogoStyle = Integer.valueOf((String) newValue);
                int index = mAosipLogoStyle.findIndexOfValue((String) newValue);
                Settings.System.putIntForUser(
                        resolver, Settings.System.STATUS_BAR_AOSIP_LOGO_STYLE, aosipLogoStyle,
                        UserHandle.USER_CURRENT);
                mAosipLogoStyle.setSummary(
                        mAosipLogoStyle.getEntries()[index]);
                return true;
        }
        return false;
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.OWLSNEST;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

}
