package vn.edu.poly.totalstreamer.myLog;

import android.util.Log;
import android.widget.Toast;

import vn.edu.poly.totalstreamer.network.MyApplication;

/**
 * Created by nix on 11/10/16.
 */

public class L {
    public static void t(String message) {
        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void m(String message) {
        Log.d("Tri", message);
    }
}
