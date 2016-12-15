package vn.edu.poly.totalstreamer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import vn.edu.poly.totalstreamer.R;

/**
 * Created by van on 15/12/2016.
 */

public class SplashScreen extends Activity {
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.mprogressbar);
        Thread thread = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i <= 10; i += 2) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.setProgress(i);
                }
                startActivity(new Intent(getApplication(), MainActivity.class));
                finish();
            }
        };
        thread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
