package edu.temple.crystalcube11;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hongear on 4/22/17.
 */
public class ToDoListActivity extends AppCompatActivity {

    private static final String TAG = "ToDoListActivity";

    private ListView mTaskListView;

    private DatabaseReference databaseRef;
    private DatabaseReference cubeRef;
    private DatabaseReference userRef;
    private DatabaseReference userDataRef;
    private DatabaseReference taskRef;

    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        databaseRef = FirebaseDatabase.getInstance().getReference(); // root ref


        // todo grab user id from login to use it to get user data reference
        userDataRef = FirebaseDatabase.getInstance().getReference("crystalCubes/crystal_chan_6/user/ff629b29-9e3a-4de3-9304-2bf209dd9c39");

        mTaskListView = (ListView) findViewById(R.id.list_todo);

        // pull list from firebase and display in list view
        updateUI();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Log.d(TAG, "Add a new task");
            // generate dialogue
            final EditText taskEditText = new EditText(this);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Add a new task")
                    .setMessage("What do you want to do next?")
                    .setView(taskEditText)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String task = String.valueOf(taskEditText.getText());
                            Log.d(TAG, "Task to add: " + task);
                            // insert to firebase
                            insertTaskToFirebase(task);
                            updateUI();

                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void insertTaskToFirebase(String task) {
        /*
            get database reference at to-do list node
            create random key then add child {task:"task"}
        */

        // generate random id for task node
        String mKey = userDataRef.child("todolist").push().getKey();

        // set task and id for newTask
        ToDoList newTask = new ToDoList(task, mKey);

        // convert ToDoList object to hash map
        Map<String, Object> postValues = newTask.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        // post content to new node
        childUpdates.put("/todolist/" + mKey, postValues);
        userDataRef.updateChildren(childUpdates);
    }

    private void updateUI() {
        taskRef = userDataRef.child("todolist").getRef();

        //todo iterate thru firebase todolist node for task with "task" key
        taskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // content come as hash map, store are list of todoList object
                List<ToDoList> todoList = new ArrayList<>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    todoList.add(child.getValue(ToDoList.class));
                }

                // add task from list of todoList object to array list of string
                ArrayList<String> taskList = new ArrayList<>();
                for(int i=0;i<todoList.size();i++){
                    taskList.add(todoList.get(i).getTask());
                }

                // set array list to list view adapter
                if (mAdapter == null) {
                    mAdapter = new ArrayAdapter<>(ToDoListActivity.this,
                            R.layout.item_todo,
                            R.id.task_title,
                            taskList);
                    mTaskListView.setAdapter(mAdapter);
                } else {
                    mAdapter.clear();
                    mAdapter.addAll(taskList);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        final String task = String.valueOf(taskTextView.getText());

        taskRef = userDataRef.child("todolist").getRef();

        taskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // content come as hash map, store are list of todoList object
                List<ToDoList> todoList = new ArrayList<>();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    todoList.add(child.getValue(ToDoList.class));
                    if(task.equals(String.valueOf(child.getValue()))) {
                        //todo findout why string compare not working; need for remove task off firebase
                        Log.d("delete task", String.valueOf(child.getValue()));
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
