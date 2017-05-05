package edu.temple.crystalcube11;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by hongear on 4/24/17.
 */
public class OptionActivity extends AppCompatActivity{
    private Button setupFaceLogin;
    private Button logout;
    private TextView loggedInUser;
    private String firstname;
    private String lastname;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        // face log in
        setupFaceLogin = (Button) findViewById(R.id.face_login_setup);
        setupFaceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent faceloginIntent = new Intent(OptionActivity.this, FaceLoginActivity.class);
                faceloginIntent.putExtra("lastname", lastname);
                faceloginIntent.putExtra("firstname", firstname);
                faceloginIntent.putExtra("uid", uid);
                startActivity(faceloginIntent);
            }
        });

        // logged in user
        Bundle extras = getIntent().getExtras();
        firstname = extras.getString("firstname");
        lastname = extras.getString("lastname");
        uid = extras.getString("uid");
        loggedInUser = (TextView) findViewById(R.id.logged_in_user);
        loggedInUser.setText("Logged in: " + firstname);
        loggedInUser.setTextSize(15);


        // log out button
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToLogin = new Intent(OptionActivity.this, LoginActivity.class);
                // destroy all activity and start login activity
                goToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(goToLogin);
            }
        });
    }

}
