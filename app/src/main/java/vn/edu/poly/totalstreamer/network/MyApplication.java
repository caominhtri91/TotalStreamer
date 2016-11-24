package vn.edu.poly.totalstreamer.network;

import android.app.Application;
import android.content.Context;

/**
 * Created by nix on 11/10/16.
 */

public class MyApplication extends Application {
    private static MyApplication sInstance;

    public static MyApplication getsInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
