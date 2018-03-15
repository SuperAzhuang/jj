package com.feihua.jjcb.phone.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.feihua.jjcb.phone.bean.Datadic;
import com.feihua.jjcb.phone.callback.BiaoCeTables;

import java.util.ArrayList;

public class BiaoKuangDatabase
{

    Context context;
    DatabaseHelper dbhelper;
    public SQLiteDatabase sqlitedatabase;

    public BiaoKuangDatabase(Context context)
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
    public int updatatable(ContentValues values, String volumeNo)
    {
        opendb(context);
        int update = sqlitedatabase.update(DatabaseHelper.BIAOK_TABLE_NAME, values, DatabaseHelper.WATER_BK + "=?", new String[]{volumeNo});
        closedb(context);
        return update;
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
    public int queryCount(String table_name)
    {
        opendb(context);
        Cursor cursor = sqlitedatabase.query(table_name, null, null, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public ArrayList<Datadic> DeptArrayAll()
    {
        ArrayList<Datadic> datas = new ArrayList<Datadic>();
        try
        {
            opendb(context);
            Cursor c = sqlitedatabase.query(DatabaseHelper.BIAOK_TABLE_NAME, null, null, null, null, null, DatabaseHelper.IS_CBWAY + " ASC");

            if (c != null)
            {
                boolean toFirst = c.moveToFirst();
                while (toFirst)
                {
                    String WATER_BK = c.getString(c.getColumnIndex(DatabaseHelper.WATER_BK));
                    String IS_CBWAY = c.getString(c.getColumnIndex(DatabaseHelper.IS_CBWAY));
                    datas.add(new Datadic(WATER_BK,IS_CBWAY));
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
        return datas;
    }

}