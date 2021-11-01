package com.android.workout.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.workout.database.WorkoutDbSchema.WorkoutTable;

public class WorkoutBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "WorkoutBaseHelper";
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "workoutBase.db";

    public WorkoutBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + WorkoutTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                WorkoutTable.Cols.UUID + ", " +
                WorkoutTable.Cols.TITLE + ", " +
                WorkoutTable.Cols.DATE + ", " +
                WorkoutTable.Cols.PLACE + ", " +
                WorkoutTable.Cols.START_TIME + ", " +
                WorkoutTable.Cols.END_TIME + ", " +
                WorkoutTable.Cols.TYPE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
