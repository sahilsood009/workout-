package com.android.workout.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.workout.R;
import com.android.workout.model.Workout;
import com.android.workout.WorkoutLab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class WorkoutFragment extends Fragment {

    private static final String ARG_WORKOUT_ID = "workout_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_START_TIME = "DialogStartTime";
    private static final String DIALOG_END_TIME = "DialogEndTime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_START_TIME = 1;
    private static final int REQUEST_END_TIME = 2;

    private Workout mWorkout;
    private EditText mTitleField;
    private Button mDateButton;
    private EditText mPlaceField;
    private Button mStartTimeButton;
    private Button mEndTimeButton;
    private RadioGroup mActivityType;
    private RadioButton mActivityTypeIndividual;
    private RadioButton mActivityTypeGroup;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onWorkoutUpdated(Workout workout);
    }

    public static WorkoutFragment newInstance(UUID workoutId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WORKOUT_ID, workoutId);

        WorkoutFragment fragment = new WorkoutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID workoutId = (UUID) getArguments().getSerializable(ARG_WORKOUT_ID);
        mWorkout = WorkoutLab.get(getActivity()).getWorkout(workoutId);
    }

    @Override
    public void onPause() {
        super.onPause();

        WorkoutLab.get(getActivity())
                .updateWorkout(mWorkout);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_workout, container, false);

        mTitleField = (EditText) v.findViewById(R.id.workout_title);
        mTitleField.setText(mWorkout.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getActivity() == null) {
                    return;
                }
                mWorkout.setTitle(s.toString());
                updateWorkout();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mDateButton = (Button) v.findViewById(R.id.workout_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mWorkout.getDate());
                dialog.setTargetFragment(WorkoutFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mPlaceField = (EditText) v.findViewById(R.id.workout_place);
        mPlaceField.setText(mWorkout.getPlace());
        mPlaceField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getActivity() == null) {
                    return;
                }
                mWorkout.setPlace(s.toString());
                updateWorkout();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mStartTimeButton = (Button) v.findViewById(R.id.workout_start_time);
        updateStartTime();
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mWorkout.getStartTime());
                dialog.setTargetFragment(WorkoutFragment.this, REQUEST_START_TIME);
                dialog.show(manager, DIALOG_START_TIME);
            }
        });

        mEndTimeButton = (Button) v.findViewById(R.id.workout_end_time);
        updateEndTime();
        mEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mWorkout.getEndTime());
                dialog.setTargetFragment(WorkoutFragment.this, REQUEST_END_TIME);
                dialog.show(manager, DIALOG_END_TIME);
            }
        });

        mActivityTypeIndividual = (RadioButton) v.findViewById(R.id.workout_individual);
        mActivityTypeGroup = (RadioButton) v.findViewById(R.id.workout_group);
        mActivityType = (RadioGroup) v.findViewById(R.id.workout_activity_type);
        updateActivityType();
        mActivityType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedType = mActivityType.findViewById(checkedId);
                mWorkout.setType(selectedType.getText().toString());
                updateWorkout();
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mWorkout.setDate(date);
            updateWorkout();
            updateDate();
        } else if (requestCode == REQUEST_START_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mWorkout.setStartTime(date);
            updateWorkout();
            updateStartTime();
        } else if (requestCode == REQUEST_END_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mWorkout.setEndTime(date);
            updateWorkout();
            updateEndTime();
        }
    }

    private void updateWorkout() {
        WorkoutLab.get(getActivity()).updateWorkout(mWorkout);
        mCallbacks.onWorkoutUpdated(mWorkout);
    }

    private void updateDate() {
        String date = new SimpleDateFormat("dd MMM yyyy").format(mWorkout.getDate());
        mDateButton.setText(date);
    }

    private void updateStartTime() {
        String startTime = new SimpleDateFormat("hh:mm a").format(mWorkout.getStartTime());
        mStartTimeButton.setText(startTime);
    }

    private void updateEndTime() {
        String endTime = new SimpleDateFormat("hh:mm a").format(mWorkout.getEndTime());
        mEndTimeButton.setText(endTime);
    }

    private void updateActivityType() {
        String activityType = mWorkout.getType();
        if(activityType == null || activityType.contentEquals(getString(R.string.workout_individual_label))) {
            mActivityType.check(mActivityTypeIndividual.getId());
        } else if(activityType.contentEquals(getString(R.string.workout_group_label))) {
            mActivityType.check(mActivityTypeGroup.getId());
        }
    }
}