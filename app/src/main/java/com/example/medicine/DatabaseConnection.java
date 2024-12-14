package com.example.medicine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseConnection extends SQLiteOpenHelper {

    public DatabaseConnection(Context context) {
        super(context, "Medicinedb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Creating table with appropriate columns
        sqLiteDatabase.execSQL("CREATE TABLE MDTable(MedicineName TEXT, date TEXT, time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Logic for upgrading schema (if needed in the future)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS MDTable");
        onCreate(sqLiteDatabase);
    }

    // Method to insert values into the database
    public boolean insertValues(String medName, String medDate, String medTime) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MedicineName", medName);
        contentValues.put("date", medDate);
        contentValues.put("time", medTime);

        long result = database.insert("MDTable", null, contentValues);
        database.close(); // Always close database to avoid leaks

        return result != -1; // Returns true if the insert was successful
    }

    // Method to fetch data based on date and time
    public Cursor fetchData(String date, String time) {
        SQLiteDatabase database = this.getReadableDatabase();

        // Use parameterized query to avoid SQL injection
        Cursor cursor = database.rawQuery("SELECT * FROM MDTable WHERE date=? AND time=?", new String[]{date, time});

        // We don't close the cursor here because it will be closed by the caller
        return cursor;
    }
}
