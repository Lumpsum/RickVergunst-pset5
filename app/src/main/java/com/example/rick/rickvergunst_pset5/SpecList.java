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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Rick on 11/27/2016.
 */
public class SpecList extends Fragment {

    //Initialize variables
    ListView lv;
    EditText et;
    ArrayList<String> array;
    Button button;
    ArrayAdapter<String> adapter;
    MainList mainList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Assign the xml items
        View view = inflater.inflate(R.layout.spec_list_fragment, container, false);
        lv = (ListView)view.findViewById(R.id.specListView);
        et = (EditText)view.findViewById(R.id.specEditText);

        //Assignment of the other fragment in order to call that fragment for functions
        mainList = (MainList)getFragmentManager().findFragmentById(R.id.mainList);

        //Reads the files from the directory
        array = new ArrayList<String>();
        File directory = new File(String.valueOf(getActivity().getFilesDir()));
        File[] files = directory.listFiles();
        //Reads every file except instant-run
        for (int i = 0; i < files.length; i++) {
            if (!files[i].getName().equals("instant-run")) {
                FileInputStream fIn = null;
                try {
                    fIn = getActivity().openFileInput(String.valueOf(files[i].getName()));
                    Scanner scanner = new Scanner(fIn);
                    array.clear();

                    //Retrieves the text after the first space of every line and adds that to an array
                    while (scanner.hasNextLine()) {
                        String str = scanner.nextLine();
                        array.add(str.substring(str.indexOf(' ')+1));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        //Creates an adapter the handles the background color as well
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, array) {

            //Override the getView function to change the color
            @Override
            public View getView(int position, View conView, ViewGroup parent) {
                View view = super.getView(position, conView, parent);
                TextView specListText = (TextView) view.findViewById(R.id.textview);
                String background = mainList.getBackground(specListText.getText().toString());
                if (background.equals("green")) {
                    view.setBackgroundColor(getResources().getColor(R.color.green));
                }
                else {
                    view.setBackgroundColor(getResources().getColor(R.color.white));
                }
                return view;
            }
        };

        //Assign the adapter
        lv.setAdapter(adapter);

        //Button handler
        button = (Button)view.findViewById(R.id.addSpecList);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Adds the item to the list and calls a function in mainlist to add the item to the tdm
                String userValue = et.getText().toString();
                TodoItem tdi = new TodoItem();
                tdi.setDescription(userValue);
                mainList.addItem(tdi);
                array.add(userValue);
                adapter.notifyDataSetChanged();
                et.setText("");
            }
        });

        //Listview longclick handler
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //Removes the item from the listview
                array.remove(position);
                adapter.notifyDataSetChanged();
                TextView specListText = (TextView) view.findViewById(R.id.textview);
                String listText = specListText.getText().toString();

                //Calls a function to delete the item from the tdm
                mainList.deleteItem(listText);
                return true;
            }
        });

        //Listview onclick handler
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView specListText = (TextView) view.findViewById(R.id.textview);
                String listText = specListText.getText().toString();

                //Function to change the background color in the tdm
                mainList.setBackground(listText);
                adapter.notifyDataSetChanged();
            }
        });

        adapter.notifyDataSetChanged();

        return view;
    }

    //Function to change the listview items to the current selected one in mainlist
    public void changeList(List<TodoItem> listArray) {
        array.clear();
        for (TodoItem item : listArray) {
            array.add(item.getDescription());
        }
        adapter.notifyDataSetChanged();
    }

    //Clears the array of all items
    public void emptyList() {
        array.clear();
        adapter.notifyDataSetChanged();
    }

}
