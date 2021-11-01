package com.android.workout.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.workout.R;
import com.android.workout.WorkoutLab;
import com.android.workout.fragment.WorkoutFragment;
import com.android.workout.model.Workout;

import java.util.List;
import java.util.UUID;

public class WorkoutPagerActivity extends AppCompatActivity
        implements WorkoutFragment.Callbacks {
    private static final String EXTRA_WORKOUT_ID =
            "com.android.workoutintent.workout_id";

    private ViewPager mViewPager;
    private List<Workout> mWorkouts;

    public static Intent newIntent(Context packageContext, UUID workoutId) {
        Intent intent = new Intent(packageContext, WorkoutPagerActivity.class);
        intent.putExtra(EXTRA_WORKOUT_ID, workoutId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_pager);

        UUID workoutId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_WORKOUT_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_workout_pager_view_pager);

        mWorkouts = WorkoutLab.get(this).getWorkouts();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Workout workout = mWorkouts.get(position);
                return WorkoutFragment.newInstance(workout.getId());
            }

            @Override
            public int getCount() {
                return mWorkouts.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                Workout workout = mWorkouts.get(position);
                if (workout.getTitle() != null) {
                    setTitle(workout.getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        for (int i = 0; i < mWorkouts.size(); i++) {
            if (mWorkouts.get(i).getId().equals(workoutId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onWorkoutUpdated(Workout workout) {

    }
}
