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

    private String loggedInUserString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crystal_main);

        // scrolling text view for logs
        messageArea = (TextView) findViewById(R.id.tv_long);
        messageArea.setMovementMethod(new ScrollingMovementMethod());

        // read firebase data =========================
        // Get a reference to our users
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //todo change ff629b29-9e3a-4de3-9304-2bf209dd9c39 to loggedInUser
        final DatabaseReference logsRef = database.getReference("crystalCubes/crystal_chan_6/user/ff629b29-9e3a-4de3-9304-2bf209dd9c39/logs"); // mack profile

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


        //logged in gmail
        final TextView loggedInUser = (TextView) findViewById(R.id.logged_in_user);
        Bundle extras = getIntent().getExtras();
        loggedInUserString = extras.getString("gmail");
        loggedInUser.setText("Logged in: " + loggedInUserString);
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
                optionIntent.putExtra("gmail", loggedInUserString);
                startActivity(optionIntent);
                return true;
            case R.id.todolist:
                Intent todolistIntent = new Intent(CrystalMainActivity.this, ToDoListActivity.class);
                todolistIntent.putExtra("gmail", loggedInUserString);
                startActivity(todolistIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
