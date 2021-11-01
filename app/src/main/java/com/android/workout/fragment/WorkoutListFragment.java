package com.android.workout.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.workout.R;
import com.android.workout.WorkoutLab;
import com.android.workout.model.Workout;

import java.text.SimpleDateFormat;
import java.util.List;

public class WorkoutListFragment extends Fragment {

    private RecyclerView mWorkoutRecyclerView;
    private WorkoutAdapter mWorkoutAdapter;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onWorkoutSelected(Workout workout);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_list, container, false);

        mWorkoutRecyclerView = (RecyclerView) view
                .findViewById(R.id.workout_recycler_view);
        mWorkoutRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mWorkoutRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_workout_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_workout:
                Workout workout = new Workout();
                WorkoutLab.get(getActivity()).addWorkout(workout);
                updateUI();
                mCallbacks.onWorkoutSelected(workout);
                Toast.makeText(getContext(), "New activity has been added successfully.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
        WorkoutLab workoutLab = WorkoutLab.get(getActivity());
        List<Workout> workouts = workoutLab.getWorkouts();

        if (mWorkoutAdapter == null) {
            mWorkoutAdapter = new WorkoutAdapter(workouts);
            mWorkoutRecyclerView.setAdapter(mWorkoutAdapter);
        } else {
            mWorkoutAdapter.setWorkouts(workouts);
            mWorkoutAdapter.notifyDataSetChanged();
        }
    }

    private class WorkoutHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDatePlaceTextView;
        private Button mDeleteButton;

        private Workout mWorkout;

        public WorkoutHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_workout_title_text_view);
            mDatePlaceTextView = (TextView) itemView.findViewById(R.id.list_item_workout_date_place_text_view);
            mDeleteButton = (Button) itemView.findViewById(R.id.list_item_workout_delete_button);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure you want to delete selected activity?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            WorkoutLab.get(getActivity()).deleteWorkout(mWorkout);
                            updateUI();
                            Toast.makeText(getContext(), "Activity has been deleted.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }

        public void bindWorkout(Workout workout) {
            mWorkout = workout;
            String title = mWorkout.getTitle();
            if(title == null) {
                title = "<Add Title>";
            }
            mTitleTextView.setText(title);
            String date = new SimpleDateFormat("dd MMM yyyy").format(mWorkout.getDate());
            String place = mWorkout.getPlace();
            if(place == null) {
                place = "<Add Place>";
            }
            mDatePlaceTextView.setText(place + " - " + date);
        }

        @Override
        public void onClick(View v) {
            mCallbacks.onWorkoutSelected(mWorkout);
        }
    }

    private class WorkoutAdapter extends RecyclerView.Adapter<WorkoutHolder> {

        private List<Workout> mWorkouts;

        public WorkoutAdapter(List<Workout> workouts) {
            mWorkouts = workouts;
        }

        @Override
        public WorkoutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_workout, parent, false);
            return new WorkoutHolder(view);
        }

        @Override
        public void onBindViewHolder(WorkoutHolder holder, int position) {
            Workout workout = mWorkouts.get(position);
            holder.bindWorkout(workout);
        }

        @Override
        public int getItemCount() {
            return mWorkouts.size();
        }

        public void setWorkouts(List<Workout> workouts) {
            mWorkouts = workouts;
        }
    }
}
