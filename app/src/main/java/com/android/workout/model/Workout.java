package com.android.workout.model;

import java.util.Date;
import java.util.UUID;

public class Workout {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mPlace;
    private Date mStartTime;
    private Date mEndTime;
    private String mType;

    public Workout() {
        this(UUID.randomUUID());
    }

    public Workout(UUID id) {
        mId = id;
        mDate = new Date();
        mStartTime = new Date();
        mEndTime = new Date();
    }
    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String mPlace) {
        this.mPlace = mPlace;
    }

    public Date getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Date mStartTime) {
        this.mStartTime = mStartTime;
    }

    public Date getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Date mEndTime) {
        this.mEndTime = mEndTime;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
