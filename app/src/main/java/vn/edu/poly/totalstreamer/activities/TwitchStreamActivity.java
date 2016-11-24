package vn.edu.poly.totalstreamer.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import vn.edu.poly.totalstreamer.R;

public class TwitchStreamActivity extends AppCompatActivity {

    WebView webViewPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitch_stream);
        this.getWindow().getDecorView().setSystemUiVisibility(getSystemUiFlags());
        UiChangeListener();

        webViewPlayer = (WebView) findViewById(R.id.webViewPlayer);

        String channelName = getIntent().getExtras().getString("channelName");

        WebSettings webViewPlayerSettings = webViewPlayer.getSettings();
        webViewPlayerSettings.setJavaScriptEnabled(true);
        webViewPlayerSettings.setUseWideViewPort(true);
        webViewPlayerSettings.setLoadWithOverviewMode(true);
        webViewPlayer.loadUrl("https://player.twitch.tv/?volume=1&channel=" + channelName + "&quality=chunked&autoplay=true");
        webViewPlayer.setWebChromeClient(new WebChromeClient());
    }

    @Override
    protected void onPause() {
        super.onPause();
        webViewPlayer.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webViewPlayer.onResume();
    }

    public void UiChangeListener() {
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
            }
        });
    }

    private int getSystemUiFlags() {
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }
}
