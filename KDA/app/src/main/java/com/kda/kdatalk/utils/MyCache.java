package com.kda.kdatalk.utils;

import android.content.SharedPreferences;

import com.kda.kdatalk.ui.base.Application;

public class MyCache {


    private static MyCache _Cache;


    private SharedPreferences.Editor _editor;
    private SharedPreferences _sharedPreferences;

    private MyCache() {
        this._sharedPreferences = Application.getInstance().getSharedPreferences("KDA_SHARED_PREF_CACHE", 0);
        this._editor = this._sharedPreferences.edit();
    }

    public static synchronized MyCache getInstance() {
        MyCache mISACache;
        synchronized (MyCache.class) {
            if (_Cache == null) {
                _Cache = new MyCache();
            }
            mISACache = _Cache;
        }
        return mISACache;
    }

    public boolean contains(String key) {
        return this._sharedPreferences.contains(key);
    }

    public String getString(String key) {
        return this._sharedPreferences.getString(key, "");
    }

    public int getInt(String key) {
        return this._sharedPreferences.getInt(key, -1);
    }

    public int getInt(String key, int defaultValue) {
        return this._sharedPreferences.getInt(key, defaultValue);
    }

    public long getLong(String key) {
        return this._sharedPreferences.getLong(key, -1);
    }

    public void clear(String key) {
        this._editor.remove(key);
        this._editor.commit();
    }

    public void clearAll() {
        this._editor.clear();
        this._editor.commit();
    }

    public String getString(String key, String defValue) {
        return this._sharedPreferences.getString(key, defValue);
    }

    public boolean getBoolean(String key) {
        return this._sharedPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this._sharedPreferences.getBoolean(key, defaultValue);
    }

    public void putString(String key, String value) {
        if (key != null) {
            if (value == null) {
                this._editor.remove(key);
            }
            this._editor.putString(key, value);
            this._editor.commit();
        }
    }

    public void putInt(String key, int value) {
        if (key != null) {
            this._editor.putInt(key, value);
            this._editor.commit();
        }
    }

    public void putLong(String key, long value) {
        if (key != null) {
            this._editor.putLong(key, value);
            this._editor.commit();
        }
    }

    public void putBoolean(String key, boolean value) {
        if (key != null) {
            this._editor.putBoolean(key, value);
            this._editor.commit();
        }
    }

}