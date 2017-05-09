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
    ListView toDoList;
    ListView checkedList;
    EditText editText;
    ArrayList<String> allToDos;
    ArrayList<String> toDos;
    ArrayList<String> dones;
    ArrayAdapter adapter;
    ArrayAdapter adapterDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        allToDos = dbHelper.read();
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
            if (status.equals("done")) {
                toDos.add(task);
            } else {
                dones.add(task);
            }
        }
        adapter = new ArrayAdapter(this, R.layout.list_layout, R.id.notCheckedView, toDos);
        toDoList.setAdapter(adapter);

        adapterDone = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dones);
        checkedList.setAdapter(adapterDone);
        setListeners();
        //        toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            // get page of movie by clicking on one of the movie names
//            public void onItemClick(AdapterView parent, View view, int position, long id) {
//                TextView taskView = (TextView) view;
//                String task = taskView.getText().toString();
//                int idToDo = (Integer) getToDo(task, 1);
//                dbHelper.update(idToDo, "done");
//                adapter.remove(task);
//                adapterDone.add(task);
//                adapter.notifyDataSetChanged();
//                adapterDone.notifyDataSetChanged();
//            }
//        });
    }

    public void addToDo(View view) {
        String task = editText.getText().toString();
        ToDoClass toDo = new ToDoClass(task, "notDone");
        dbHelper.create(toDo);
        adapter.add(toDo.todoPub);
        adapter.notifyDataSetChanged();
    }

    public void deleteTaskDone(String task) {
        int id = (Integer) getToDo(task, 1);
        dbHelper.delete(id);
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

//    public void setListeners(){
//        checkedList = (ListView) findViewById(R.id.doneView);
//        AdapterView.OnItemLongClickListener myLongClickListener = new MyLongClickListener();
//        checkedList.setOnLongClickListener(myLongClickListener);
//    }
//
//    private class MyLongClickListener implements AdapterView.OnItemLongClickListener {
//        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long viewId) {
//            String task = checkedList.getItemAtPosition(position).toString();
//            deleteTaskDone(task);
//            return true;
//        }
//    }

    public void update(View view) {
        RelativeLayout layout = (RelativeLayout) view.getParent();
        TextView taskView = (TextView) layout.getChildAt(0);
        String task = taskView.getText().toString();
        CheckBox checkbox = (CheckBox) view;
        int id = (Integer) getToDo(task, 1);
        dbHelper.update(id, "done");
        adapter.remove(task);
        adapterDone.add(task);
        adapter.notifyDataSetChanged();
        adapterDone.notifyDataSetChanged();
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