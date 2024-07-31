package im.drh.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void showToast(Context context, String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int num) {
        Toast.makeText(context, Integer.toString(num), Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(Context context, String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
    }

    static Toast toast = null;

    public static void show(final Context context, final String text) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (toast != null) {
                        toast.setText(text);
                    } else {
                        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    }
                    toast.show();
                }
            });
        }
    }

}
