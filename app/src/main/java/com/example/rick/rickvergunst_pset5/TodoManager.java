package com.example.rick.rickvergunst_pset5;

import java.util.List;

/**
 * Created by Rick on 11/27/2016.
 */

//Class that acts as a manager of the todolist and todoitem objects
public class TodoManager {
    private static TodoManager toDoManager = new TodoManager( );
    private List<TodoList> list;

    private TodoManager() {};

    //Created as a singleton
    public static TodoManager getInstance() {
        return toDoManager;
    }

    //Retrieves the todoolist array
    protected List<TodoList> getList() {
        return list;
    }

    //Sets the todolist array
    protected void setList(List<TodoList> list) {
        this.list = list;
    }

    protected static void readTodos() {

    }

    protected static void writeTodos() {

    }
}
