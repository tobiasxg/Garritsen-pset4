package com.example.tobias.garritsen_pset4_todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;

    ListView toDoList;
    ListView checkedList;
    EditText taskBar;
    ArrayList<String> tasks;
    ArrayList<String> toDoTasks;
    ArrayList<String> doneTasks;
    ArrayAdapter adapter;
    ArrayAdapter adapterDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toDoList = (ListView) findViewById(R.id.listView);
        checkedList = (ListView) findViewById(R.id.doneView);
        taskBar = (EditText) findViewById(R.id.toDo);
        dbHelper = new DBHelper(this);
        tasks = dbHelper.read();
        // divides the tasks between the done tasks and the tasks yet to be executed
        setLists();
        // adapter for the to do list
        adapter = new ArrayAdapter(this, R.layout.list_layout, R.id.taskView, toDoTasks);
        toDoList.setAdapter(adapter);
        // adapter for the done list
        adapterDone = new ArrayAdapter(this, android.R.layout.simple_list_item_1, doneTasks);
        checkedList.setAdapter(adapterDone);
        // listeners for both lists
        setListeners();
    }

    public void addToDo(View view) {
        String task = taskBar.getText().toString();
        // creat task obect where status is to be done on default
        ToDoClass toDo = new ToDoClass(task, "notChecked");
        dbHelper.create(toDo);
        // update adapter
        adapter.add(toDo.todoPub);
        adapter.notifyDataSetChanged();
    }

    public void deleteTaskDone(String task) {
        int id = (Integer) getToDo(task, 1);
        dbHelper.delete(id);
        // update adapter
        adapterDone.remove(task);
        adapterDone.notifyDataSetChanged();
    }


    public void setListeners() {
        checkedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long viewId) {
                String task = checkedList.getItemAtPosition(position).toString();
                deleteTaskDone(task);
                return true;
            }
        });
    }

    public Object getToDo(String todoName, int todoVScheck) {
        int id = 0;
        String check = null;
        ArrayList<HashMap<String, String>> idHashMap = dbHelper.getData(todoVScheck);//id=1 check=0
        HashMap hashMapTemp;
        String todoTitle;
        for (int i = 0; i < idHashMap.size(); i++) {
            hashMapTemp = idHashMap.get(i);
            todoTitle = (String) hashMapTemp.get("todoTitle");
            if (todoTitle.equals(todoName)) {
                if (todoVScheck == 1) {
                    String tempid = (String) hashMapTemp.get("id");
                    id = Integer.parseInt(tempid);
                    return id;
                } else {
                    check = (String) hashMapTemp.get("status");
                    return check;
                }
            }
        }
        return null;
    }
}