package edu.temple.crystalcube11;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "NewPostActivity";

    private TextView mTextView;

    private Button postBT;

    private DatabaseReference myRef;
    private FirebaseDatabase database;

    private Date mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        database = FirebaseDatabase.getInstance();
        mTime = Calendar.getInstance().getTime();
        myRef = database.getReference("message"+mTime);

        // write to db
        postBT = (Button) findViewById(R.id.postBTid);
        postBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.setValue("Hello, World! "+mTime);
            }
        });

        mTextView = (TextView) findViewById(R.id.textView);

        // Read from the database
        //myRef = database.getReference("users");
        //todo handle hashmap data from firebase
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);

                mTextView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}
