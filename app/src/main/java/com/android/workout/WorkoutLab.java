package com.android.workout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.workout.database.WorkoutBaseHelper;
import com.android.workout.database.WorkoutCursorWrapper;
import com.android.workout.database.WorkoutDbSchema.WorkoutTable;
import com.android.workout.model.Workout;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkoutLab {
    private static WorkoutLab sWorkoutLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static WorkoutLab get(Context context) {
        if (sWorkoutLab == null) {
            sWorkoutLab = new WorkoutLab(context);
        }
        return sWorkoutLab;
    }

    private WorkoutLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new WorkoutBaseHelper(mContext)
                .getWritableDatabase();
    }


    public void addWorkout(Workout c) {
        ContentValues values = getContentValues(c);

        mDatabase.insert(WorkoutTable.NAME, null, values);
    }

    public List<Workout> getWorkouts() {
        List<Workout> workouts = new ArrayList<>();

        WorkoutCursorWrapper cursor = queryWorkouts(null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            workouts.add(cursor.getWorkout());
            cursor.moveToNext();
        }
        cursor.close();

        return workouts;
    }

    public Workout getWorkout(UUID id) {
        WorkoutCursorWrapper cursor = queryWorkouts(
                WorkoutTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getWorkout();
        } finally {
            cursor.close();
        }
    }

    public void updateWorkout(Workout workout) {
        String uuidString = workout.getId().toString();
        ContentValues values = getContentValues(workout);

        mDatabase.update(WorkoutTable.NAME, values,
                WorkoutTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public void deleteWorkout(Workout workout) {
        String uuidString = workout.getId().toString();
        ContentValues values = getContentValues(workout);

        mDatabase.delete(WorkoutTable.NAME, WorkoutTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private static ContentValues getContentValues(Workout workout) {
        ContentValues values = new ContentValues();
        values.put(WorkoutTable.Cols.UUID, workout.getId().toString());
        values.put(WorkoutTable.Cols.TITLE, workout.getTitle());
        values.put(WorkoutTable.Cols.DATE, workout.getDate().getTime());
        values.put(WorkoutTable.Cols.PLACE, workout.getPlace());
        values.put(WorkoutTable.Cols.START_TIME, workout.getStartTime().getTime());
        values.put(WorkoutTable.Cols.END_TIME, workout.getEndTime().getTime());
        values.put(WorkoutTable.Cols.TYPE, workout.getType());

        return values;
    }

    private WorkoutCursorWrapper queryWorkouts(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                WorkoutTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new WorkoutCursorWrapper(cursor);
    }
}
