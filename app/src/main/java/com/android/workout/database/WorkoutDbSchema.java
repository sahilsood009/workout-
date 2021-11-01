package com.android.workout.database;

public class WorkoutDbSchema {
    public static final class WorkoutTable {
        public static final String NAME = "workouts";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String PLACE = "place";
            public static final String START_TIME = "start_time";
            public static final String END_TIME = "end_time";
            public static final String TYPE = "type";
        }
    }
}
