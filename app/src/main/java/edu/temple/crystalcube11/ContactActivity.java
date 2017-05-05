package edu.temple.crystalcube11;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactActivity extends AppCompatActivity {

    private static final String TAG = "ToDoListActivity";

    private ListView mTaskListView;

    private DatabaseReference databaseRef;
    private DatabaseReference cubeRef;
    private DatabaseReference userRef;
    private DatabaseReference userDataRef;
    private DatabaseReference taskRef;

    private ContactsArrayListAdapter mAdapter;

    private String loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        databaseRef = FirebaseDatabase.getInstance().getReference(); // root ref

        Bundle extra = getIntent().getExtras();
        loggedInUser = extra.getString("uid");
        userDataRef = FirebaseDatabase.getInstance().getReference("crystalCubes/crystal_chan_6/user/" + loggedInUser);

        mTaskListView = (ListView) findViewById(R.id.list_contact);

        // pull list from firebase and display in list view
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_menu, menu);
        setTitle("Contacts");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_contact:
                // generate dialogue
                LayoutInflater factory = LayoutInflater.from(this);
                //text_entry is an Layout XML file containing two text field to display in alert dialog
                final View textEntryView = factory.inflate(R.layout.contact_dialog, null);
                final EditText input1 = (EditText) textEntryView.findViewById(R.id.editText);
                final EditText input2 = (EditText) textEntryView.findViewById(R.id.editText2);

                input1.setText("", TextView.BufferType.EDITABLE);
                input2.setText("", TextView.BufferType.EDITABLE);

                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert
                        .setTitle("Add new contact: ")
                        .setView(textEntryView)
                        .setPositiveButton("Save",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        String name = String.valueOf(input1.getText());
                                        String number = String.valueOf(input2.getText());
                                        // insert to firebase
                                        insertTaskToFirebase(name, number);
                                        updateUI();

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        // on cancel do nothing
                                    }
                                });


                alert.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void insertTaskToFirebase(String name, String number) {
        /*
            get database reference at to-do list node
            create random key then add child {task:<task>}
        */

        // generate random id for task node
        String mKey = userDataRef.child("contacts").push().getKey();

        // set task and id for newTask
        Contacts newContact = new Contacts(name, number, mKey);

        // convert ToDoList object to hash map
        Map<String, Object> postValues = newContact.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        // post content to new node
        childUpdates.put("/contacts/" + mKey, postValues);
        userDataRef.updateChildren(childUpdates);
    }

    private void updateUI() {
        taskRef = userDataRef.child("contacts").getRef();
        taskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // content come as hash map, store are list of todoList object
                ArrayList<Contacts> contacts = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    contacts.add(child.getValue(Contacts.class));
                    Log.d("get contact data", child.getValue().toString());
                }

                // set array list to list view adapter

                mAdapter = new ContactsArrayListAdapter(ContactActivity.this, R.layout.item_contact, contacts);

                mTaskListView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void deleteContact(final View view) {
        // get task and convert to string
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Remove contact:")
                .setMessage("Are you sure you want to remove this contact?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View parent = (View) view.getParent();
                        TextView nameTextView = (TextView) parent.findViewById(R.id.contact_name);
                        final String contactName = String.valueOf(nameTextView.getText());
                        Toast.makeText(getApplicationContext(), "Deleted: " + contactName, Toast.LENGTH_LONG).show();

                        // find task and delete
                        Query queryRef = userDataRef.child("contacts").orderByChild("name").equalTo(contactName);
                        queryRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                dataSnapshot.getRef().setValue(null);
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                })
                .setNegativeButton("No", null)
                .show();


    }
}
