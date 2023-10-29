package com.qw.adse.web_view;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qw.adse.af.AppsFlyerLibUtil;
import com.qw.adse.app.Constant;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import me.goldze.mvvmhabit.utils.KLog;


public class WebViewActivity extends Activity {

    private static final String TAG = "WebViewActivity";
    private WebView webView;

    String loadUrl = "https://mm.hf-demo.com/#/";

    private ValueCallback<Uri> mUploadCallBack;
    private ValueCallback<Uri[]> mUploadCallBackAboveL;
    private final  int REQUEST_CODE_FILE_CHOOSER = 888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (TextUtils.isEmpty(loadUrl)) {
            finish();
        }
        Intent intent=getIntent();
//        loadUrl=intent.getStringExtra("body");

        System.out.println("loadUrl="+loadUrl);

        webView = new WebView(this);


        AppsFlyerLibUtil.init(this);

        webView.clearCache(true);
        webView.clearHistory();
        setSetting();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (TextUtils.equals(failingUrl, loadUrl)) {
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String WgPackage = "javascript:window.WgPackage = {name:'" + getPackageName() + "', version:'"
                        + getAppVersionName(WebViewActivity.this) + "'}";
                webView.evaluateJavascript(WgPackage, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                String WgPackage = "javascript:window.WgPackage = {name:'" + getPackageName() + "', version:'"
                        + getAppVersionName(WebViewActivity.this) + "'}";
                webView.evaluateJavascript(WgPackage, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
        });
        webView.addJavascriptInterface(new JsInterface(), "jsBridge");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){

                handler.proceed();

            }
        });


        webView.loadUrl(loadUrl);
        setContentView(webView);

        // 只需要将第一种方法的loadUrl()换成下面该方法即可
//        webView.evaluateJavascript("javascript:getAppsFlyerId()", new ValueCallback<String>() {
//            @Override
//            public void onReceiveValue(String value) {
//                //此处为 js 返回的结果
//                Log.d("js 返回的结果:","js getAppsFlyerId()返回的结果:"+value);
//            }
//        });


}


    public String getAppVersionName(Context context) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext().getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return appVersionName;
    }

    private void setSetting() {
        WebSettings setting = webView.getSettings();

        setting.setSupportMultipleWindows(true);
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        setting.setDomStorageEnabled(true);
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);
        setting.setAllowContentAccess(true);
        setting.setDatabaseEnabled(true);
        setting.setGeolocationEnabled(true);





        String appCachePath = getApplicationContext().getCacheDir()
                .getAbsolutePath() + "/webcache";


        setting.setDatabasePath(appCachePath);



        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        setting.setUserAgentString(setting.getUserAgentString().replaceAll("; wv", ""));


        int SDK_INT = Build.VERSION.SDK_INT;
        if (SDK_INT > 16) {
            setting.setMediaPlaybackRequiresUserGesture(false);
        }
        setting.setSupportZoom(false);

        try {
            Class<?> clazz = setting.getClass();
            Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
            if (method != null) {
                method.invoke(setting, true);
            }
        } catch (IllegalArgumentException | NoSuchMethodException | IllegalAccessException
                 | InvocationTargetException e) {
            e.printStackTrace();
        }
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent intent = new Intent();

                intent.setAction(Intent.ACTION_VIEW);

                Uri uri = Uri.parse(url);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                WebViewActivity.this.mUploadCallBack = uploadMsg;
                openFileChooseProcess();
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsgs) {
                WebViewActivity.this.mUploadCallBack = uploadMsgs;
                openFileChooseProcess();
            }

            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                WebViewActivity.this.mUploadCallBack = uploadMsg;
                openFileChooseProcess();
            }

            // For Android  >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                WebViewActivity.this.mUploadCallBackAboveL = filePathCallback;
                openFileChooseProcess();
                return true;
            }
        });
    }

    private void openFileChooseProcess() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Select Picture"), REQUEST_CODE_FILE_CHOOSER);
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
    public class JsInterface {

        @JavascriptInterface
        public void postMessage(String name, String data) {
//            Log.e(TAG, "name = " + name + "    data = " + data);
            KLog.d("1name = " + name + "    data = " + data);
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(data)) {
                return;
            }
//            AppsFlyerLibUtil.event(WebViewActivity.this, name, data);
        }

        @JavascriptInterface
        public String getAppsFlyerId() {
            KLog.d("getAppsFlyerId="+ Constant._AppsFlyerUSERID);
            return Constant._AppsFlyerUSERID;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "---------requestCode = "+requestCode+ "      resultCode = "+resultCode);
        if (requestCode == this.REQUEST_CODE_FILE_CHOOSER) {
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (result != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (mUploadCallBackAboveL != null) {
                        mUploadCallBackAboveL.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                        mUploadCallBackAboveL = null;
                        return;
                    }
                } else if (mUploadCallBack != null) {
                    mUploadCallBack.onReceiveValue(result);
                    mUploadCallBack = null;
                    return;
                }
            }
            clearUploadMessage();
            return;
        }else if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if (webView == null) {
                    return;
                }


                webView.evaluateJavascript("javascript:window.closeGame()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
        }
    }
    private void clearUploadMessage() {
        if (mUploadCallBackAboveL != null) {
            mUploadCallBackAboveL.onReceiveValue(null);
            mUploadCallBackAboveL = null;
        }
        if (mUploadCallBack != null) {
            mUploadCallBack.onReceiveValue(null);
            mUploadCallBack = null;
        }
    }
}
