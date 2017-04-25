package edu.temple.crystalcube11;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by hongear on 4/24/17.
 */
public class OptionActivity extends AppCompatActivity{
    private Button setupFaceLogin;
    private Button logout;
    private Button editProfile;
    private TextView loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        setupFaceLogin = (Button) findViewById(R.id.face_login_setup);
        loggedInUser = (TextView) findViewById(R.id.logged_in_user);

        setupFaceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent faceloginIntent = new Intent(OptionActivity.this, FaceLoginActivity.class);
                startActivity(faceloginIntent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference("crystalCubes/crystal_chan_6/user/ff629b29-9e3a-4de3-9304-2bf209dd9c39/profile");

        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String name = (String) dataSnapshot.child("firstName").getValue();
                    loggedInUser.setText("Logged in: "+name);
                    loggedInUser.setTextSize(15);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
