package com.hubspot.android.utils;

import android.content.SharedPreferences;

public class Settings {
    /** The preferences object */
    public static final String PREFS_NAME = "hubspot";

    /** Settings keys */
    public static final String API_KEY_PREF_NAME = "api_key";
    public static final String NOTIFY_ENABLED_PREF_NAME = "notify_enabled_key";
    public static final String LAST_UPDATE_PREF_NAME = "last_update_key";
    public static final String UPDATE_RATE_PREF_NAME = "update_rate_key";

    private static final String HAS_FULL_HISTORY_PREF_NAME = "has_full_history";

    public SharedPreferences preferences;

    public Settings(final SharedPreferences prefs) {
        this.preferences = prefs;
    }

    public String getApiKey() {
        synchronized (preferences) {
            return preferences.getString(API_KEY_PREF_NAME, Utils.EMPTY_STRING);
        }
    }

    public void setApiKey(final String apiKey) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(API_KEY_PREF_NAME, apiKey);
        editor.commit();
    }
    
    public boolean getNofifyEnabled() {
        return preferences.getBoolean(NOTIFY_ENABLED_PREF_NAME, false);
    }

    public void setNofifyEnabled(final boolean notifyEnabled) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(NOTIFY_ENABLED_PREF_NAME, notifyEnabled);
        editor.commit();
    }

    public long getLastUpdate() {
        return preferences.getLong(LAST_UPDATE_PREF_NAME, 0L);
    }

    public void setLastUpdate(long time) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(LAST_UPDATE_PREF_NAME, time);
        editor.commit();
    }

    /** In milliseconds */
    public int getUpdateRate() {
        String value = preferences.getString(UPDATE_RATE_PREF_NAME, null);
        if (value == null) {
            // Default to 30 minutes
            return 1800000;
        }

        return Integer.parseInt(value);
    }

    /** In milliseconds */
    public void setUpdateRate(int time) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(UPDATE_RATE_PREF_NAME, Integer.toString(time));
        editor.commit();
    }
    
    private Boolean hasFullHistory = null;
    
    public void setHasFullHistory(boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(HAS_FULL_HISTORY_PREF_NAME, value);
        editor.commit();
        this.hasFullHistory = value;
    }

    public boolean isHasFullHistory() {
        if (hasFullHistory == null) {
            hasFullHistory = preferences.getBoolean(HAS_FULL_HISTORY_PREF_NAME, false);
        }
        
        return hasFullHistory; 
    }
}
