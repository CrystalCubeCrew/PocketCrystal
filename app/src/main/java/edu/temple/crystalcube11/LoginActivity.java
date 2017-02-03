package edu.temple.crystalcube11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText userNameET = (EditText) findViewById(R.id.editTextEmailID);
        EditText passwordET = (EditText) findViewById(R.id.editTextPassID);
        Button loginBT = (Button) findViewById(R.id.loginButtonID);

        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (true) { // TODO: 2/2/17 verify credential with db
                    // go to CrystalMainActivity
                    Intent myIntent = new Intent(LoginActivity.this, CrystalMainActivity.class);
                    startActivity(myIntent);
                } else {
                    // TODO: 2/2/17 make EditText red and shake indicating invalid credential
                }
            }
        });


    }
}
