package com.qw.adse.utils;

import me.goldze.mvvmhabit.utils.SPUtils;

public class LocalData {


    /**
     * 保存首页数据
     * @param MainName
     */
    public static void saveMainName(String MainName) {
        SPUtils.getInstance().put("MainName", MainName);
    }

    /**
     * 获取首页数据
     * @return
     */
    public static String getMainName() {
        return SPUtils.getInstance().getString("MainName");
    }

    /**
     * 保存首页主题
     * @param Theme
     */
    public static void saveTheme(String Theme) {
        SPUtils.getInstance().put("Theme", Theme);
    }


    /**
     * 获取首页主题
     * @return
     */
    public static String getTheme() {
        return SPUtils.getInstance().getString("Theme");
    }

}
