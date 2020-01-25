package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final String name="ToDoList";
    private static final String DB_TABLE="Task";
    private static final String DB_COLUMN="Taskname";
    private static final int version = 1;


    public DbHelper(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql= String.format("CREATE TABLE %s(ID INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL); " , DB_TABLE,DB_COLUMN);
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql= String.format("DELETE TABLE IF EXISTS %s" , DB_TABLE);
        db.execSQL(sql);
        onCreate(db);
    }
    public void insertTask(String task){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(DB_COLUMN,task);
        db.insertWithOnConflict(DB_TABLE,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
    public void deleteTask(String task){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(DB_TABLE,DB_COLUMN + " = ?",new String[]{task});
        db.close();
    }
    public ArrayList<String> gettasklist(){
        ArrayList<String> tasklist=new ArrayList<>();

        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.query(DB_TABLE,new String[]{DB_COLUMN},null,null,null,null,null);

       while (c.moveToNext()){
           int index=c.getColumnIndex(DB_COLUMN);
           tasklist.add(c.getString(index));
        }
        c.close();
       db.close();
        return tasklist;
    }
}
