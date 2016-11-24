package vn.edu.poly.totalstreamer.extras;

import android.content.res.Resources;
import android.util.TypedValue;

import vn.edu.poly.totalstreamer.network.MyApplication;

/**
 * Created by nix on 11/22/16.
 */

public class CustomMethods {
    public static int dpToPx(int dp) {
        Resources r = MyApplication.getAppContext().getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
