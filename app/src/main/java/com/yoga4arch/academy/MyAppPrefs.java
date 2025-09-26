package com.yoga4arch.academy;

import android.content.Context;
import android.content.SharedPreferences;

public class MyAppPrefs {
    private static final String PREFS = "Yoga4archPrefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_EXP = "jwt_exp";

    private SharedPreferences prefs;

    public MyAppPrefs(Context ctx) {
        prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void saveToken(String token, long expUnix) {
        prefs.edit().putString(KEY_TOKEN, token).putLong(KEY_EXP, expUnix).apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public long getExp() {
        return prefs.getLong(KEY_EXP, 0);
    }

    public boolean isTokenValid() {
        String t = getToken();
        if (t == null) return false;
        long now = System.currentTimeMillis() / 1000;
        long exp = getExp();
        return exp == 0 ? true : now < exp;
    }

    public void clear() {
        prefs.edit().remove(KEY_TOKEN).remove(KEY_EXP).apply();
    }
}
