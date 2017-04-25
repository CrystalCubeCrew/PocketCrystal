package edu.temple.crystalcube11;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrystalMainActivity extends AppCompatActivity {
    private String TAG = "onDataChange";


    private ImageButton optionMenu;
    TextView messageArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crystal_main);

        // read firebase data =========================
        // Get a reference to our users

        optionMenu = (ImageButton) findViewById(R.id.option_button);
        messageArea = (TextView) findViewById(R.id.tv_long);

        // scrolling text view for logs
        messageArea.setMovementMethod(new ScrollingMovementMethod());

        // option menu
        optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent optionIntent = new Intent(CrystalMainActivity.this, OptionActivity.class);
                startActivity(optionIntent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference logsRef = database.getReference("crystalCubes/crystal_chan_6/user/ff629b29-9e3a-4de3-9304-2bf209dd9c39/logs"); // mack logs

        // logs data retrieve and display
        logsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageArea.setText("");
                messageArea.setTextColor(Color.WHITE);
                messageArea.setTextSize(12);
                int i = 0;
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    i=i+1;
                    messageArea.append("Entry "+i+":\n");
                    String date = (String) child.child("date").getValue();
                    messageArea.append(date+"\n");
                    String intent = (String) child.child("intent").getValue();
                    messageArea.append("Intent: "+intent+"\n");
                    String message = (String) child.child("message").getValue();
                    messageArea.append("Message: "+message+"\n");
                    String speaker = (String) child.child("speaker").getValue();
                    messageArea.append("Source: "+speaker+"\n\n");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //user first name retrieve and display
        final TextView loggedInUser = (TextView) findViewById(R.id.logged_in_user);
        final DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("crystalCubes/crystal_chan_6/user/ff629b29-9e3a-4de3-9304-2bf209dd9c39/profile");

        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("firstName").getValue();
                loggedInUser.setText("Logged in: "+name);
                loggedInUser.setTextSize(14);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // adapter view for drop down ===================================
        Spinner mySpinner= (Spinner) findViewById(R.id.spinner_view);
        String[] crystalFunctions = {"Home", "To-do List"};

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, crystalFunctions);

        mySpinner.setAdapter(myAdapter);

        //todo make option button work; add options

        AdapterView.OnItemSelectedListener myOISL = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Intent todoIntent;

                switch (i) {
                    case 1 : // launch to do list launcher
                        todoIntent= new Intent(CrystalMainActivity.this, ToDoListActivity.class);
                        startActivity(todoIntent);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        mySpinner.setOnItemSelectedListener(myOISL);

    }
}
