package work.yusukesugawara.examinewebviewinterface;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import work.yusukesugawara.examinewebviewinterface.misc.LogDog;
import work.yusukesugawara.examinewebviewinterface.misc.Str;

public class ActivityMain extends AppCompatActivity {
    private static final String TAG = "ActivityMain";

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        configureWebView();

        webView.loadUrl("file:///android_asset/examineWebViewInterface/index.html");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configureWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        webView.setInitialScale(1);

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.clearCache(true);

        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemCallJsFunc:
                long timeMillis = System.currentTimeMillis();

                LogDog.d(TAG, "menuItemCallJsFunc: timeMillis=%s", timeMillis);

                webView.evaluateJavascript(Str.format("hello(%d)", timeMillis), new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        LogDog.d(TAG, "onReceiveValue: value=%s", value);
                    }
                });

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
