package edu.temple.crystalcube11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CrystalMainActivity extends AppCompatActivity {
    private String TAG = "onDataChange";

    private EditText userID_ET;
    private EditText user_first_name_ET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crystal_main);

        // read firebase data =========================
        // Get a reference to our users
        userID_ET = (EditText) findViewById(R.id.user_id_edit_text);
        user_first_name_ET = (EditText) findViewById(R.id.user_name_edit_text);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("crystalCubes/crystal_chan_6/user");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // pull all data of current user
                User user = dataSnapshot.getValue(User.class);
                Log.d(TAG, String.valueOf(user));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG ,"The read failed: " + databaseError.getCode());
            }
        });

        ref.orderByChild("profile").limitToFirst(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String userID = String.valueOf(dataSnapshot.getKey());
                Log.d(TAG + " userId key", userID);
                userID_ET.setText(String.valueOf(userID));

                ref.child("profile/firstName").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String user_firstname = String.valueOf(dataSnapshot.getValue());
                        user_first_name_ET.setText(user_firstname);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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



        // adapter view for drop down ===================================
        Spinner mySpinner= (Spinner) findViewById(R.id.spinner_view);
        String[] crystalFunctions = {"Home", "Reminder", "Music", "To-do List", "Weather"};

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, crystalFunctions);

        mySpinner.setAdapter(myAdapter);

        //todo make option button work; add options

        AdapterView.OnItemSelectedListener myOISL = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //todo connect to cloud for functionality
                //todo create activity for each functions
                //Toast.makeText(CrystalMainActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(CrystalMainActivity.this, ""+i , Toast.LENGTH_SHORT).show();

                Intent mIntent;

                switch (i) {
                    case 4 :
                        mIntent = new Intent(CrystalMainActivity.this, WeatherActivity.class);
                        startActivity(mIntent);
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
