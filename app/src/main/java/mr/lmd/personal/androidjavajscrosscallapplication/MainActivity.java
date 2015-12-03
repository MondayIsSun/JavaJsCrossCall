package mr.lmd.personal.androidjavajscrosscallapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WebView myWebView = (WebView) findViewById(R.id.myWebView);

        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        myWebView.addJavascriptInterface(new JsInteration(), "control");

        myWebView.setWebChromeClient(new WebChromeClient() {
        });

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                testMethod(myWebView);
            }
        });

        //装载html文件(包含其中的js脚本)
        myWebView.loadUrl("file:///android_asset/js_java_interaction.html");
    }

    /**
     * Java调用Javascript
     *
     * @param webView webView对象
     */
    private void testMethod(WebView webView) {
        String js;
//        js = "javascript:sayHello()";
//        js = "javascript:alertMessage(\"\"+\"content\"+\"\")";
//        js = "javascript:toastMessage(\"\" + \"content\"+\"\")";
        js = "javascript:sumToJava(1,2)";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(js, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.i(TAG, "onReceiveValue value=" + value);
                }
            });
        } else {
            webView.loadUrl(js);
        }
    }

    public class JsInteration {
        @JavascriptInterface
        public void toastMessage(String message) {//js调用java
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void onSumResult(int result) {
            Log.i(TAG, "onSumResult result=" + result);
        }
    }

}
