package com.example.tobias.garritsen_pset4_todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;

    EditText editText;

    ArrayList<String> allToDos;

    // to do list
    ListView toDoList;
    ArrayList<String> toDos;
    ArrayAdapter toDoAdapter;

    // done list
    ListView checkedList;
    ArrayList<String> dones;
    ArrayAdapter doneAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        allToDos = dbHelper.read();
//        checkedList.setOnItemLongClickListener(new MyLongClickListener());
        createLists();
    }

    public void createLists() {
        toDoList = (ListView) findViewById(R.id.listViewTodo);
        checkedList = (ListView) findViewById(R.id.listViewDone);
        editText = (EditText) findViewById(R.id.toDo);
        toDos = new ArrayList<>();
        dones = new ArrayList<>();
        int allToDosSize = allToDos.size();
        for (int i=0; i < allToDosSize; i++) {
            String task = allToDos.get(i);
            String status = (String) getToDo(task, 0);
            if (status.equals("notDone")) {
                toDos.add(task);
            } else {
                dones.add(task);
            }
        }
        toDoAdapter = new ArrayAdapter(this, R.layout.list_layout, R.id.notCheckedView, toDos);
        toDoList.setAdapter(toDoAdapter);

        doneAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dones);
        checkedList.setAdapter(doneAdapter);
        setListeners();

        //        toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            // get page of movie by clicking on one of the movie names
//            public void onItemClick(AdapterView parent, View view, int position, long id) {
//                TextView taskView = (TextView) view;
//                String task = taskView.getText().toString();
//                int idToDo = (Integer) getToDo(task, 1);
//                dbHelper.update(idToDo, "done");
//                toDoAdapter.remove(task);
//                doneAdapter.add(task);
//                toDoAdapter.notifyDataSetChanged();
//                DoneAdapter.notifyDataSetChanged();
//            }
//        });
    }

    public void addToDo(View view) {
        String task = editText.getText().toString();
        ToDoClass toDo = new ToDoClass(task, "notDone");
        dbHelper.create(toDo);
        toDoAdapter.add(toDo.todoPub);
        toDoAdapter.notifyDataSetChanged();
    }

    public void deleteTaskDone(String task) {
        int id = (Integer) getToDo(task, 1);
        dbHelper.delete(id);
        doneAdapter.remove(task);
        doneAdapter.notifyDataSetChanged();
    }

//    public void setListeners() {
//        checkedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long viewId) {
//                String task = checkedList.getItemAtPosition(position).toString();
//                deleteTaskDone(task);
//                return true;
//            }
//        });
//    }

    public void setListeners(){
        checkedList = (ListView) findViewById(R.id.listViewDone);
        AdapterView.OnItemLongClickListener myLongClickListener = new MyLongClickListener();
        checkedList.setOnItemLongClickListener(myLongClickListener);
    }
//
    private class MyLongClickListener implements AdapterView.OnItemLongClickListener {
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long viewId) {
            String task = checkedList.getItemAtPosition(position).toString();
            deleteTaskDone(task);
            return true;
        }
    }

    // using checkbox because I was unable to get OnItemClickListener working
    // the checkboxes have only the purpose of making it clickable.
    //
    public void update(View view) {
        RelativeLayout layout = (RelativeLayout) view.getParent();
        TextView taskView = (TextView) layout.getChildAt(0);
        String task = taskView.getText().toString();
        int id = (Integer) getToDo(task, 1);
        dbHelper.update(id, "done");
        toDoAdapter.remove(task);
        doneAdapter.add(task);
        toDoAdapter.notifyDataSetChanged();
        doneAdapter.notifyDataSetChanged();
        CheckBox checkbox = (CheckBox) view;
        checkbox.setChecked(false);
    }

    public Object getToDo(String todoName, int todoVScheck) {
        int id;
        String check;
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