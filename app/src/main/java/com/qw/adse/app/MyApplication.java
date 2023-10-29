package com.qw.adse.app;


import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import me.goldze.mvvmhabit.utils.KLog;

public class MyApplication extends Application {


    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();



        context = this;
        KLog.init(true);



    }

    public static <T> ArrayList<T> getObjectList(String jsonString, Class<T> cls) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
            for (JsonElement jsonElement : arry) {
                list.add(gson.fromJson(jsonElement, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }



    public static Context getContext() {
        return context;
    }
}
