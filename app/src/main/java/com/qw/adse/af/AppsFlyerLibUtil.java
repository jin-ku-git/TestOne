package com.qw.adse.af;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;
import com.qw.adse.web_view.AdvertisingActivity;


import java.util.HashMap;
import java.util.Map;

import me.goldze.mvvmhabit.utils.KLog;

public class AppsFlyerLibUtil {
    private static final String TAG = "AppsFlyerLibUtil";


    public static void init(Context context) {

        AppsFlyerLib.getInstance().start(context, "ga4wcjCT7zgRyoetxvPHze", new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {

                Log.e(TAG, "Launch sent successfully, got 200 response code from server");
                KLog.d("成功");
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Log.e(TAG, "Launch failed to be sent:\n" + "Error code: " + i + "\n" + "Error description: " + s);
                KLog.d("失败");
            }
        });
        AppsFlyerLib.getInstance().setDebugLog(true);
    }


    public static void event(Activity context, String name, String data) {
        Map<String, Object> eventValue = new HashMap<String, Object>();

        KLog.e("name="+name+"\ndata="+data);



        /***
         * 开启新窗口跳转
         */
        if ("openWindow".equals(name)) {
            Intent intent = new Intent(context, AdvertisingActivity.class);
            intent.putExtra("url", data);
            context.startActivityForResult(intent, 1);
        } else if ("firstrecharge".equals(name) || "recharge".equals(name)) {
            try {
                Map maps = (Map) JSON.parse(data);
                for (Object map : maps.entrySet()) {
                    String key = ((Map.Entry) map).getKey().toString();
                    if ("amount".equals(key)) {
                        eventValue.put(AFInAppEventParameterName.REVENUE, ((Map.Entry) map).getValue());
                    } else if ("currency".equals(key)) {
                        eventValue.put(AFInAppEventParameterName.CURRENCY, ((Map.Entry) map).getValue());
                    }
                }
            } catch (Exception e) {

            }
        } else if ("withdrawOrderSuccess".equals(name)) {
            // 提现成功
            try {
                Map maps = (Map) JSON.parse(data);
                for (Object map : maps.entrySet()) {
                    String key = ((Map.Entry) map).getKey().toString();
                    if ("amount".equals(key)) {
                        float revenue = 0;
                        String value = ((Map.Entry) map).getValue().toString();
                        if (!TextUtils.isEmpty(value)) {
                            revenue = Float.valueOf(value);
                            revenue = -revenue;
                        }
                        eventValue.put(AFInAppEventParameterName.REVENUE, revenue);

                    } else if ("currency".equals(key)) {
                        eventValue.put(AFInAppEventParameterName.CURRENCY, ((Map.Entry) map).getValue());
                    }
                }
            } catch (Exception e) {

            }
        } else {
            eventValue.put(name, data);
        }

//        if (Constant.LIST_AF.get(18).equals(name)) {
//            Intent intent = new Intent(context, AdvertisingActivity.class);
//            intent.putExtra("url", data);
//            context.startActivityForResult(intent, 1);
//        }else if (Constant.LIST_AF.size()!=0){
//            if (Constant.LIST_AF.get(6).equals(name) || Constant.LIST_AF.get(7).equals(name)) {
//                try {
//                    Map maps = (Map) JSON.parse(data);
//                    for (Object map : maps.entrySet()) {
//                        String key = ((Map.Entry) map).getKey().toString();
//                        if ("amount".equals(key)) {
//                            eventValue.put(AFInAppEventParameterName.REVENUE, ((Map.Entry) map).getValue());
//                        } else if ("currency".equals(key)) {
//                            eventValue.put(AFInAppEventParameterName.CURRENCY, ((Map.Entry) map).getValue());
//                        }
//                    }
//                } catch (Exception e) {
//
//                }
//            } else if (Constant.LIST_AF.get(9).equals(name)) {
//
//                try {
//                    Map maps = (Map) JSON.parse(data);
//                    for (Object map : maps.entrySet()) {
//                        String key = ((Map.Entry) map).getKey().toString();
//                        if ("amount".equals(key)) {
//                            float revenue = 0;
//                            String value = ((Map.Entry) map).getValue().toString();
//                            if (!TextUtils.isEmpty(value)) {
//                                revenue = Float.valueOf(value);
//                                revenue = -revenue;
//                            }
//                            eventValue.put(AFInAppEventParameterName.REVENUE, revenue);
//
//                        } else if ("currency".equals(key)) {
//                            eventValue.put(AFInAppEventParameterName.CURRENCY, ((Map.Entry) map).getValue());
//                        }
//                    }
//                } catch (Exception e) {
//
//                }
//            } else {
//                eventValue.put(name, data);
//            }
//        }

        AppsFlyerLib.getInstance().logEvent(context, name, eventValue);

    }
}
