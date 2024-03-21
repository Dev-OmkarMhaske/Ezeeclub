package com.tsysinfo.oneabove;
/**
 * Created by tsysinfo on 9/21/2017.
 */
public class entityWorkout {
    String warmup;
    String reps;
    String sets;
    String workout;
    String remark;
    String bodypart;
    String exercise;
    public String getWarmup(){return warmup;}
    public void setWarmup(String warmup){this.warmup=warmup;}
    public String getExercise(){return  exercise;}
    public void setExercise(String exercise) {this.exercise=exercise;}
    public String getReps() {
        return reps;
    }
    public void setReps(String reps) {
        this.reps = reps;
    }
    public String getSets() {
        return sets;
    }
    public void setSets(String sets) {
        this.sets = sets;
    }
    public String getWorkout() {
        return workout;
    }
    public void setWorkout(String workout) {
        this.workout = workout;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getBodypart() {
        return bodypart;
    }
    public void setBodypart(String bodypart) {
        this.bodypart = bodypart;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    String time;
    String id;
}
