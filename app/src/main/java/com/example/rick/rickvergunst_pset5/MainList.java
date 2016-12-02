package com.example.rick.rickvergunst_pset5;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Rick on 11/27/2016.
 */
public class MainList extends Fragment {
    //Initialize the variables
    ListView lv;
    EditText et;
    Button button;
    public TodoManager tdm;
    List<TodoList> tdl;
    ArrayList<String> array;
    ArrayList<String> specArray;
    String selected;
    SpecList specList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Assign the view and elements of the xml
        View view = inflater.inflate(R.layout.main_list_fragment, container, false);
        lv = (ListView)view.findViewById(R.id.mainListView);
        et = (EditText)view.findViewById(R.id.mainEditText);

        //Variable to keep track of the selected list
        selected = "";

        //Fragment assignment in order to call functions
        specList = (SpecList)getFragmentManager().findFragmentById(R.id.specList);

        //Assignment of the lists
        tdl = new ArrayList<TodoList>();
        array = new ArrayList<String>();
        specArray = new ArrayList<String>();

        //Search trough the local files
        File directory = new File(String.valueOf(getActivity().getFilesDir()));
        File[] files = directory.listFiles();

        //Loop through the files in the folder
        for (int i = 0; i < files.length; i++)
        {
            //Look at every file except for instant-run
            if (!files[i].getName().equals("instant-run")) {
                //Create a todolist item and assign an array to that item
                array.add(files[i].getName());
                TodoList tdlist = new TodoList();
                tdlist.setTitle(files[i].getName());
                List<TodoItem> list = new ArrayList<TodoItem>();
                try {
                    //Read the file and assign a scanner
                    FileInputStream fIn = getActivity().openFileInput(String.valueOf(files[i].getName()));
                    Scanner scanner = new Scanner(fIn);
                    specArray.clear();
                    while (scanner.hasNextLine()) {
                        //Read every line inside the file which has a specific layout
                        //Every line starts with a background color and then a space
                        //After that the decription of the file comes
                        TodoItem tdi = new TodoItem();
                        String str = scanner.nextLine();
                        tdi.setTitle(files[i].getName());

                        //Line is split and assign here
                        tdi.setDescription(str.substring(str.indexOf(' ')+1));
                        tdi.setBackgroundColor(str.substring(0,str.indexOf(' ')));
                        list.add(tdi);
                        specArray.add(tdi.getDescription());
                    }
                    // After filling the list, add the list to the todolist
                    tdlist.setList(list);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //Add the full todolist array with items and set the start title
                tdl.add(tdlist);
                selected = tdlist.getTitle();
            }
        }

        //Assign the todolist to the manager
        tdm = TodoManager.getInstance();
        tdm.setList(tdl);

        //Set the listview adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, array);
        lv.setAdapter(adapter);

        //Assign the button and set an onClick handler
        button = (Button)view.findViewById(R.id.addMainList);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the user input and set the new selected
                String userValue = et.getText().toString();
                selected = userValue;
                TodoList tdlUser = new TodoList();
                tdlUser.setTitle(userValue);

                //Make a new todolist item, which is assigned to the manager
                List<TodoItem> list = new ArrayList<TodoItem>();
                tdlUser.setList(list);
                tdl.add(tdlUser);
                array.add(userValue);
                adapter.notifyDataSetChanged();

                //Reset the user input
                et.setText("");

                //Check the todolist for the selected title and call a function in speclist that changes the listview there
                for (TodoList toDoList : tdl) {
                    if (toDoList.getTitle().equals(userValue
                    )) {
                        SpecList specList = (SpecList)getFragmentManager().findFragmentById(R.id.specList);
                        specList.changeList(toDoList.getList());
                    }
                }
            }
        });

        //Long item click listener that deletes an item
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView)view.findViewById(R.id.textview);
                String text = textView.getText().toString();
                TodoList removeList = null;

                //Searches trough the todolist items and finds the one that is pressed
                for (TodoList list : tdl) {
                    if (list.getTitle().equals(text)) {
                        removeList = list;
                    }
                }
                //Deletes the item from the tdm and empties the other listview as well
                specList = (SpecList)getFragmentManager().findFragmentById(R.id.specList);
                specList.emptyList();
                tdl.remove(removeList);
                array.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        //onClick handler that specifies which list is selected
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView mainListText = (TextView) view.findViewById(R.id.textview);
                String listText = mainListText.getText().toString();
                selected = listText;

                //Loops through the todolist items and changes the other listview accordingly
                for (TodoList list : tdl) {
                    if (list.getTitle().equals(listText)) {
                        specList = (SpecList)getFragmentManager().findFragmentById(R.id.specList);
                        specList.changeList(list.getList());
                    }
                }
            }
        });

        return view;
    }

    //Function that is called when the application is paused
    @Override
    public void onPause() {
        super.onPause();
        File directory = new File(String.valueOf(getActivity().getFilesDir()));
        File[] files = directory.listFiles();

        //Deletes every file that is not instant-run
        for (int i = 0; i < files.length; i++)
        {
            if (!files[i].getName().equals("instant-run")) {
                files[i].delete();
            }
        }

        //Creates a file for every todolist and writes a line for every todoitem
        for (TodoList tdl : tdm.getList()) {
            try {
                FileOutputStream fOut = getActivity().openFileOutput(tdl.getTitle(), getActivity().MODE_PRIVATE);
                for (TodoItem tdi : tdl.getList()) {
                    String str = tdi.getBackgroundColor() + " " + tdi.getDescription() + System.getProperty("line.separator");
                    fOut.write(str.getBytes());
                }

                //Closes the writing
                fOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Function that adds a todoitem that is created in the other fragment and sets the basic values
    public void addItem(TodoItem tdi) {
        tdi.setTitle(selected);
        tdi.setBackgroundColor("white");
        for (TodoList list : tdl) {
            if (list.getTitle().equals(selected)) {
                list.getList().add(tdi);
            }
        }
    }

    //Function that deletes a todoitem from the tdm
    public void deleteItem(String value) {
        for (TodoList list : tdl) {
            if (list.getTitle().equals(selected)) {
                for (TodoItem tdi : list.getList()) {
                    if (tdi.getDescription().equals(value)) {
                        list.getList().remove(tdi);
                    }
                }
            }
        }
    }

    //Function that returns the current background color of a todoitem as defined in the tdm
    public String getBackground(String title) {
        for (TodoList list : tdl) {
            if (list.getTitle().equals(selected)) {
                for (TodoItem tdi : list.getList()) {
                    if (tdi.getDescription().equals(title)) {
                        return tdi.getBackgroundColor();
                    }
                }
            }
        }
        return "";
    }

    //Function that assigns a new background color when a todoitem is clicked
    public void setBackground(String title) {
        for (TodoList list : tdl) {
            if (list.getTitle().equals(selected)) {
                for (TodoItem tdi : list.getList()) {
                    if (tdi.getDescription().equals(title)) {
                        if (tdi.getBackgroundColor().equals("white")) {
                            tdi.setBackgroundColor("green");
                        }
                        else {
                            tdi.setBackgroundColor("white");
                        }
                    }
                }
            }
        }
    }
}
