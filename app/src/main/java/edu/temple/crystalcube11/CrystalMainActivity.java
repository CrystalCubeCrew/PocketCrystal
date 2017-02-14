package edu.temple.crystalcube11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CrystalMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crystal_main);

        Spinner mySpinner= (Spinner) findViewById(R.id.spinner_view);
        String[] crystalFunctions = {"Home", "Reminder", "Music", "To-do List"};

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, crystalFunctions);

        mySpinner.setAdapter(myAdapter);

        //todo make option button work; add options

        AdapterView.OnItemSelectedListener myOISL = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //todo connect to cloud for functionality
                //todo create activity for each functions
                Toast.makeText(CrystalMainActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        mySpinner.setOnItemSelectedListener(myOISL);

    }
}
