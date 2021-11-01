package com.android.workout.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.android.workout.model.Workout;

import java.util.Date;
import java.util.UUID;

import com.android.workout.database.WorkoutDbSchema.WorkoutTable;

public class WorkoutCursorWrapper extends CursorWrapper {
    public WorkoutCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Workout getWorkout() {
        String uuidString = getString(getColumnIndex(WorkoutTable.Cols.UUID));
        String title = getString(getColumnIndex(WorkoutTable.Cols.TITLE));
        long date = getLong(getColumnIndex(WorkoutTable.Cols.DATE));
        String place = getString(getColumnIndex(WorkoutTable.Cols.PLACE));
        long startTime = getLong(getColumnIndex(WorkoutTable.Cols.START_TIME));
        long endTime = getLong(getColumnIndex(WorkoutTable.Cols.END_TIME));
        String type = getString(getColumnIndex(WorkoutTable.Cols.TYPE));

        Workout workout = new Workout(UUID.fromString(uuidString));
        workout.setTitle(title);
        workout.setDate(new Date(date));
        workout.setPlace(place);
        workout.setStartTime(new Date(startTime));
        workout.setEndTime(new Date(endTime));
        workout.setType(type);

        return workout;
    }
}
