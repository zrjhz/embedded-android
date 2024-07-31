package im.drh.utils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@SuppressLint({"NewApi"})
public class PreferencesUtil {
    private static SharedPreferences mPrefs;
    private static final String LENGTH_SUFFIX = "#LENGTH";
    private static final String LEFT_MOUNT = "[";
    private static final String RIGHT_MOUNT = "]";

    public PreferencesUtil() {
    }

    public static void initPrefs(Context context) {
        if (mPrefs == null) {
            String key = context.getPackageName();
            if (key == null) {
                throw new NullPointerException("Prefs key may not be null");
            }

            mPrefs = context.getSharedPreferences(key, 4);
        }

    }

    /** @deprecated */
    @Deprecated
    public static void reInit(Context context) {
        if (context != null) {
            String key = context.getPackageName();
            if (key == null) {
                throw new NullPointerException("Prefs key may not be null");
            }

            mPrefs = context.getSharedPreferences(key, 4);
        }

    }

    public static SharedPreferences getPreferences() {
        if (mPrefs != null) {
            return mPrefs;
        } else {
            throw new RuntimeException("please call iniPrefs(context) in the Application class onCreate.");
        }
    }

    public static Map<String, ?> getAll() {
        return getPreferences().getAll();
    }

    public static int getInt(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public static float getFloat(String key, float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    public static String getString(String key, String defValue) {
        return getPreferences().getString(key, defValue);
    }

    @TargetApi(11)
    public static Set<String> getStringSet(String key, Set<String> defValue) {
        SharedPreferences prefs = getPreferences();
        if (VERSION.SDK_INT >= 11) {
            return prefs.getStringSet(key, defValue);
        } else if (!prefs.contains(key + "#LENGTH")) {
            return defValue;
        } else {
            HashSet<String> set = new HashSet();
            int stringSetLength = prefs.getInt(key + "#LENGTH", -1);
            if (stringSetLength >= 0) {
                for(int i = 0; i < stringSetLength; ++i) {
                    prefs.getString(key + "[" + i + "]", (String)null);
                }
            }

            return set;
        }
    }

    public static void putLong(String key, long value) {
        Editor editor = getPreferences().edit();
        editor.putLong(key, value);
        if (VERSION.SDK_INT < 9) {
            editor.commit();
        } else {
            editor.apply();
        }

    }

    public static void putInt(String key, int value) {
        Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        if (VERSION.SDK_INT < 9) {
            editor.commit();
        } else {
            editor.apply();
        }

    }

    public static void putFloat(String key, float value) {
        Editor editor = getPreferences().edit();
        editor.putFloat(key, value);
        if (VERSION.SDK_INT < 9) {
            editor.commit();
        } else {
            editor.apply();
        }

    }

    public static void putBoolean(String key, boolean value) {
        Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        if (VERSION.SDK_INT < 9) {
            editor.commit();
        } else {
            editor.apply();
        }

    }

    public static void putString(String key, String value) {
        Editor editor = getPreferences().edit();
        editor.putString(key, value);
        if (VERSION.SDK_INT < 9) {
            editor.commit();
        } else {
            editor.apply();
        }

    }

    @TargetApi(11)
    public static void putStringSet(String key, Set<String> value) {
        Editor editor = getPreferences().edit();
        if (VERSION.SDK_INT >= 11) {
            editor.putStringSet(key, value);
        } else {
            int stringSetLength = 0;
            if (mPrefs.contains(key + "#LENGTH")) {
                stringSetLength = mPrefs.getInt(key + "#LENGTH", -1);
            }

            editor.putInt(key + "#LENGTH", value.size());
            int i = 0;

            for(Iterator var5 = value.iterator(); var5.hasNext(); ++i) {
                String aValue = (String)var5.next();
                editor.putString(key + "[" + i + "]", aValue);
            }

            while(i < stringSetLength) {
                editor.remove(key + "[" + i + "]");
                ++i;
            }
        }

        if (VERSION.SDK_INT < 9) {
            editor.commit();
        } else {
            editor.apply();
        }

    }

    public static void remove(String key) {
        SharedPreferences prefs = getPreferences();
        Editor editor = prefs.edit();
        if (prefs.contains(key + "#LENGTH")) {
            int stringSetLength = prefs.getInt(key + "#LENGTH", -1);
            if (stringSetLength >= 0) {
                editor.remove(key + "#LENGTH");

                for(int i = 0; i < stringSetLength; ++i) {
                    editor.remove(key + "[" + i + "]");
                }
            }
        }

        editor.remove(key);
        if (VERSION.SDK_INT < 9) {
            editor.commit();
        } else {
            editor.apply();
        }

    }

    public static boolean contains(String key) {
        return getPreferences().contains(key);
    }
}
