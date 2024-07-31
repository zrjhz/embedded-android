package car.car2024.Utils.Camera;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//@SuppressWarnings("all")
public class CameraCommandUtils {

    public CameraCommandUtils() {
    }

    public Bitmap httpForImage(String IP) {
        String httpUrl1 = "http://" + IP + "/snapshot.cgi?loginuse=admin&loginpas=888888&res=0";
        Bitmap bitmap = null;
        URL imageUrl = null;

        try {
            imageUrl = new URL(httpUrl1);
        } catch (MalformedURLException var8) {
            var8.printStackTrace();
        }

        if (imageUrl != null) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) imageUrl.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStream in = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
                httpURLConnection.disconnect();
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }

        return bitmap;
    }

    public void postHttp(String IP, int command, int step) {
        String httpUrl2 = "http://" + IP + "/decoder_control.cgi?loginuse=admin&loginpas=888888&" + "command=" + command + "&onestep=" + step;
        Log.e("TAG", "cameraMiscControlPostHttp: " + httpUrl2);
        new Thread(() -> {
            URL getUrl = null;
            try {
                getUrl = new URL(httpUrl2);
            } catch (MalformedURLException var9) {
                var9.printStackTrace();
            }
            try {
                assert getUrl != null;
                HttpURLConnection urlConnection = (HttpURLConnection) getUrl.openConnection();
                urlConnection.connect();
                urlConnection.getInputStream();
                urlConnection.disconnect();
            } catch (IOException var8) {
                var8.printStackTrace();
            }
        }).start();
    }


    public void sendHttp(String IP, int param, int value) {
        String httpUrl2 = "http://" + IP + "/camera_control.cgi?loginuse=admin&loginpas=888888&" + "param=" + param + "&value=" + value;
        URL getUrl = null;

        try {
            getUrl = new URL(httpUrl2);
        } catch (MalformedURLException var9) {
            var9.printStackTrace();
        }

        try {
            assert getUrl != null;
            HttpURLConnection urlConnection = (HttpURLConnection) getUrl.openConnection();
            urlConnection.connect();
            InputStream in = urlConnection.getInputStream();
            urlConnection.disconnect();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

    }

}
