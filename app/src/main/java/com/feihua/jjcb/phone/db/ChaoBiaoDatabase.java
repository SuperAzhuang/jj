package com.feihua.jjcb.phone.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.feihua.jjcb.phone.callback.BiaoCeTables;
import com.feihua.jjcb.phone.callback.ChaoBiaoTables;
import com.feihua.jjcb.phone.constants.Constants;
import com.feihua.jjcb.phone.utils.L;
import com.feihua.jjcb.phone.utils.SharedPreUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChaoBiaoDatabase
{

    Context context;
    DatabaseHelper dbhelper;
    public SQLiteDatabase sqlitedatabase;

    public ChaoBiaoDatabase(Context context)
    {
        super();
        this.context = context;
    }

    // 打开数据库连接
    public void opendb(Context context)
    {
        dbhelper = new DatabaseHelper(context);
        sqlitedatabase = dbhelper.getWritableDatabase();
    }

    // 关闭数据库连接
    public void closedb(Context context)
    {
        if (sqlitedatabase.isOpen())
        {
            sqlitedatabase.close();
        }
    }

    // 插入表数据
    public void insert(String table_name, ContentValues values)
    {
        opendb(context);
        sqlitedatabase.insert(table_name, null, values);
        closedb(context);
    }

    // 更新数据
    public int updatatable(ContentValues values, String userbKh)
    {
        opendb(context);
        return sqlitedatabase.update(DatabaseHelper.CHAOBIAO_TABLE_NAME, values, DatabaseHelper.CHAOBIAO_USERB_KH + "=?", new String[]{userbKh});
    }

    // 删除表数据
    public void delete(String table_name, String whereClause, String[] whereArgs)
    {
        opendb(context);
        try
        {
            for (int i = 0; i < whereArgs.length; i++)
            {
                sqlitedatabase.delete(table_name, whereClause, new String[]{whereArgs[i]});
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closedb(context);
        }
    }

    // 删除表
    public void delete(String table_name)
    {
        opendb(context);
        try
        {

            sqlitedatabase.delete(table_name, null, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closedb(context);
        }
    }

    // 查询数据表有多少条数据
    public int queryCount()
    {
        return queryCount(null, null);
    }

    // 查询指定条件是,数据表有多少条数据
    public int queryCount(String selection, String[] selectionArgs)
    {
        opendb(context);
        int count = 0;
        try
        {
            Cursor cursor = sqlitedatabase.query(DatabaseHelper.CHAOBIAO_TABLE_NAME, null, selection, selectionArgs, null, null, null);
            count = cursor.getCount();
            cursor.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closedb(context);
        }
        return count;
    }

    public ArrayList<String> queryUserKh(String VOLUME_NO)
    {
        ArrayList<String> khDatas = new ArrayList<>();
        String selection = DatabaseHelper.CHAOBIAO_VOLUME_NO + "=?";
        String[] selectionArgs = new String[]{VOLUME_NO};
        try
        {
            opendb(context);
            Cursor c = sqlitedatabase.query(DatabaseHelper.CHAOBIAO_TABLE_NAME, new String[]{DatabaseHelper.CHAOBIAO_USERB_KH}, selection, selectionArgs, null, null, null);
            boolean toFirst = c.moveToFirst();
            while (toFirst)
            {
                String USERB_KH = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_KH));
                khDatas.add(USERB_KH.trim());
                toFirst = c.moveToNext();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closedb(context);
        }
        return khDatas;
    }

    public ArrayList<ChaoBiaoTables> DeptArrayHH(String volumeNo, String USERB_KH)
    {
        return DeptArray(DatabaseHelper.CHAOBIAO_VOLUME_NO + "=? and " + DatabaseHelper.CHAOBIAO_USERB_KH + "=?", new String[]{volumeNo, USERB_KH});
    }

    public ArrayList<ChaoBiaoTables> DeptArrayBK(String volumeNo, String WATER_BK)
    {
        return DeptArray(DatabaseHelper.CHAOBIAO_VOLUME_NO + "=? and " + DatabaseHelper.CHAOBIAO_WATER_BK + "=?", new String[]{volumeNo, WATER_BK});
    }

    public ArrayList<ChaoBiaoTables> DeptArrayCB(String volumeNo, String IS_SAVE)
    {
        return DeptArray(DatabaseHelper.CHAOBIAO_VOLUME_NO + "=? and " + DatabaseHelper.CHAOBIAO_IS_SAVE + "=?", new String[]{volumeNo, IS_SAVE});
    }

    public ArrayList<ChaoBiaoTables> DeptArray(String volumeNo)
    {
        return DeptArray(DatabaseHelper.CHAOBIAO_VOLUME_NO + "=?", new String[]{volumeNo});
    }

    public ArrayList<ChaoBiaoTables> DeptArray(String selection, String[] selectionArgs)
    {
        ArrayList<ChaoBiaoTables> datas = new ArrayList<ChaoBiaoTables>();
        try
        {
            opendb(context);
            //TODO
            Cursor c = sqlitedatabase.query(DatabaseHelper.CHAOBIAO_TABLE_NAME, null, selection, selectionArgs, null, null, DatabaseHelper.CHAOBIAO_USERB_JBH + " ASC");

            if (c != null)
            {
                boolean toFirst = c.moveToFirst();
                while (toFirst)
                {
                    ChaoBiaoTables tables = new ChaoBiaoTables();
                    tables.VOLUME_NO = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_VOLUME_NO));//
                    tables.USERB_KH = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_KH));//
                    tables.USERB_HM = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_HM));//
                    tables.USERB_ADDR = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_ADDR));//
                    tables.USERB_DH = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_DH));//
                    tables.USERB_MT = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_MT));//
                    tables.USERB_SQDS = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_SQDS));//
                    tables.USERB_JBH = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_JBH));//
                    tables.WATERM_CHECK_CODE = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERM_CHECK_CODE));//
                    tables.WATERM_YIELD = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERM_YIELD));//
                    tables.IS_UPDATA = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_IS_UPDATA));//
                    tables.IS_SAVE = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_IS_SAVE));//
                    tables.WATERU_YEAR = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERU_YEAR));//
                    tables.WATERU_MONTH = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERU_MONTH));//
                    datas.add(tables);
                    toFirst = c.moveToNext();
                }
                c.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closedb(context);
        }
        L.w("BiaoKuangDatabase", "datas.size() = " + datas.size());
        return datas;
    }

    public ChaoBiaoTables DeptArrayOne(String volumeNo, String USERB_KH)
    {
        ChaoBiaoTables tables = new ChaoBiaoTables();
        try
        {
            opendb(context);
            L.w("DeptArray", "USERB_KH" + USERB_KH);
            Cursor c = sqlitedatabase.query(DatabaseHelper.CHAOBIAO_TABLE_NAME, null, DatabaseHelper.CHAOBIAO_USERB_KH + "=?", new String[]{USERB_KH}, null, null, null);
            L.w("DeptArray", "count" + c.getCount());
            if (c != null)
            {
                boolean toFirst = c.moveToFirst();
                if (toFirst)
                {
                    tables.VOLUME_NO = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_VOLUME_NO));
                    tables.USERB_KH = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_KH));//
                    tables.USERB_HM = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_HM));//
                    tables.USERB_ADDR = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_ADDR));//
                    tables.USERB_DH = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_DH));//
                    tables.USERB_MT = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_MT));//
                    tables.METERC_CALIBRE = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_METERC_CALIBRE));
                    tables.WATERM_TYPE = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERM_TYPE));
                    tables.METERC_BASIC = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_METERC_BASIC));
                    tables.USERB_YSXZ = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_YSXZ));
                    tables.USERB_CUP = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_CUP));
                    tables.WATERM_AZRQ = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERM_AZRQ));
                    tables.USERB_LHRQ = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_LHRQ));
                    tables.WATERM_NX = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERM_NX));
                    tables.WATERM_NO = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERM_NO));
                    tables.USERB_SQDS = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_SQDS));//
                    tables.USERB_JBH = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_JBH));//
                    tables.GPS = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERM_LOALTION));
                    tables.WATERM_CHECK_CODE = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERM_CHECK_CODE));//
                    tables.WATERM_YIELD = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERM_YIELD));//
                    tables.IS_UPDATA = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_IS_UPDATA));//
                    tables.IS_SAVE = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_IS_SAVE));
                    tables.WATERM_REPDATE = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERM_REPDATE));
                    tables.USERB_YHLX = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_YHLX));
                    tables.WATERT_NAME = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERT_NAME));
                    tables.WATERU_YEAR = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERU_YEAR));
                    tables.WATERU_MONTH = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERU_MONTH));
                    tables.TIME = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_TIME));
                    tables.WATER_BK = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATER_BK));
                    tables.IS_CBWAY = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_IS_CBWAY));
                    tables.AREA_NO = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_AREA_NO));
                    tables.Q3Q = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_Q3Q));
                    tables.METERH_WATERQAN = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_METERH_WATERQAN));
                    tables.BASICTON_TAG = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_BASICTON_TAG));
                    tables.BASICTON_QAN = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_BASICTON_QAN));
                    tables.FIRST_TAG = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_FIRST_TAG));
                    tables.USERB_SFFS = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_SFFS));
                    tables.USERB_TOTAL = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_TOTAL));
                    tables.ISCHARGING = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_ISCHARGING));
                    tables.FREEPULL_TAG = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_FREEPULL_TAG));
                    tables.LADDER_TAG = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_LADDER_TAG));
                    tables.SEASON_TAG = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_SEASON_TAG));
                    tables.WZ_TAG = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WZ_TAG));
                    tables.USERB_SYSL = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_SYSL));
                    tables.QFJE = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_QFJE));
                    L.w("DeptArray", "QFJE = " + c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_QFJE)));
                    //                    datas.add(tables);
                    //                    toFirst = c.moveToNext();
                }
                c.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closedb(context);
        }
        L.w("BiaoKuangDatabase", "tables = ");
        return tables;
    }

    public JSONArray DeptArraySaveAll(String volumeNo, String userId)
    {
        return DeptArraySaveAll(volumeNo, userId, 0);
    }

    public JSONArray DeptArraySaveAll(String volumeNo, String userId, int pageNum)
    {
        JSONArray jsonArray = new JSONArray();
        try
        {
            opendb(context);
            String selection = DatabaseHelper.BIAOCE_VOLUME_NO + "=? and " + DatabaseHelper.CHAOBIAO_IS_SAVE + "=? and " + DatabaseHelper.CHAOBIAO_IS_UPDATA + "=?";
            String[] selectionArge = {volumeNo, "1", "0"};
            String orderBy = null;
            String limit = null;
            if (pageNum != 0)
            {
                orderBy = DatabaseHelper.CHAOBIAO_USERB_JBH + " asc";
                limit = String.valueOf(pageNum);

            }
            Cursor c = sqlitedatabase.query(DatabaseHelper.CHAOBIAO_TABLE_NAME, null, selection, selectionArge, null, null, orderBy, limit);
            if (c != null)
            {
                boolean toFirst = c.moveToFirst();
                while (toFirst)
                {
                    JSONObject item = new JSONObject();
                    item.put("RECORDER", userId);
                    item.put("VOLUME_NO", volumeNo);
                    item.put("USERB_KH", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_KH)));
                    item.put("USERB_SQDS", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_SQDS)));
                    item.put("USERB_BQDS", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERM_CHECK_CODE)));
                    item.put("WATERU_QAN", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERM_YIELD)));
                    String q3q = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_Q3Q));
                    String yhlx = c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_YHLX));
                    if (TextUtils.isEmpty(q3q))
                    {
                        q3q = "";
                    }
                    if (TextUtils.isEmpty(yhlx))
                    {
                        yhlx = "";
                    }
                    item.put("THREEMON_AVG", q3q);
                    item.put("GPS", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERM_LOALTION)));
                    item.put("USERB_HM", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_HM)));
                    item.put("USERB_YHLX", yhlx);
                    item.put("WATERU_YEAR", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERU_YEAR)));
                    item.put("WATERU_MONTH", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATERU_MONTH)));
                    item.put("WATER_BK", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WATER_BK)));
                    item.put("AREA_NO", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_AREA_NO)));
                    item.put("IS_CBWAY", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_IS_CBWAY)));
                    item.put("FIRST_TAG", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_FIRST_TAG)));
                    item.put("RECORD_DATE", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_TIME)));
                    item.put("USERB_SFFS", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_SFFS)));
                    item.put("METERH_WATERQAN", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_METERH_WATERQAN)));
                    item.put("USERB_TOTAL", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_TOTAL)));
                    item.put("ISCHARGING", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_ISCHARGING)));
                    item.put("USERB_CUP", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_CUP)));
                    item.put("FREEPULL_TAG", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_FREEPULL_TAG)));
                    item.put("BASICTON_TAG", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_BASICTON_TAG)));
                    item.put("BASICTON_QAN", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_BASICTON_QAN)));
                    item.put("LADDER_TAG", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_LADDER_TAG)));
                    item.put("SEASON_TAG", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_SEASON_TAG)));
                    item.put("WZ_TAG", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_WZ_TAG)));
                    item.put("USERB_SYSL", c.getString(c.getColumnIndex(DatabaseHelper.CHAOBIAO_USERB_SYSL)));
                    jsonArray.put(item);
                    toFirst = c.moveToNext();
                }
                c.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closedb(context);
        }
        return jsonArray;
    }

}