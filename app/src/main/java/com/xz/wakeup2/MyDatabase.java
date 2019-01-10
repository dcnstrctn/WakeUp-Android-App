package com.xz.wakeup2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyDatabase {

    public class MyDatabaseHelper extends SQLiteOpenHelper {

        public static final String CREATE_DEADLINE ="create table Deadline(id integer primary key autoincrement, year integer, month integer, day integer, hour integer, minute integer, thing text)";
        public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DEADLINE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists Deadline");
            onCreate(db);
        }
    }
    //构造函数，创建数据库
    public MyDatabase(Context context){
        createDatabase(context);
    }

    private MyDatabaseHelper dbHelper;
    public void createDatabase(Context context)
    {
        dbHelper = new MyDatabaseHelper(context, "Deadline.db", null, 10);
    }
    public void  upgradeDatabase(Context context){
        dbHelper = new MyDatabaseHelper(context, "Deadline.db", null, 11);
    }
    public void addData(int year, int month, int day, int hour, int minute, String thing) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("year", year);
        values.put("month", month);
        values.put("day", day);
        values.put("hour", hour);
        values.put("minute", minute);
        values.put("thing", thing);
        db.insert("Deadline", null, values);
        values.clear();

        Cursor cursor = db.query("Deadline", null, null, null, null, null, null);

        cursor.moveToLast();
        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();

        while(cursor.moveToPrevious()) {
            int id1 = cursor.getInt(cursor.getColumnIndex("id"));
            int year1 = cursor.getInt(cursor.getColumnIndex("year"));
            int month1 = cursor.getInt(cursor.getColumnIndex("month"));
            int day1 = cursor.getInt(cursor.getColumnIndex("day"));
            int hour1 = cursor.getInt(cursor.getColumnIndex("hour"));
            int minute1 = cursor.getInt(cursor.getColumnIndex("minute"));
            String thing1 = cursor.getString(cursor.getColumnIndex("thing"));

            values1.put("year", year1);
            values1.put("month", month1);
            values1.put("day", day1);
            values1.put("hour", hour1);
            values1.put("minute", minute1);
            values1.put("thing", thing1);

            cursor.moveToNext();

            int id2 = cursor.getInt(cursor.getColumnIndex("id"));
            int year2 = cursor.getInt(cursor.getColumnIndex("year"));
            int month2 = cursor.getInt(cursor.getColumnIndex("month"));
            int day2 = cursor.getInt(cursor.getColumnIndex("day"));
            int hour2 = cursor.getInt(cursor.getColumnIndex("hour"));
            int minute2 = cursor.getInt(cursor.getColumnIndex("minute"));
            String thing2 = cursor.getString(cursor.getColumnIndex("thing"));
            cursor.moveToPrevious();
            values2.put("year", year2);
            values2.put("month", month2);
            values2.put("day", day2);
            values2.put("hour", hour2);
            values2.put("minute", minute2);
            values2.put("thing", thing2);
Log.d("gh","\n\n\n"+year1+"     "+year2);
            if(year1>year2||(year1==year2&&month1>month2)||(year1==year2&&month1==month2&&day1>day2)||(year1==year2&&month1==month2&&day1==day2&&hour1>hour2)||(year1==year2&&month1==month2&&day1==day2&&hour1==hour2&&minute1>minute2)||(year1==year2&&month1==month2&&day1==day2&&hour1==hour2&&minute1==minute2&&thing1.compareTo(thing2)>0))
            {
                db.update("Deadline",values1, "id = ?", new String[] {""+id2});
                values1.clear();
                db.update("Deadline",values2, "id = ?", new String[] {""+id1});
                values2.clear();
                continue;
            }
            else if(year1<year2||(year1==year2&&month1<month2)||(year1==year2&&month1==month2&&day1<day2)||(year1==year2&&month1==month2&&day1==day2&&hour1<hour2)||(year1==year2&&month1==month2&&day1==day2&&hour1==hour2&&minute1<minute2)||(year1==year2&&month1==month2&&day1==day2&&hour1==hour2&&minute1==minute2&&thing1.compareTo(thing2)<0)) break;
        }

    }
    /*public void addData(int year, int month, int day, int hour, int minute, String thing) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("year", year);
        values.put("month", month);
        values.put("day", day);
        values.put("hour", hour);
        values.put("minute", minute);
        values.put("thing", thing);
        db.insert("Deadline", null, values);
        values.clear();
        Cursor cursor1 = db.query("Deadline", null, null, null, null, null, null);
        if(cursor1.moveToLast()) {
            Cursor cursor2 = cursor1;
            while(cursor1.moveToPrevious()) {
                int year1 = cursor1.getInt(cursor1.getColumnIndex("year"));
                int month1 = cursor1.getInt(cursor1.getColumnIndex("month"));
                int day1 = cursor1.getInt(cursor1.getColumnIndex("day"));
                int hour1 = cursor1.getInt(cursor1.getColumnIndex("hour"));
                int minute1 = cursor1.getInt(cursor1.getColumnIndex("minute"));
                String thing1 = cursor1.getString(cursor1.getColumnIndex("thing"));
                ContentValues values1 = new ContentValues();
                values1.put("year", year1);
                values1.put("month", month1);
                values1.put("day", day1);
                values1.put("hour", hour1);
                values1.put("minute", minute1);
                values1.put("thing", thing1);

                int year2 = cursor2.getInt(cursor2.getColumnIndex("year"));
                int month2 = cursor2.getInt(cursor2.getColumnIndex("month"));
                int day2 = cursor2.getInt(cursor2.getColumnIndex("day"));
                int hour2 = cursor2.getInt(cursor2.getColumnIndex("hour"));
                int minute2 = cursor2.getInt(cursor2.getColumnIndex("minute"));
                String thing2 = cursor2.getString(cursor2.getColumnIndex("thing"));
                ContentValues values2 = new ContentValues();
                values2.put("year", year2);
                values2.put("month", month2);
                values2.put("day", day2);
                values2.put("hour", hour2);
                values2.put("minute", minute2);
                values2.put("thing", thing2);

                if(year1>year2)
                {
                    db.update("Deadline",values1, "year = ?", new String[] {""+year2});
                    values1.clear();
                    db.update("Deadline",values2, "year = ?", new String[] {""+year1});
                    values2.clear();
                    cursor2=cursor1;
                    continue;
                }
                else if(year1<year2) break;

                if(month1>month2)
                {
                    db.update("Deadline",values1, "month = ?", new String[] {""+month2});
                    values1.clear();
                    db.update("Deadline",values2, "month = ?", new String[] {""+month1});
                    values2.clear();
                    cursor2=cursor1;
                    continue;
                }
                else if(month1<month2) break;

                if(day1>day2)
                {
                    db.update("Deadline",values1, "day = ?", new String[] {""+day2});
                    values1.clear();
                    db.update("Deadline",values2, "day = ?", new String[] {""+day1});
                    values2.clear();
                    cursor2=cursor1;
                    continue;
                }
                else if(day1<day2) break;

                if(hour1>hour2)
                {
                    db.update("Deadline",values1, "hour = ?", new String[] {""+hour2});
                    values1.clear();
                    db.update("Deadline",values2, "hour = ?", new String[] {""+hour1});
                    values2.clear();
                    cursor2=cursor1;
                    continue;
                }
                else if(hour1<hour2) break;

                if(minute1>minute2)
                {
                    db.update("Deadline",values1, "minute = ?", new String[] {""+minute2});
                    values1.clear();
                    db.update("Deadline",values2, "minute = ?", new String[] {""+minute1});
                    values2.clear();
                    cursor2=cursor1;
                    continue;
                }
                else if(minute1<minute2) break;

                if(thing1.compareTo(thing2)>0)
                {
                    db.update("Deadline",values1, "thing = ?", new String[] {""+thing2});
                    values1.clear();
                    db.update("Deadline",values2, "thing = ?", new String[] {""+thing1});
                    values2.clear();
                    cursor2=cursor1;
                    continue;
                }
                else if(thing1.compareTo(thing2)<0) break;
            }
            cursor1.close();
            cursor2.close();
        }
    }*/

    public void updateData(int year1, int month1, int day1, int hour1, int minute1, String thing1,
                           int year2, int month2, int day2, int hour2, int minute2, String thing2) {
        deleteData(year1,month1,day1,hour1,minute1,thing1);
        addData(year2,month2,day2,hour2,minute2,thing2);
    }
    public void deleteData(int year, int month, int day, int hour, int minute, String thing) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Deadline", "year = ? and month = ? and day = ? and hour = ? and minute = ? and thing = ?",
                new String[]{""+year, ""+month, ""+day, ""+hour, ""+minute,thing});
    }
    public List<String[]> getData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<String[]> allData = new ArrayList<>();
        Cursor cursor = db.query("Deadline", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                int year = cursor.getInt(cursor.getColumnIndex("year"));
                int month = cursor.getInt(cursor.getColumnIndex("month"));
                int day = cursor.getInt(cursor.getColumnIndex("day"));
                int hour = cursor.getInt(cursor.getColumnIndex("hour"));
                int minute = cursor.getInt(cursor.getColumnIndex("minute"));
                String thing = cursor.getString(cursor.getColumnIndex("thing"));
                allData.add(new String[]{""+year, ""+month, ""+day, String.format("%02d",hour), String.format("%02d",minute),thing});
            }
        }
        return allData;
    }
    public List<String[]> getDataByDate(int year,int month,int day) {
        List<String[]> a = getData();
        List<String[]>b = new ArrayList<>();
        for(int i=0;i<a.size();i++)
            if(year==Integer.parseInt(a.get(i)[0])&&month==Integer.parseInt(a.get(i)[1])&&day==Integer.parseInt(a.get(i)[2]))
                b.add(a.get(i));
        return b;
    }
    public int getSaltyFish() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int cnt = 0;
        Cursor cursor = db.query("Deadline", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                int year0 = cursor.getInt(cursor.getColumnIndex("year"));
                int month0 = cursor.getInt(cursor.getColumnIndex("month"));
                int day0 = cursor.getInt(cursor.getColumnIndex("day"));
                int hour0 = cursor.getInt(cursor.getColumnIndex("hour"));
                int minute0 = cursor.getInt(cursor.getColumnIndex("minute"));
                if(year0<year)
                {
                    cnt++;
                    continue;
                }
                else if(year0>year)continue;

                if(month0<month)
                {
                    cnt++;
                    continue;
                }
                else if(month0>month)continue;

                if(day0<day)
                {
                    cnt++;
                    continue;
                }
                else if(day0>day)continue;

                if(hour0<hour)
                {
                    cnt++;
                    continue;
                }
                else if(hour0>hour)continue;

                if(minute0<minute)
                {
                    cnt++;
                    continue;
                }
                else if(minute0>=minute)continue;
            }
        }
        return cnt;
    }
    public int getFishAlive() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int cnt = 0;
        Cursor cursor = db.query("Deadline", null, null, null, null, null, null);
        if(cursor.moveToFirst())while (cursor.moveToNext())cnt++;
        return cnt-getSaltyFish();
    }
}
