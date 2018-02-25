package work.yusukesugawara.examinewebviewinterface;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import work.yusukesugawara.examinewebviewinterface.misc.Lagdoll;
import work.yusukesugawara.examinewebviewinterface.misc.Str;

public class ActivityMain extends AppCompatActivity {
    private static final String TAG = "ActivityMain";

    @BindView(R.id.webView)
    WebView webView;

    private final Observable<Long> periodicSubject = PublishSubject.interval(1, TimeUnit.SECONDS);

    @Nullable
    private Disposable disposablePeriodicStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Lagdoll.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        configureWebView();

        webView.loadUrl("file:///android_asset/ExamineWebViewInterface-web/index.html");
    }

    @Override
    protected void onDestroy() {
        Lagdoll.d(TAG, "onDestroy");

        if (disposablePeriodicStream != null) {
            disposablePeriodicStream.dispose();
            disposablePeriodicStream = null;
        }

        super.onDestroy();
    }

    public static final String CUSTOM_URL_SCHEME = "work.yusukesugawara.callback://";
    public static final String SEPARATOR_EVENT_NAME_AND_PARAMETERS = "\\/\\?";

    @SuppressLint("SetJavaScriptEnabled")
    private void configureWebView() {
        Lagdoll.d(TAG, "configureWebView");
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

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Lagdoll.d(TAG, "onProgressChanged: progress=%d", newProgress);
            }
        });

        /*
        We should NOT use `WebView.addJavascriptInterface`
        because it can bring a indeterminate security risk.

        ```
        webView.addJavascriptInterface(new JavascriptInterfacePublisher(), "Android")
        ```

        Instead we use custom URL scheme pattern with
        `WebViewClient.shouldOverrideUrlLoading` event hooking.
        */
        webView.setWebViewClient(new WebViewClient() {
            private static final String TAG = "WebViewClient";

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Lagdoll.d(TAG, "shouldOverrideUrlLoading: url=%s", url);
                if (url.startsWith(CUSTOM_URL_SCHEME)) {
                    handleCallbackFromJS(url);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Lagdoll.d(TAG, "onPageFinished: url=%s", url);
            }
        });
    }

    private void handleCallbackFromJS(@NonNull String urlString) {
        String[] customRequest = urlString
                .replaceAll(CUSTOM_URL_SCHEME, "")
                .split(SEPARATOR_EVENT_NAME_AND_PARAMETERS);

        int i=0;
        for (String member : customRequest) {
            Lagdoll.w(TAG, "customRequest.member[%d]=%s", i, member);
            i+=1;
        }

        String eventName = customRequest[0];
        String paramsJson = customRequest[1];

        Lagdoll.d(TAG, "handleCallbackFromJS: [%s] %s", eventName, paramsJson);

        switch (eventName) {
            case "ready": {
                disposablePeriodicStream = periodicSubject
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                callJSFunction();
                            }
                        });
                break;
            }
        }
    }

    private void callJSFunction() {
        long timeMillis = System.currentTimeMillis();

        Lagdoll.d(TAG, "callJSFunction: timeMillis=%s", timeMillis);

        webView.evaluateJavascript(Str.format("hello(%d)", timeMillis), new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Lagdoll.d(TAG, "callJSFunction.onReceiveValue: value=%s", value);
            }
        });
    }
}
