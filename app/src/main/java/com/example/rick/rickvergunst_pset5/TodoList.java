package com.example.rick.rickvergunst_pset5;

import java.util.List;

/**
 * Created by Rick on 11/27/2016.
 */

//Todolist object with getters and setters
public class TodoList {
    private String title;
    private List<TodoItem> list;

    //Retrieves the title
    public String getTitle() {
        return title;
    }

    //Sets the title
    public void setTitle(String title) {
        this.title = title;
    }

    //Retrieves the list
    public List<TodoItem> getList() {
        return list;
    }

    //Sets the list
    public void setList(List<TodoItem> list) {
        this.list = list;
    }
}
