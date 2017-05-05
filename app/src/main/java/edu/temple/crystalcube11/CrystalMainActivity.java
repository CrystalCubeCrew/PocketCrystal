package edu.temple.crystalcube11;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CrystalMainActivity extends AppCompatActivity {


    TextView messageArea;

    private String uid;
    private String firstname;
    private String lastname;

    private TextView loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crystal_main);

        //get extras from login activity
        loggedInUser = (TextView) findViewById(R.id.logged_in_user);
        Bundle extras = getIntent().getExtras();
        uid = extras.getString("uid");
        firstname = extras.getString("firstname");
        lastname = extras.getString("lastname");

        // scrolling text view for logs
        messageArea = (TextView) findViewById(R.id.tv_long);
        messageArea.setMovementMethod(new ScrollingMovementMethod());

        // read firebase data =========================
        // Get a reference to our users
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference logsRef = database.getReference("crystalCubes/crystal_chan_6/user/" + uid + "/logs");

        // logs data retrieve and display
        logsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageArea.setText("");
                messageArea.setTextColor(Color.WHITE);
                messageArea.setTextSize(12);
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    i = i + 1;
                    messageArea.append("Entry " + i + ":\n");
                    String date = (String) child.child("date").getValue();
                    messageArea.append(date + "\n");
                    String intent = (String) child.child("intent").getValue();
                    messageArea.append("Intent: " + intent + "\n");
                    String message = (String) child.child("message").getValue();
                    messageArea.append("Message: " + message + "\n");
                    String speaker = (String) child.child("speaker").getValue();
                    messageArea.append("Source: " + speaker + "\n\n");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // set logged in user
        loggedInUser.setText("Logged in: " + firstname);
        loggedInUser.setTextSize(14);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        setTitle("Log");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option:
                Intent optionIntent = new Intent(CrystalMainActivity.this, OptionActivity.class);
                optionIntent.putExtra("firstname", firstname);
                optionIntent.putExtra("lastname", lastname);
                optionIntent.putExtra("uid", uid);
                startActivity(optionIntent);
                return true;
            case R.id.todolist:
                Intent todolistIntent = new Intent(CrystalMainActivity.this, ToDoListActivity.class);
                todolistIntent.putExtra("uid", uid);
                startActivity(todolistIntent);
                return true;
            case R.id.contact:
                Intent contactIntent = new Intent(getApplicationContext(), ContactActivity.class);
                contactIntent.putExtra("uid", uid);
                startActivity(contactIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
