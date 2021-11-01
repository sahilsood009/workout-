package com.android.workout.activity;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.android.workout.R;
import com.android.workout.SingleFragmentActivity;
import com.android.workout.fragment.WorkoutFragment;
import com.android.workout.fragment.WorkoutListFragment;
import com.android.workout.model.Workout;

public class WorkoutListActivity extends SingleFragmentActivity
        implements WorkoutListFragment.Callbacks, WorkoutFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new WorkoutListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onWorkoutSelected(Workout workout) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = WorkoutPagerActivity.newIntent(this, workout.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = WorkoutFragment.newInstance(workout.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onWorkoutUpdated(Workout workout) {
        WorkoutListFragment listFragment = (WorkoutListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
