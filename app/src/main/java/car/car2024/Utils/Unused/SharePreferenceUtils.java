package car.car2024.Utils.Unused;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharePreferenceUtils 工具类
 *  应该是没用到
 * @author hdy
 *
 */
@SuppressLint("NewApi")
public class SharePreferenceUtils {
    private static Context context;
    private static SharedPreferences preferences;
    private static Editor edit;

    public static void setContext(Context context) {
        SharePreferenceUtils.context = context;
        if (context != null) {
            preferences = context.getSharedPreferences("data",
                    Context.MODE_PRIVATE);
            edit = preferences.edit();
        }
    }

    public static void putString(String name, String input) {
        edit.putString(name, input);
        edit.commit();
    }

    public static String getString(String name) {
        String string = preferences.getString(name, "");
        return string;
    }

    public static void putInt(String name, int i) {
        edit.putInt(name, i);
        edit.commit();
    }

    public static int getInt(String name) {
        int i = preferences.getInt(name, 0);
        return i;
    }

    public static void putBoolean(String name, boolean input) {
        edit.putBoolean(name, input);
        edit.commit();
    }

    public static boolean getBoolean(String name) {
        boolean b = preferences.getBoolean(name, false);
        return b;
    }

    /**
     *
     * 清空所有的数据
     */
    public static void clear() {
        edit.clear().apply();
    }
}
