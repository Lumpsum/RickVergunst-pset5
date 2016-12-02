package com.example.rick.rickvergunst_pset5;

/**
 * Created by Rick on 11/27/2016.
 */

//Todoitem object with getters and setters
public class TodoItem {
    private String title;
    private String completed;
    private String picture;
    private String duration;
    private String backgroundColor;
    private String inProgress;
    private String description;

    //Returns the title
    public String getTitle() {
        return title;
    }

    //Sets the title
    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    //Retrieves the background color
    public String getBackgroundColor() {
        return backgroundColor;
    }

    //Sets the background color
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getInProgress() {
        return inProgress;
    }

    public void setInProgress(String inProgress) {
        this.inProgress = inProgress;
    }

    //Retrieves the description
    public String getDescription() {
        return description;
    }

    //Sets the description
    public void setDescription(String description) {
        this.description = description;
    }
}
