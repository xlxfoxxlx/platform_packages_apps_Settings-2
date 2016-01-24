/*
 * Copyright (C) 2015 Androis Open Source Illusion Project
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

package com.android.settings.aosip.qssettings;

import android.app.ActivityManagerNative;
import android.content.Context;
import android.content.ContentResolver;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.preference.Preference;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManagerGlobal;
import android.view.IWindowManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Locale;
import android.text.TextUtils;
import android.view.View;

import com.android.settings.aosip.seekbar.SeekBarPreference;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.MetricsLogger;
import com.android.settings.Utils;

public class TileCategory extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    
    private static final String PREF_QS_TRANSPARENT_SHADE = "qs_transparent_shade";

    private SeekBarPreference mQSShadeAlpha;

    private final Configuration mCurConfig = new Configuration();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.aosip_tiles);
        PreferenceScreen prefSet = getPreferenceScreen();

        ContentResolver resolver = getActivity().getContentResolver();

        // QS shade alpha
        mQSShadeAlpha =
                (SeekBarPreference) prefSet.findPreference(PREF_QS_TRANSPARENT_SHADE);
        int qSShadeAlpha = Settings.System.getInt(resolver,
                Settings.System.QS_TRANSPARENT_SHADE, 255);
        mQSShadeAlpha.setValue(qSShadeAlpha / 1);
        mQSShadeAlpha.setOnPreferenceChangeListener(this);
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.OWLSNEST;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();
        if (preference == mQSShadeAlpha) {
                int alpha = (Integer) newValue;
                Settings.System.putInt(getActivity().getContentResolver(),
                        Settings.System.QS_TRANSPARENT_SHADE, alpha * 1);
                return true;
        }
         return false;
    }
}


