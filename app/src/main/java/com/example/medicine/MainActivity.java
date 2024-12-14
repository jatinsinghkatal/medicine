package com.example.medicine;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // Declare UI elements
    EditText medname, meddate;
    Button insert, fetch;
    Spinner day;
    Switch switch1;
    TextView medtxt;
    DatabaseConnection dbconnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        medname = findViewById(R.id.medicinename);
        meddate = findViewById(R.id.date);
        insert = findViewById(R.id.insert);
        fetch = findViewById(R.id.fetch);
        day = findViewById(R.id.spinner);
        switch1 = findViewById(R.id.switch1);
        medtxt = findViewById(R.id.medtext);

        // Initialize database connection
        dbconnection = new DatabaseConnection(this);

        // Hide the fetch button initially
        fetch.setVisibility(View.INVISIBLE);

        // Set up switch listener to toggle between insert and fetch UI
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {  // If switch is OFF
                    fetch.setVisibility(View.INVISIBLE);  // Hide fetch button
                    insert.setVisibility(View.VISIBLE);   // Show insert button
                    medname.setVisibility(View.VISIBLE);  // Show medicine name input
                    medtxt.setVisibility(View.VISIBLE);   // Show medicine name label
                } else {  // If switch is ON
                    fetch.setVisibility(View.VISIBLE);   // Show fetch button
                    insert.setVisibility(View.INVISIBLE); // Hide insert button
                    medname.setVisibility(View.INVISIBLE); // Hide medicine name input
                    medtxt.setVisibility(View.INVISIBLE);  // Hide medicine name label
                }
            }
        });

        // Set up insert button click listener
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = medname.getText().toString();
                String date = meddate.getText().toString();
                String time = day.getSelectedItem().toString();

                // Insert data into database
                boolean isInserted = dbconnection.insertValues(name, date, time);
                if (isInserted) {
                    Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_LONG).show();
                    medname.setText("");  // Clear input
                    meddate.setText("");  // Clear input
                } else {
                    Toast.makeText(getApplicationContext(), "Data Not Inserted", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Set up fetch button click listener
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = meddate.getText().toString();
                String time = day.getSelectedItem().toString();

                // Fetch data from database based on selected date and time
                Cursor cursor = dbconnection.fetchData(date, time);
                String medNames = "";
                if (cursor.moveToFirst()) {
                    do {
                        medNames += cursor.getString(cursor.getColumnIndexOrThrow("MedicineName")) + "\n";
                    } while (cursor.moveToNext());

                    // Display fetched medicine names in a Toast
                    Toast.makeText(getApplicationContext(), medNames, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No Entries in Database", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
