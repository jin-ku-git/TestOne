package com.qw.adse.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.qw.adse.ui.choujiang.bena.DrawBean;

import java.util.ArrayList;
import java.util.List;


public class DrawDAO {
    public static final String TAG = "DrawDAO";


    private final String[] GOODS_COLUMNS = new String[]{"id", "name", "listName"};


    private Context context;
    private DBHelper goodsDBHelper;

    public DrawDAO(Context context) {
        this.context = context;
        goodsDBHelper = new DBHelper(context);
    }

    public boolean isDataExist() {
        int count = 0;

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = goodsDBHelper.getReadableDatabase();
            // select count(Id) from Goods
            cursor = db.query(goodsDBHelper.DRAW_TABLE_NAME, new String[]{"COUNT(id)"}, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    int key = 0;


    public synchronized void initTable(DrawBean goodsBeans) {
        if (null == goodsBeans ) {
            return;
        }

        SQLiteDatabase db = null;


        try {
            db = goodsDBHelper.getWritableDatabase();
            db.beginTransaction();




                ContentValues contentValues = new ContentValues();
                contentValues.put("name", goodsBeans.getName());
                contentValues.put("listName", goodsBeans.getListName());

                db.insert(goodsDBHelper.DRAW_TABLE_NAME, null, contentValues);


//            for (int i = 0; i < goodsBeans.size(); i++) {
//                key = i;
//                DrawBean goodsBean = goodsBeans.get(i);
//                if (null != goodsBean) {
//
//
//                    ContentValues contentValues = new ContentValues();
//
//
//                    contentValues.put("name", goodsBean.getName());
//                    contentValues.put("listName", goodsBean.getListName());
//
//                    db.insert(goodsDBHelper.DRAW_TABLE_NAME, null, contentValues);
//
//                }
//            }


            db.setTransactionSuccessful();
        } catch (Exception e) {

            Log.e(TAG, "---", e);
        } finally {


            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public List<DrawBean> getAllDate() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = goodsDBHelper.getReadableDatabase();
            // select * from Goods
            cursor = db.query(goodsDBHelper.DRAW_TABLE_NAME, GOODS_COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                List<DrawBean> goodList = new ArrayList<DrawBean>(cursor.getCount());
                while (cursor.moveToNext()) {
                    goodList.add(parseGoodS(cursor));
                }
                return goodList;
            }
        } catch (Exception e) {
            Log.e(TAG, "getAllData--exception", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }


    /**
     * 修改商品对应的
     *
     *
     * @return
     */
    public boolean update(String id, String name,String list) {

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {

            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.DRAW_TABLE_NAME, GOODS_COLUMNS, "id = ?", new String[]{id}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);

            if (null != cursor && cursor.getCount() > 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", name);
                contentValues.put("listName", list);


                db.update(goodsDBHelper.DRAW_TABLE_NAME, contentValues,
                        "id = ?",
                        new String[]{id});
//                db.delete(goodsDBHelper.DRAW_TABLE_NAME,"good_id = ?",new String[]{goodBean.getGood_id()});
            }


            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    /**
     * 删除对应id的数据
     *
     *
     * @return
     */

    public boolean delete(String Id) {

        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = goodsDBHelper.getReadableDatabase();
            cursor = db.query(goodsDBHelper.DRAW_TABLE_NAME, GOODS_COLUMNS, "id = ?", new String[]{Id}, null, null, null);
            Log.e(TAG, "cursor  " + cursor);
            if (null != cursor && cursor.getCount() > 0) {
                db.delete(goodsDBHelper.DRAW_TABLE_NAME, "id = ?", new String[]{Id});
                return true;
            } else {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }





    private DrawBean parseGoodS(Cursor cursor) {
        DrawBean DrawBean = new DrawBean();
        DrawBean.setId(cursor.getInt(cursor.getColumnIndex(GOODS_COLUMNS[0])));
        DrawBean.setName(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[1])));
        DrawBean.setListName(cursor.getString(cursor.getColumnIndex(GOODS_COLUMNS[2])));

        return DrawBean;
    }

    public void deleteAllData() {
        SQLiteDatabase db = null;

        db = goodsDBHelper.getReadableDatabase();
        try {
            db.execSQL("delete from " + goodsDBHelper.DRAW_TABLE_NAME);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

}
